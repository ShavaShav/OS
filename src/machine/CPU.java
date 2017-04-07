package machine;

import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kernel.Process;

// CPU is static
public class CPU extends Observable {
	public static final int CLOCK_SPEED = 1000; // 1000 ticks per second
	
	private static final CPU instance = new CPU();
	private static Executor core = Executors.newSingleThreadExecutor();
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
 			Thread.sleep((currentProcess.advanceIP()/CLOCK_SPEED) * 1000);
 			// simulate an i/o or syscall - return control to kernel
 			this.interrupt();
 		} catch (InterruptedException e) {
 			System.out.println("Illegal CPU state!");
 			e.printStackTrace();
 		}
	 }

}
