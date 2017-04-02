package kernel;
import java.util.ArrayList;
import java.util.Random;

import machine.CPU;
import structures.Resource;
import structures.State;

/*
 *  Process: Modeled on a UNIX process. Includes process control block
 *  information to simply things.
 */
public class Process {
	private static final int STACK_SIZE = 4096; // 4 MB
	private static int numProcesses = 0;	// init = 0 (first process) incremented on every new proc
	private Random random;
	// Process Control Block
	private int pid; 				// job identifier
	private int state;				// current state (see State.java)
	private boolean highPriority;   // true if a high priority, false if low priority
	private int totalTicks;
	private int ticksRemaining;		// total running time of process (time it takes IP to reach end of instructions)
	private int cpuBurst;			// average # of cpu ticks before interrupt
	// Program info
	private int size = 24;	// kb process takes in memory (init to size of process control block = 6 * kbsizeof(int))
	private ArrayList<Resource> resourceList; // list of resources that are used by job
		
	// A pseudo-process with job header to be constructed by short-term scheduler
	public Process (int size, boolean highPriority, int cpuBurst, int totalTicks){
		pid = numProcesses++; // increment for next process
		this.highPriority = highPriority;
		this.totalTicks = totalTicks;
		this.ticksRemaining = totalTicks;	// time remaining is the total time at process birth
		this.cpuBurst = cpuBurst;
		this.size += (size + STACK_SIZE); // size includes stack size
		state = State.NEW; // new state
		
		resourceList = new ArrayList<Resource>();
		random = new Random();
	}
	
	// setters
	public void addResource(Resource resource){ resourceList.add(resource); }
	public void setState(int state) { this.state = state; }
	public void setPriority(boolean highPriority) { this.highPriority = highPriority; }
	
	// getters
	public int getPID() { return pid; }
	public boolean isHighPriority() { return highPriority; }
	public int getTicksRemaining() { return ticksRemaining; }
	public int getTotalTicks() { return totalTicks; }
	public int getState() { return state; }
	public int getSize() { return size; } 
	private String getCPUTimeRemaining(){ return String.format("%.2f", ((double)ticksRemaining/CPU.CLOCK_SPEED)); }
	public String toString() { return "PID: "+pid+" ("+getCPUTimeRemaining()+" secs left)"; }
	
	// simulates resource requested from process while in CPU
	public Resource getRequestedResource(){
		return null;
	}
	
	// simulate the instruction pointer register advancing towards the end
	public int advanceIP() { 
		// to simulate interrupts at different times, adding a bit of randomness to each cpu burst time inteval
		double randomBurst = (int) (random.nextGaussian() * (cpuBurst/5) + cpuBurst);
		int randomTicks = (int) Math.round(randomBurst);
		ticksRemaining -= randomTicks;
		if (ticksRemaining < 0) ticksRemaining = 0;
		return randomTicks;
	}
}