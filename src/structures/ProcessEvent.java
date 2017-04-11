package structures;

public class ProcessEvent {
	private Process process;
	private String message;
	
	public ProcessEvent(Process process, String message){
		this.process = process;
		this.message = message;
	}
	
	public ProcessEvent(Process process){
		this(process, "");
	}
	
	public ProcessEvent(String message){
		this(null, message);
	}
	
	public Process getProcess() { return process; }
	public String getMessage() { return message; }
}
