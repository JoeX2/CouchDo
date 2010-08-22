package dk.babra.couchDo;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.json.simple.JSONObject;

public class App {

	public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
		CouchObjectChanges ccc = new CouchObjectChanges( "http://udvikling-PC.teamonline.dk:5984/progressinator", "app", "test-task" );
		while( true ) {
			JSONObject myTask = ccc.getChangedObject();
			System.out.println( "The task: " + myTask );
			
			myTask.put( "message", "Started" );
			ccc.saveJSONObject( myTask );
			
			for( int counter = 0; counter < 100; counter += 5 ) {
				myTask = ccc.loadJSONObject( "test-task" );
				myTask.put( "message", counter + "% completed" );
				ccc.saveJSONObject( myTask );
				sleep( 5 );
			}
			
			myTask = ccc.loadJSONObject( "test-task" );
			myTask.put( "message", "Done" );
			myTask.put( "done", "true" );
			ccc.saveJSONObject( myTask );
		}
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
