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

// The short term process scheduler, that takes jobs, allocates RAM
// and schedules them to the CPU and various IO devices
// Our implementation is event driven, adding and removing processes from
// the different queues based on interrupt events from the CPU and IO devices
// Observable by the view, so it can track terminated processes
public class CPUScheduler extends Observable{
	private CPU cpu;
	private ProcessQueue readyQueue;	// ready queue holds process that are in the READY state.
	private boolean isRunning;
	
	public CPUScheduler(int schedule, ArrayList<Process> initialProcesses) throws Exception{
		isRunning = false;
		
		// access CPU, watch it's activities so can deallocate on interrupt
		cpu = CPU.getInstance();
		cpu.deleteObservers();	// delete existing observers if any
		cpu.addObserver(new CPUObserver());
		
		// access the system's resources, listen for IO completions
		for (IODevice resource : Config.RESOURCES) {
			resource.deleteObservers();
			resource.addObserver(new ResourceObserver());
		}
		
		// initialize ready queue according to schedule
		readyQueue = new ProcessQueue(schedule);

		// load initial processes into memory and add them to the ready queue
		for (Process process : initialProcesses)
			scheduleProcess(process);
	}

	public ProcessQueue getReadyQueue(){ return readyQueue; }
	
	// true if the scheduler is currently running
	public boolean isRunning(){ return isRunning; }
	
	// get the schedule that the scheduler is using for it's ready queue
	public int getSchedule() { return readyQueue.getSchedule(); }
	
	// load a NEW process into RAM and insert into ready queue
	public void scheduleProcess(Process process) throws Exception{
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
		if (readyQueue.isEmpty())
			return;

		// allocate initial process and give to CPU
		Process process;
		try {
			isRunning = true;
			process = readyQueue.next(); // get the first process from readyqueue
			process.setState(State.RUNNING); // set state to RUNNING
			System.out.println("<-> Switching in: " + process);
			cpu.allocate(process); // allocate CPU, will get update when it's done
			sendProcessToObservers(process); // notify view
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
				process.setState(State.WAITING);
				System.out.println("<-> Switching out: " + process);
				
				// if process was completed by CPU, terminate it (GUI will keep list)
				if (process.getTicksRemaining() <= 0){
					RAM.unloadProcess(process);			// deallocate memory taken by process
					process.setState(State.TERMINATED);	// set state to TERMINATED
					System.out.println("\n*** PID " + process.getPID() + " is terminating! ***");
				}	
				sendProcessToObservers(process); // send to gui so it can update panel(s)

				// move process to IO queue if it's not terminated (since it received interupt)
				// since non-preemptive kernel, processes are only WAITING if they request resources
				if (process.getState() != State.TERMINATED){
					moveToIOQueue(process);
				}
				
				// get the next process from ready queue and assign it to the CPU
				try {
					while (readyQueue.isEmpty()) Thread.sleep(500); // give queue time to show process
					Process nextProcess = readyQueue.next();
					System.out.println("\n<-> Switching in: " + nextProcess);
					nextProcess.setState(State.RUNNING);
					cpu.allocate(nextProcess);
					sendProcessToObservers(nextProcess); // send to gui so it can update running panel
				} catch (InterruptedException e) {
					System.err.println("Unable to get next process from ready queue.");
					e.printStackTrace();
				}
			
		}	
	}
	
	private void sendProcessToObservers(Process process){
		synchronized(this){
			setChanged();
			notifyObservers(process);			
		}
	}
	
	// move a process to it's requested IO queue (invoked on interrupt)
	private void moveToIOQueue(Process process){
		IODevice device = process.resourceRequest(); // get the processes requested resource
		System.out.println("\n==> Moving PID " + process.getPID() + " to "+device.getName()+"'s Queue");
		try {
			device.addToQueue(process);		// add it to resource's IO queue
		} catch (InterruptedException e) {
			System.err.println("Unable to move PID " + process.getPID() +
					" to " + device.getName() + "'s IO queue.");
			e.printStackTrace();
		}
	}
	
	// Scheduler will watch resources for IO completion so it can put them back in ready queue
	private class ResourceObserver implements Observer {
		@Override
		public void update(Observable obs, Object o) {
			// check that update in fact received from IODevice
			if (! (obs instanceof IODevice)) {
				System.err.println("ResourceObserver malfunction in CPUScheduler.");
				return;
			}
			IODevice device = (IODevice) obs; // get the device that sent notification
			
			// device will send a process when it completes it
			if (o instanceof Process){
				Process doneIO = (Process) o; // get process that device sent
				System.out.println("\n(() " + device.getName() + " finished IO for PID " + doneIO.getPID());
				doneIO.setState(State.READY); // put process in READY state
				try {
					readyQueue.add(doneIO);	// add back into ready queue
				} catch (InterruptedException e) {
					System.err.println("Unable to add " + doneIO.getPID() + " back into ready queue.");
					e.printStackTrace();
				}
			}
		}	
	}

	// can change the schedule on the fly using this method, replaces ready queue with new one
	public void setSchedule(int schedule) {
		readyQueue.setSchedule(schedule);
	}
	
}
