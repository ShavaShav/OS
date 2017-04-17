package kernel;
import java.util.ArrayList;
import java.util.Random;

import Logging.LifetimeTracker;
import machine.CPU;
import structures.IODevice;
import structures.State;

/*
 *  Process: Modeled on a UNIX process. Includes process control block
 *  information to simply things.
 */
public class Process {
	// logging
//	private LifetimeTracker lt = LifetimeTracker.getInstance();
	
	// some utility variables
	private static final int STACK_SIZE = 4096; // 4 MB
	private static final int MAX_BURST_SECS = 3; // max tim spent bursting in cpu before interupt
	private static int numProcesses = 0;	// init = 0 (first process) incremented on every new proc
	private Random random;
	private int lastBurst;
	
	// Process Control Block
	private int pid; 				// job identifier
	private int state;				// current state (see State.java)
	private boolean highPriority;   // true if a high priority, false if low priority
	private int totalTicks;
	private int ticksRemaining;		// total running CPU time of process (time it takes IP to reach end of instructions)
	
	// Program info
	private int size = 24;	// kb process takes in memory (init to size of process control block = 6 * kbsizeof(int))
	private ArrayList<IODevice> resourceList; // list of resources that are used by job
		
	// A pseudo-process with job header to be constructed by short-term scheduler
	public Process (int size, boolean highPriority, int totalTicks, ArrayList<IODevice> resourceList){
//		lt.startTime = System.nanoTime();
		pid = numProcesses++; // increment for next process
		this.highPriority = highPriority;
		this.totalTicks = totalTicks;
		this.ticksRemaining = totalTicks;	// time remaining is the total time at process birth
		this.size += (size + STACK_SIZE); // size includes stack size
		this.resourceList = resourceList;
		this.setState(State.NEW); // new state
		random = new Random();
	}
	
	// setters
	public void addResource(IODevice resource){ resourceList.add(resource); }
	public void setState(int state) { 
//		lt.endTime = System.nanoTime();
//		lt.logRecord(State.getName(this.state), (lt.endTime-lt.startTime)/(double)1e9);
		
		this.state = state; 

//		if (state == State.TERMINATED){
//			lt.closeCSV();
//		}
	}
	public void setPriority(boolean highPriority) { this.highPriority = highPriority; }
	public static void resetProcessCount(){ numProcesses = 0; }
	
	// getters
	public int getPID() { return pid; }
	public boolean isHighPriority() { return highPriority; }
	public int getTicksRemaining() { return ticksRemaining; }
	public int getTotalTicks() { return totalTicks; }
	public int getState() { return state; }
	public int getSize() { return size; } 
	public int getLastBurst() { return lastBurst; } 
	public int getPercentageDone() { return (int) Math.round(((totalTicks-ticksRemaining)/(double)totalTicks) * 100); }
	public ArrayList<IODevice> getResources() { return resourceList; } 
	public String getCPUTimeRemaining(){ return String.format("%.2f", ((double)ticksRemaining/CPU.CLOCK_SPEED)); }
	public String toString() { return "PID: "+pid+" ("+size+" kb)"; }
	public String fullDetails(){
		return "PID: "+pid+
				"\tTicks: " + ticksRemaining + "/" + totalTicks + 
				"\t" + size + "kb\t" +
				"\t" + (highPriority ? "High" : "Low") + " priority";
	}
	
	/*
	 * SIMULATION METHODS
	 */
	
	// simulates a resource request (syscall) from process while in CPU
	// by picking one of it's resources randomly (scheduler will move it to io)
	public IODevice resourceRequest(){
		int randResource = random.nextInt(resourceList.size());
		return resourceList.get(randResource);				
	}
	
	// get number of ticks that Process could burst for, to be used by CPU to advanceIP()
	public int getRandomBurst(){
		if (resourceList.isEmpty()){ 
			// no resource requests, so IP should advance all the way to end
			System.out.println("No resources, bursting to the end!");
			return ticksRemaining;
		} else {
			// to simulate interrupts at different times, we add a bit of
			// randomness to each cpu burst time interval
			double randProb = Math.random();
			// pick a random amount of ticks, bias towards smaller bursts
		    randProb = Math.pow(randProb, 0.05);
		    // capping lower bound to the cpu clock speed, so it will burst for at least 1 second
		    lastBurst = (int) (CPU.CLOCK_SPEED + (totalTicks - (totalTicks * randProb)));
		    return lastBurst;
		}
	}
	
	// advance the process a number of ticks, return s number of ticks advanced
	public int advanceIP(int ticks){
		ticksRemaining -= ticks;
		if (ticksRemaining < 0) {
			ticks += ticksRemaining;
			ticksRemaining = 0;
		}
		return ticks;
	}
}