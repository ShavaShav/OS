package sim;

import java.util.ArrayList;
import java.util.Random;

import kernel.CPUScheduler;
import kernel.Process;
import machine.CPU;
import machine.Config;
import structures.IODevice;
import structures.Schedule;

public class SimulationUI {
	public static final boolean DETAILS = true;
	public static final int NUM_PROCESSES = 3, MIN_TIME = 1, MAX_TIME = 60;
	
	public static void startSimulation(){
		ArrayList<Process> initialProcesses = new ArrayList<Process>();
		
		// calculate the maxTicks
		Random random = new Random();
		int maxTicks = random.nextInt(((CPU.CLOCK_SPEED * MAX_TIME) - MIN_TIME) + 1) + MIN_TIME;
		
		// initialize the processes, this can be thought of as the long term scheduler
		// which passes the jobs to the short term (CPU) scheduler to assign jobs to CPU
		for (int i = 0; i < NUM_PROCESSES; i ++){
			// creating a bunch of random processes
			int totalTicks = random.nextInt(maxTicks); // random amount of ticks less than the max
			int burstTicks = random.nextInt(totalTicks); // random amount of cpu burst ticks
			int size = totalTicks * 100; // making the size (kb) a function of the amount of ticks
			
			initialProcesses.add(new Process(
				size,    		// KB size 
				false,			// High Priority?
				burstTicks,     // Average CPU Burst ticks (before interrupt)
				totalTicks,     // Estimated total CPU ticks to complete process
				Config.getRandomResources()		// Resources that process needs over lifetime
			));
		}
		
		CPUScheduler sts = new CPUScheduler(Schedule.FCFS, initialProcesses);
		
		// start the device threads
		for (IODevice device : Config.resources)
			new Thread(device).start();
		
		// start the scheduling algorithm
		sts.start();
	}
	
	public static void main(String args[]){
		startSimulation();
	}
}
