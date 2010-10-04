package dk.teamonline.independedServer.rakeTaskRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TaskRunner {
	private String taskName;
	private Map<String, String> parameters;
	
	TaskRunner() {
	}
	
	TaskRunner( String taskName, Map<String, String> parameters ) {
		setTaskName( taskName );
		setParameters( parameters );
	}
	
	public List<String> run() {
		List<String> errorOutput = new LinkedList<String>();

		String command[] = { "rake", taskName };
		try {
			Process p = Runtime.getRuntime().exec( command );
			BufferedReader stderr = new BufferedReader( new InputStreamReader( p.getErrorStream() ) );
			
			String line;
			while ( ( line = stderr.readLine() ) != null ) {
				errorOutput.add( line );
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return errorOutput;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}
