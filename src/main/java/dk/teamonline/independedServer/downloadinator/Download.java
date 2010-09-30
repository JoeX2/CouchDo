package dk.teamonline.independedServer.downloadinator;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import dk.teamonline.util.couchDo.CouchObjectChanges;

public class Download {
	private static Map<String, String> fileLocation;
	
	public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
		String downloadHost = "http://localhost:8080";
		fileLocation = new HashMap<String, String>();
		fileLocation.put("download-debitor", "%HOMEPATH%\\Dokumenter");
		fileLocation.put("download-invoice", "%HOMEPATH%\\Dokumenter");

		URL couchDBAddress = new URL( "http://bosted204.bosted.net:5984/downloadinator" );
		CouchObjectChanges ccc = new CouchObjectChanges( couchDBAddress );
		
		while( true) {
			JSONObject changedObject = ccc.getChangedObject( false );

			// Test if it something we should download
			if (    changedObject.containsKey( "type" ) &&
					changedObject.containsKey( "filename" ) &&
					changedObject.containsKey( "url" ) &&
					fileLocation.containsKey( changedObject.get( "type" ) )
			   ) {
				// It is. Start downloading
				URL inputUrl = new URL( downloadHost + changedObject.get( "url" ).toString() );
				File outputDir = new File( fileLocation.get( changedObject.get( "type" ) ) );
				outputDir.mkdirs();
				File outputFile = new File( outputDir, changedObject.get( "filename" ).toString() );
				if ( download( inputUrl, outputFile ) ) {
					//If download is a success, when don't download it again.
					ccc.deleteJSOBObject( changedObject );
				}
			}
		}
	}
	
	public static boolean download( URL inputUrl, File outputFile ) {
		System.out.println( "Downloading from: " + inputUrl + " ...");
		
		try {
			DataInputStream dis = new DataInputStream( inputUrl.openStream() );
			FileWriter fw = new FileWriter( outputFile );
			while( true ) {
				int i = dis.read();
				if( i == -1 ) {
					break;
				}
				fw.write( i );
			}
			
			fw.flush();
			fw.close();
			dis.close();
			
			

			System.out.println( "    Succes" );
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
