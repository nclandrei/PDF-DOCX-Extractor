/**
 *  Main class for processing a JSON file that contains data related to tablees
 *  extracted by tabula.
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.lang.StringBuilder;
import java.io.FileWriter;
import java.io.BufferedWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParseTable {
	public static void main(String[] args) {

		JSONParser parser = new JSONParser();
		StringBuilder sb = new StringBuilder();

		try (BufferedWriter buffer = new BufferedWriter(new FileWriter("output.csv"))){
			JSONArray array = (JSONArray) parser.parse(new FileReader("./tables.json"));
			for (Object obj : array) {
				JSONObject table = (JSONObject) obj;
				JSONArray cells = (JSONArray) table.get("data");
				for (Object c : cells) {
					JSONArray cellProperties = (JSONArray) c;
					sb = new StringBuilder();
					for (Object p : cellProperties) {
						JSONObject property = (JSONObject) p;
						String text = (String) property.get("text");
						if (text.compareTo(" ") == 0 || text.compareTo("") == 0) {
							break;
						}
						sb.append(text + ",");
						
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

		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
