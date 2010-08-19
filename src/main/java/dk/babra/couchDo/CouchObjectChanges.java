package dk.babra.couchDo;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CouchObjectChanges extends CouchObject{
	private String filter;
	private String application;
	private int lastSeq = 0;
	private volatile JSONObject changesQueue = new JSONObject(); //contains changes not returned yet

	public CouchObjectChanges( String dbUrl, String application, String filter ) {
		super(dbUrl);
		this.application = application;
		this.filter = filter;
	}
	
	public JSONObject getChangedObject() {
		JSONObject changedObject;

		//First check if we already knows about the next change
		changedObject = getChangeFromQueue();

		//If we have not, get some changes from the database
		if ( changedObject == null ) {
			changedObject = getChangeFromDatabase();
		}
		
		//If both proves without result, wait for a change
		if ( changedObject == null ) {
			changedObject = waitForChange();
		}
		
		return changedObject;
	}
	
	private synchronized JSONObject getChangeFromQueue() {
		JSONArray resultArray = changesQueue.optJSONArray( "results" );
		if ( resultArray == null ) {
			return null;
		}
		
		JSONObject firstResult = resultArray.optJSONObject( 0 );
		if ( firstResult == null ) {
			return null;
		}
		
		resultArray.remove( 0 );
		return returnResult( firstResult );
	}
	
	private JSONObject getChangeFromDatabase() {
		try {
			URL url = new URL( getDbUrl() + "/_changes?since=" + lastSeq + "&filter=" + application + "/" + filter );
			changesQueue = loadJSONObject( url );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// After trying to add some results to the changesQueue, we'll try to load one from it.
		return getChangeFromQueue();
	}
	
	private JSONObject waitForChange() {
		try {
			// I just want one change. If I choose more, when I will wait longer. 
			URL url = new URL( getDbUrl() + "/changes?feed=continuous&limit=1&since=" + lastSeq + "&filter=" + application + "/" + filter );
			changesQueue = loadJSONObject( url );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getChangeFromQueue();
	}
	
	/*private JSONObject privateChangedObject() {
		JSONObject result = new JSONObject();
		URL url;
		
		try {
			//This URL will give me 1 relevant change
			url = new URL( getDbUrl() + "/_changes?since=" + lastSeq + "&filter=" + application + "/" + filter );

			changesQueue = loadJSONObject( url );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray resultArray = (JSONArray) changesQueue.get("results");
		JSONObject firstResult = (JSONObject) resultArray.remove(0);
		
		lastSeq = firstResult.getInt( "seq" );
		String changedDocument = firstResult.getString( "id" );
		
		try {
			URL changedDocumentUrl = new URL( getDbUrl() + "/" + changedDocument );
			result = loadJSONObject( changedDocumentUrl );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	*/
	private synchronized JSONObject returnResult( JSONObject change ) {
		JSONObject result = null;
		try {
			lastSeq = change.getInt( "seq" );
			
			String documentId = URLEncoder.encode( change.getString( "id" ), "UTF-8" );
			URL changedDocumentUrl = new URL( getDbUrl() + "/" + documentId );
			result = loadJSONObject( changedDocumentUrl );
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

}
