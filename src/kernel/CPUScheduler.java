package kernel;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import structures.IODevice;
import structures.ProcessQueue;
import structures.State;
import machine.CPU;
import machine.Config;
import machine.RAM;

// The short term process scheduler, that takes jobs, loads them into RAM
// and schedules them to the CPU. 
// Our implementation is event driven, adding and removing processes from
// the different queues based on interrupt events from the CPU and IO devices
public class CPUScheduler {
	private CPU cpu;
	private ProcessQueue readyQueue;	// ready queue holds process that are in the READY state.
	private int numResources = 0;
	private ArrayList<Process> completedProcesses; // will hold the completed processes (for now.. might not need/want too)

	public CPUScheduler(int schedule, ArrayList<Process> initialProcesses){

		// access CPU, watch it's activities so can deallocate on interrupt
		cpu = CPU.getInstance();
		cpu.addObserver(new CPUObserver());
		
		// access the system's resources, listen for IO completions
		for (IODevice resource : Config.resources)
			resource.addObserver(new ResourceObserver());
		
		// initialize ready queue according to schedule
		readyQueue = new ProcessQueue(schedule);

		// load initial processes into memory and add them to the ready queue
		for (Process process : initialProcesses)
			scheduleProcess(process);
		
		completedProcesses = new ArrayList<Process>();
	}
	
	// get the schedule that the scheduler is using for it's ready queue
	public int getSchedule() { return readyQueue.getSchedule(); }
	
	// load a NEW process into RAM and insert into ready queue
	public void scheduleProcess(Process process){
		try {
			RAM.loadProcess(process);		// load process into memory
			process.setState(State.READY);	// set the state to READY
			readyQueue.add(process);		// add to ready queue
		} catch (InterruptedException e) {
			System.err.println("Unable to add PID " + process.getPID() + " to ready queue.");
			e.printStackTrace();
		}
	}
	
	// begin the scheduling algorithm, it won't start until this is called!
	public void start(){	
		// allocate initial process and give to CPU - 500 burst ticks, 6000 total
		Process process;
		try {
			process = readyQueue.next(); // get the first process from readyqueue
			process.setState(State.RUNNING); // set state to RUNNING
			cpu.allocate(process); // allocate CPU, will get update when it's done
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// move a process to it's requested IO queue (invoked on interrupt)
	private void moveToIOQueue(Process process){
		IODevice device = process.resourceRequest(); // get the processes request resource
		System.out.println("Moving PID " + process.getPID() + " to "+device.getName()+"'s Queue");
		try {
			device.addToQueue(process);				// add it to resource's IO queue
		} catch (InterruptedException e) {
			System.err.println("Unable to move PID " + process.getPID() +
					" to " + device.getName() + "'s IO queue.");
			e.printStackTrace();
		}
	}
	
	// Scheduler will watch CPU to know when to allocate/deallocate
	private class CPUObserver implements Observer {
		@Override
		public void update(Observable obs, Object o) {
			if (! (obs instanceof CPU)) {
				System.err.println("CPUObserver malfunction in CPUScheduler.");
				return;
			}
			
			// interrupt received, get from CPU and context switch
			Process process = cpu.deallocate();
			System.out.println("Switching out: " + process);
			process.setState(State.WAITING);
			
			// if process was completed by CPU, terminate it
			if (process.getTicksRemaining() <= 0){
				RAM.unloadProcess(process);			// deallocate memory taken by process
				process.setState(State.TERMINATED);	// set state to TERMINATED
				completedProcesses.add(process);	// adding to pile of completed process (WOULDN"T EXIST IN A REAL COMPUTER)
			}
			
			// move process to IO queue if it's in a WAITING state
			// since non-preemptive kernel, processes are only WAITING if they request resources
			if (process.getState() != State.TERMINATED){
				moveToIOQueue(process);
			}
				
			// get the next process from ready queue and assign it to the CPU
			try {
				Process nextProcess = readyQueue.next();
				System.out.println("\nSwitching in: " + nextProcess);
				
				nextProcess.setState(State.RUNNING);
				cpu.allocate(nextProcess);
			} catch (InterruptedException e) {
				System.err.println("Unable to get next process from ready queue.");
				e.printStackTrace();
			} // for now, just using one process
		}	
	}
	
	// Scheduler will watch resources for IO completion so it can put them back in ready queue
	private class ResourceObserver implements Observer {
		@Override
		public void update(Observable obs, Object o) {
			if (! (obs instanceof IODevice)) {
				System.err.println("CPUObserver malfunction in CPUScheduler.");
				return;
			}
			IODevice device = (IODevice) obs; // get the device that sent notification
			
			// device will send a process with update when it completes it
			if (o instanceof Process){
				Process doneIO = (Process) o; // get process that device sent with notify
				System.out.println(device.getName() + " finished IO for PID " + doneIO.getPID());
				doneIO.setState(State.READY); // put process in READY state
				try {
					readyQueue.add(doneIO);
				} catch (InterruptedException e) {
					System.err.println("Unable to add " + doneIO.getPID() + " back into ready queue.");
					e.printStackTrace();
				}
			}
		}	
	}
	
}
