package machine;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kernel.Process;

/*
 *  CPU is a single instance, static class observed by the scheduler.
 *  When it is allocated with a process, it "runs it" in a separate thread
 *  and notifies the scheduler when it is done, so it can be deallocated.
 */

public class CPU extends Observable {
	public static int CLOCK_SPEED = 750; // 750 ticks per second by default
	
	private static final CPU instance = new CPU();
	private static ExecutorService core = Executors.newSingleThreadExecutor();
	private static Process currentProcess;

	// only allow one CPU to be instantiated
	private CPU(){};	
	public static CPU getInstance(){ return instance; }
	
	// notify kernel to deallocate the CPU, 
	// I/O interrupt or other system call
	private void interrupt(){
		this.setChanged();
		this.notifyObservers();
	}

	// load and run a process on the cpu
	public void allocate(Process process){
		currentProcess = process;
		this.run();
	}
	
	// remove a process from the CPU
	public Process deallocate(){
		Process interruptedProcess = currentProcess;
		currentProcess = null;
		return interruptedProcess;
	}
	
	public Process getCurrentProcess() {
		return currentProcess;
	}
	
	// run current process on the CPU (in a separate thread)
 	// using's Executor's single thread service to ensure CPU cannot
 	// call execute more than once simultaneously (a.k.a 1 core)
 	public void run() {
		System.out.println("[#] CPU processing PID " + currentProcess.getPID() + "...");
 		core.execute(new Runnable() { 
 			public void run() { 
 				doWork();
 			} 
 		});
 	}
 	
 	// simulate working for the CPU - burst time by sleeping
 	// for a random amount of time, advancing through burst in segments
 	// (CPU_UPDATE_SPEED) so that GUI can update progress bar in real time
 	private void doWork() {
 		try {
 			int randomBurst = currentProcess.getRandomBurst();
 			do {
 				currentProcess.advanceIP(Config.CPU_UPDATE_SPEED);
 				randomBurst -= Config.CPU_UPDATE_SPEED;
 				Thread.sleep(Config.CPU_UPDATE_SPEED);
 			} while (randomBurst > 0);
 			// simulate an i/o or syscall - return control to kernel
 			this.interrupt();
 		} catch (InterruptedException e) {
 			System.out.println("Illegal CPU state!");
 			e.printStackTrace();
 		}
	 }

}
