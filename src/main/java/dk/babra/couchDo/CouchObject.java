package dk.babra.couchDo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CouchObject {
	private String dbUrl;
	
	public CouchObject( String dbUrl ) {
		this.dbUrl = dbUrl;
	}
	
	String getDbUrl() {
		return dbUrl;
	}

	void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public JSONObject saveJSONObject( JSONObject doc ) {
		String inLine = "";
		try {
			URL url = new URL( dbUrl );
			URLConnection con = url.openConnection();
			con.setDoOutput( true );
			
			OutputStreamWriter out = new OutputStreamWriter( con.getOutputStream() );
			out.write( doc.toString() );
			out.flush();
			
			BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			inLine = in.readLine();
			out.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ( JSONObject )JSONValue.parse( inLine );
	}
	
	public JSONObject loadJSONObject( URL url ) throws java.io.FileNotFoundException {
		System.out.println( "Request: " + url.toString() );
		JSONObject json = new JSONObject();
		try {
			URLConnection con = url.openConnection();
			BufferedReader reader = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			
			StringBuilder jsonString = new StringBuilder();
			String lastLine;
			while( ( lastLine = reader.readLine() ) != null ) {
				jsonString.append( lastLine );	
			}
			json = ( JSONObject )JSONValue.parse( jsonString.toString() );

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch ( java.io.FileNotFoundException e ) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		System.out.println( "Read JSONObject: " + json.toString() );
		return json;
	}

}
