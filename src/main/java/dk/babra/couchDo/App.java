package dk.babra.couchDo;

public class App {

	public static void main(String[] args) {
		CouchObjectChanges ccc = new CouchObjectChanges( "http://192.168.3.1:5984/progressinator", "app", "tasks" );
		while( true ) {
			System.out.println( "The doc: " + ccc.getChangedObject() );
		}
	}

}
