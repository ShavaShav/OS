package structures;

// utility class for defining state of process
public class State {
	public static final int NEW = 0; 
	public static final int READY = 1; 
	public static final int RUNNING = 2; 
	public static final int WAITING = 3; 
	public static final int TERMINATED = 4;
	
	public static String getName(int state){
		switch (state){
		case 0:
			return "New";
		case 1:
			return "Ready";
		case 2:
			return "Running";
		case 3:
			return "Waiting";
		case 4:
			return "Terminated";
		default:
			return "Unknown";
		}
	}
}
