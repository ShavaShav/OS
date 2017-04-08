package kernel;
import java.util.ArrayList;
import java.util.Random;

import machine.CPU;
import machine.Config;
import structures.IODevice;
import structures.State;

/*
 *  Process: Modeled on a UNIX process. Includes process control block
 *  information to simply things.
 */
public class Process {
	private static final int STACK_SIZE = 4096; // 4 MB
	private static final int MAX_BURST_SECS = 4; // max tim spent bursting in cpu before interupt
	private static int numProcesses = 0;	// init = 0 (first process) incremented on every new proc
	private Random random;
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
		pid = numProcesses++; // increment for next process
		this.highPriority = highPriority;
		this.totalTicks = totalTicks;
		this.ticksRemaining = totalTicks;	// time remaining is the total time at process birth
		this.size += (size + STACK_SIZE); // size includes stack size
		this.resourceList = resourceList;
		state = State.NEW; // new state
		random = new Random();
	}
	
	// setters
	public void addResource(IODevice resource){ resourceList.add(resource); }
	public void setState(int state) { this.state = state; }
	public void setPriority(boolean highPriority) { this.highPriority = highPriority; }
	public static void resetProcessCount(){ numProcesses = 0; }
	
	// getters
	public int getPID() { return pid; }
	public boolean isHighPriority() { return highPriority; }
	public int getTicksRemaining() { return ticksRemaining; }
	public int getTotalTicks() { return totalTicks; }
	public int getState() { return state; }
	public int getSize() { return size; } 
	public int getPercentageDone() { return (int) Math.round(((totalTicks-ticksRemaining)/(double)totalTicks) * 100); }
	public ArrayList<IODevice> getResources() { return resourceList; } 
	private String getCPUTimeRemaining(){ return String.format("%.2f", ((double)ticksRemaining/CPU.CLOCK_SPEED)); }
	public String toString() { return "PID: "+pid+" ("+size+" kb)"; }
	
	public String fullDetails(){
		return "PID: "+pid+
				"\tTicks: " + ticksRemaining + "/" + totalTicks + 
				"\t" + size + "kb\t" +
				"\t" + (highPriority ? "High" : "Low") + " priority";
	}
	
	// simulates a resource request (syscall) from process while in CPU
	// by picking one of it's resources randomly (scheduler will move it to io)
	public IODevice resourceRequest(){
		int randResource = random.nextInt(resourceList.size());
		return resourceList.get(randResource);				
	}
	
	// simulate the instruction pointer register advancing towards the end
	public int advanceIP() { 
		if (resourceList.isEmpty()){ // no resource requests, so IP should advance all the way to end
			System.out.println("No resources, bursting to the end!");
			int cpuBurst = ticksRemaining;
			ticksRemaining = 0;
			return cpuBurst; // returning the ticks remaining as one cpu burst
		} else {
			// to simulate interrupts at different times, adding a bit of randomness to each cpu burst time inteval
			int randomTicks = random.nextInt(CPU.CLOCK_SPEED * MAX_BURST_SECS); // have them spend less than a second in CPU fro demo purposes
			ticksRemaining -= randomTicks;
			if (ticksRemaining < 0) {
				randomTicks -= ticksRemaining;
				ticksRemaining = 0;
			}
			return randomTicks;			
		}
	}
}