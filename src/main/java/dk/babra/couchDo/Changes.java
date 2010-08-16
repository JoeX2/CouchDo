package dk.babra.couchDo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

public class Changes {
	private String dbUrl;
	
	public Changes( String dbUrl ) {
		this.dbUrl = dbUrl;
	}
	
	public JSONObject getOneChange() {
		JSONObject result = new JSONObject();
		int lastSeq = getLastSeq();
		boolean gotChange = false;
		
		try {
            String lastChangeId = "";
			while( !gotChange ) {
				URL lastChange = new URL( dbUrl + "_changes?since=" + lastSeq + "&feed=continuous" );
				JSONObject lastChangeDoc = loadJSONObject( lastChange );
				lastChangeId = lastChangeDoc.optString( "id" );
				if( !lastChangeId.equals( "" ) ) {
					gotChange = true;
				}
				else if( lastChangeId.equals( "lastSeqRead" ) ) {
					lastSeq++;
				}
			}
			
			URL newDoc = new URL( dbUrl + URLEncoder.encode( lastChangeId, "UTF-8" ) );
			result = loadJSONObject( newDoc );
			incrementLastSeq();				
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private int incrementLastSeq() {
		int newSeq = 0;
		try {
			URL oldSeq = new URL( dbUrl + "lastSeqRead" );
			JSONObject seqDoc = loadJSONObject( oldSeq );
			
			newSeq = seqDoc.getInt( "last_seq" ) + 2;
			seqDoc.element( "last_seq", newSeq );
			
			saveJSONObject( seqDoc );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newSeq;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return JSONObject.fromObject( inLine );
	}
	
	private int getLastSeq() {
		int lastSeq = 0;
		try {
			URL lastDocURL = new URL( dbUrl + "lastSeqRead" );
			JSONObject lastDoc = loadJSONObject( lastDocURL );
			lastSeq = lastDoc.getInt( "last_seq" );
			
		} catch ( java.io.FileNotFoundException e ) {
			createLastSeq();
			lastSeq = getLastSeq();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return lastSeq;		
	}
	
	private void createLastSeq() {
		try {
			URL changeSeqURL = new URL( dbUrl + "_changes" );
			JSONObject changeSeq = loadJSONObject( changeSeqURL );
			int lastSeq = changeSeq.getInt( "last_seq" );
			
			URL lastDoc = new URL( dbUrl );
			URLConnection con = lastDoc.openConnection();
			con.setDoOutput( true );
			
			OutputStreamWriter out = new OutputStreamWriter( con.getOutputStream() );
			String lastSeqDocString = ( "{'_id':'lastSeqRead','last_seq':" + ( lastSeq + 1 ) + "}" ).replace( "'", "\"" );
			out.write( lastSeqDocString );
			out.flush();
			
			BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			String inLine = in.readLine();
			
			System.out.println( "createLastSeq Result:" + inLine );
			
			out.close();
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JSONObject loadJSONObject( URL url ) throws java.io.FileNotFoundException {
		JSONObject json = new JSONObject();
		try {
			URLConnection con = url.openConnection();
			BufferedReader reader = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			
			StringBuilder jsonString = new StringBuilder();
			String lastLine;
			boolean done = false;
			while( ( lastLine = reader.readLine() ) != null && done == false) {
				jsonString.append( lastLine );
				
				try {
					done = true;
					json = JSONObject.fromObject( jsonString.toString() );
					
				} catch ( net.sf.json.JSONException e ) {
					done = false;
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( java.io.FileNotFoundException e ) {
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println( "Read JSONObject: " + json.toString() );
		return json;
	}
}
