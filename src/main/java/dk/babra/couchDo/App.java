package dk.babra.couchDo;

import net.sf.json.JSONObject;

public class App {

	public static void main(String[] args) {
		String json = "{name=\"json test\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";
		JSONObject jsonObject = JSONObject.fromObject( json );
		System.out.println( jsonObject.get( "name" ) );
		
		while( true ) {
			Changes c = new Changes( "http://192.168.3.1:5984/progressinator/" );
			System.out.println( "The doc: " + c.getOneChange() );			
		}
	}

}
