package machine;

import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kernel.Process;

// CPU is static
public class CPU extends Observable {
	public static int CLOCK_SPEED = 750; // 1000 ticks per second
	
	private static final CPU instance = new CPU();
	private static ExecutorService core = Executors.newSingleThreadExecutor();
	private static Process currentProcess;
	
	public ProcessObservable processObservable = new ProcessObservable();
	
	private class ProcessObservable extends Observable {
		public void changeAndNotify(){
			this.setChanged();
			this.notifyObservers();
		}
	};
	
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
		processObservable.changeAndNotify();
		this.run();
	}
	
	// remove a process from the CPU
	public Process deallocate(){
		Process interruptedProcess = currentProcess;
		currentProcess = null;
		processObservable.changeAndNotify();
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
 	// for the amount of cpuBurst ticks (advanceIP) divided
 	/// by the clock speed multiplied by 1000 ( milliseconds in a sec)
 	private void doWork() {
 		try {
 			int randomBurst = currentProcess.getRandomBurst();
 			do {
 				currentProcess.advanceIP(500); // advance half a second
 				randomBurst -= 500;
 				Thread.sleep(500); 	
 			} while (randomBurst > 0);
 			// simulate an i/o or syscall - return control to kernel
 			this.interrupt();
 		} catch (InterruptedException e) {
 			System.out.println("Illegal CPU state!");
 			e.printStackTrace();
 		}
	 }

 	public Observable getProcessObservable(){
 		return processObservable;
 	}
}
