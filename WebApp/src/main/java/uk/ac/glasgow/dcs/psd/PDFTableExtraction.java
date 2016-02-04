package uk.ac.glasgow.dcs.psd;

import java.io.*;
import java.lang.StringBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PDFTableExtraction{

    public static void process (String  fileName){
        generateJSON(fileName);
        processJSON(fileName);
        ZipMaker.createZip(fileName);
        try {
            ZipMaker.delete(new File(fileName));
        }
        catch(IOException e){
            e.printStackTrace();
        }
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

        File outDir = new File(fileName);
        outDir.mkdir();

        JSONParser parser = new JSONParser();
        StringBuilder sb = new StringBuilder();

        try {
            JSONArray array = (JSONArray) parser.parse(new FileReader(fileName + ".json"));
            int csvName = 0;

            for (Object obj : array) {
                JSONObject table = (JSONObject) obj;
                JSONArray cells = (JSONArray) table.get("data");

                for (Object c : cells) {
                    JSONArray cellProperties = (JSONArray) c;

                    for (Object p : cellProperties) {
                        JSONObject property = (JSONObject) p;
                        String text = (String) property.get("text");
                        if (text.compareTo(" ") == 0 || text.compareTo("") == 0 || text.length() > 100) {
                            break;
                        }
                        sb.append("\"").append(text).append("\",");
                    }

                    if (sb.toString().compareTo("") == 0) {
                        continue;
                    }
                    sb.append("\n");
                }

                if(!sb.toString().isEmpty()) {
                    try (BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName + File.separator + csvName + ".csv"))) {
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 1);
                        buffer.write(sb.toString());
                        csvName++;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    sb = new StringBuilder();
                }
            }

            //delete the used json
            File jsonFile = new File(fileName + ".json");
            jsonFile.delete();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}