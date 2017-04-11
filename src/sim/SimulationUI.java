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
	public static final int NUM_PROCESSES = 2, 
			MAX_TICKS = 100000,
			SCHEDULE = Schedule.FCFS;
	
	public static void startSimulation(){
		ArrayList<Process> initialProcesses = new ArrayList<Process>();
		
		// calculate the maxTicks
		Random random = new Random();
		
		// initialize the processes, this can be thought of as the long term scheduler
		// which passes the jobs to the short term (CPU) scheduler to assign jobs to CPU
		for (int i = 0; i < NUM_PROCESSES; i ++){
			// creating a bunch of random processes
			int totalTicks = random.nextInt(MAX_TICKS); // random amount of ticks less than the max
			System.out.println(totalTicks);
			int size = totalTicks * 100; // making the size (kb) a function of the amount of ticks
			
			initialProcesses.add(new Process(
				size,    		// KB size 
				false,			// High Priority?
				totalTicks,     // Estimated total CPU ticks to complete process
				Config.getRandomResources()		// Resources that process needs over lifetime
			));
		}
		
		CPUScheduler sts = new CPUScheduler(SCHEDULE, initialProcesses);
		
		// start the device threads
		for (IODevice device : Config.RESOURCES)
			new Thread(device).start();
		
		// start the scheduling algorithm
		sts.start();
	}
	
	public static void main(String args[]){
		startSimulation();
	}
}
