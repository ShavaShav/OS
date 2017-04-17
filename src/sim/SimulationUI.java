package sim;

import java.util.ArrayList;
import java.util.Random;

import kernel.CPUScheduler;
import kernel.Process;
import machine.Config;
import structures.IODevice;
import structures.Schedule;

public class SimulationUI {
	public static final boolean DETAILS = true;
	public static final int NUM_PROCESSES = 10, 
			MAX_TICKS = 10000,
			SCHEDULE = Schedule.FCFS;
	
	public static void startSimulation(){
		IODevice.FAST_SPEED = 0.5;
		IODevice.MEDIUM_SPEED = 1;
		IODevice.SLOW_SPEED = 2;
		
		ArrayList<Process> initialProcesses = new ArrayList<Process>();
		
		// calculate the maxTicks
		Random random = new Random();
		
		// initialize the processes, this can be thought of as the long term scheduler
		// which passes the jobs to the short term (CPU) scheduler to assign jobs to CPU
		for (int i = 0; i < NUM_PROCESSES; i ++){
			// creating a bunch of random processes
			int totalTicks = random.nextInt(MAX_TICKS); // random amount of ticks less than the max
			System.out.println(totalTicks);
			int size = totalTicks; // making the size (kb) a function of the amount of ticks
			
			initialProcesses.add(new Process(
				size,    		// KB size 
				false,			// High Priority?
				totalTicks,     // Estimated total CPU ticks to complete process
				Config.getRandomResources()		// Resources that process needs over lifetime
			));
		}
		
		CPUScheduler sts;
		try {
			sts = new CPUScheduler(SCHEDULE, initialProcesses);
			// start the device threads
			for (IODevice device : Config.RESOURCES)
				new Thread(device).start();
			
			// start the scheduling algorithm
			sts.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		startSimulation();
	}
}
