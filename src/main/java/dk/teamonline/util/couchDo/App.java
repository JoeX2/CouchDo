package dk.teamonline.util.couchDo;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {
	
	public static String couchDBAddress = "http://192.168.3.1:5984/progressinator";

	public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
		CouchObjectChanges ccc = new CouchObjectChanges( couchDBAddress, "rake", "rakeTask" );
		
	}
	
	private static void sleep( int time ) {
		try {
			Thread.currentThread().sleep( time * 1000 );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
