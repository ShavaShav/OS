package kernel;

import java.util.Observable;
import java.util.Observer;

import structures.State;
import machine.CPU;

public class ShortTermScheduler implements Observer {
	private CPU cpu;
	
	// begin the scheduling algorithm
	public void start(){
		// access CPU, watch it's activities so can deallocate on interrupt
		cpu = CPU.getInstance();
		cpu.addObserver(this);
		
		// allocate initial process and give to CPU - 500 burst ticks, 6000 total
		Process process = new Process(12, true, 1000, 10000);
		process.setState(State.RUNNING);
		cpu.allocate(process);
		
	}
	
	// CPU will notify the scheduler when it receives an interrupt
	@Override
	public void update(Observable arg0, Object arg1) {
		// interrupt received, context switch
		Process process = cpu.deallocate();
		if (process.getTicksRemaining() <= 0){
			process.setState(State.TERMINATED);
		}
		Process nextProcess = process; // for now, just using one process
		System.out.println("Context switching:\t" + process + "\t->  " + nextProcess);
		
		
		if (nextProcess.getState() == State.TERMINATED){
			System.out.println("Done scheduling!");
			System.exit(0);;
		}
		cpu.allocate(nextProcess);
	}
	
}
