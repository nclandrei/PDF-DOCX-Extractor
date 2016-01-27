package uk.ac.glasgow.dcs.psd;

import java.io.*;
import java.lang.StringBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PDFTableExtraction{

    public static void process (String  fileName){
        generateJSON(fileName);
        processJSON(fileName);
    }

    public static void generateJSON(String fileName){

        //finding the relative path to the tabula jar
        StringBuilder tabulaPath = new StringBuilder();
        int counter = 0;
        for(int i = fileName.length() - 1; i >= 0; i--){
            //ensures functionality on windows and linux
            if(fileName.charAt(i) == '/' || fileName.charAt(i) == '\\'){
                counter++;
            }
            if(counter == 4){
                tabulaPath.append(fileName.substring(0, i + 1));
                break;
            }
        }
        tabulaPath.append("tabula-0.8.0-jar-with-dependencies.jar");

        try{
            // run ProcessBuilder instead
            ProcessBuilder pb = new ProcessBuilder("java", "-jar",
                    tabulaPath.toString(), fileName + ".pdf",
                    "-i", "-pall", "-r", "-f", "JSON"); // jar params to output JSON
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            try (BufferedWriter buffer = new BufferedWriter(new FileWriter( fileName + ".json"))){
                while ((line = reader.readLine())!= null) {
                    buffer.write(line + "\n");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            p.destroy();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void processJSON(String fileName) {

        JSONParser parser = new JSONParser();
        StringBuilder sb = new StringBuilder();

        try (BufferedWriter buffer = new BufferedWriter(new FileWriter (fileName + ".csv"))){
            JSONArray array = (JSONArray) parser.parse(new FileReader (fileName + ".json"));
            for (Object obj : array) {
                JSONObject table = (JSONObject) obj;
                JSONArray cells = (JSONArray) table.get("data");
                for (Object c : cells) {
                    JSONArray cellProperties = (JSONArray) c;
                    sb = new StringBuilder();
                    for (Object p : cellProperties) {
                        JSONObject property = (JSONObject) p;
                        String text = (String) property.get("text");
                        if (text.compareTo(" ") == 0 || text.compareTo("") == 0 || text.length() > 100) {
                            break;
                        }
                        sb.append("\"").append(text).append("\",");
                    }
                    if(sb.toString().compareTo("") == 0){
                        continue;
                    }

                    sb.deleteCharAt(sb.length()-1);
                    buffer.write(sb.toString());
                    buffer.write("\n");
                }
                if(sb.toString().compareTo("") != 0){
                    buffer.write("\n");
                }
            }

            //delete the used json
            File jsonFile = new File(fileName + ".json");
            //noinspection ResultOfMethodCallIgnored
            jsonFile.delete();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}