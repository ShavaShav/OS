package sim;

import java.util.ArrayList;

import kernel.CPUScheduler;
import kernel.Process;
import machine.Config;
import structures.IODevice;
import structures.Schedule;

public class SimulationUI {
	public static final boolean DETAILS = true;
	public static final int NUM_PROCESSES = 100;
	
	public static void startSimulation(){
		ArrayList<Process> initialProcesses = new ArrayList<Process>();
		
		// initialize the processes, this can be thought of as the long term scheduler
		// which passes the jobs to the short term (CPU) scheduler to assign jobs to CPU
		for (int i = 0; i < NUM_PROCESSES; i ++){
			// creating a bunch of random processes
			initialProcesses.add(new Process(
				(int) (Math.random() * 100),    // KB size 
				false,							// High Priority?
				(int) (Math.random() * 1000),    // Average CPU Burst ticks (before interrupt)
				(int)(Math.random() * 10000),  // Estimated total CPU ticks to complete process
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
