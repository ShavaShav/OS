import java.util.ArrayList;

public class Job {
	public static String NEW = "New";
	public static String READY = "New";
	public static String RUNNING = "New";
	public static String WAITING = "New";
	public static String TERMINATED = "New";
	
	private int JID; 				// job identifier
	private int state;				// current state
	private boolean highPriority;   // true if a high priority
	private double runningTime;		// total estimated running time
	private long size; 				// size of program's data, code, registers, heap etc on memory
	private ArrayList<Resource> resourceList; // list of resources that are used by job
	private double ioBoundProb;		// percentage of time spent doing I/O
	// private sharedVariable;		// for critical section
}