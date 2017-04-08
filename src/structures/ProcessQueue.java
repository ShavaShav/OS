package structures;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import kernel.Process;
import machine.Config;
import machine.Monitor;
import sim.QueuePane;

public class ProcessQueue extends Observable {
	private static int INIT = 11; // initial capacity of the queue (java default)
	// a blocking queue allows us to add and remove simultaneously
	// as internally it contains locks that solve the consumer/producer prob
	private BlockingQueue<Process> queue;
	private int schedule;
	
	public ProcessQueue(int schedule) {
		this.schedule = schedule;
		queue = getNewQueue(schedule);
	}
	
	public void setSchedule(int schedule){
		BlockingQueue<Process> newQueue = getNewQueue(schedule);
		newQueue.addAll(queue); // add from queue to new queue according to schedule
		queue = newQueue;	// replace with new queue
		this.schedule = schedule;
		setChanged();
		notifyObservers(Schedule.getName(schedule));
	}
	
	public int getSchedule(){
		return schedule;
	}
	
	public boolean hasNext(){
		return queue.peek() != null;
	}
	
	public int size(){
		return queue.size();
	}
	
	// get the next job, according to schedule
	public Process next() throws InterruptedException {
		Process nextProcess = queue.take(); // blocks if there is nothing in queue
		setChanged();
		notifyObservers(); // notify view of change
		return nextProcess;
	}
	
	// add a job, according to schedule
	public void add(Process process) throws InterruptedException {
		queue.put(process);
		setChanged();
		notifyObservers();
	}
	
	public Iterator<Process> iterator(){
		 return new ProcessIterator();
	}
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}
	
	public void clear(){
		queue.clear();
		setChanged();
		notifyObservers();
	}
	
	private class ProcessIterator implements Iterator{
		Process[] processList;
		int current = 0;
		
		public ProcessIterator(){
			processList = new Process[queue.size()];
			queue.toArray(processList);
			if (schedule != Schedule.FCFS)
				Arrays.sort(processList, Schedule.getComparator(schedule));	// priority queue conversion need to be resorted			
	
			current = 0;
		}
		
		@Override
		public boolean hasNext() {
			return current < processList.length;
		}

		@Override
		public Object next() {
			return processList[current++];
		}
		
	}

	private BlockingQueue<Process> getNewQueue(int schedule){
		BlockingQueue<Process> queue = null;
		switch (schedule){
		case Schedule.FCFS:
			// First Come First Serve (regular queue)
			queue = new LinkedBlockingQueue<Process>();
			break;
		case Schedule.PRIORITY:
			// Priority Queue
			queue = new PriorityBlockingQueue<Process>(INIT, 
						new Schedule.PriorityComparator());
			break;
		case Schedule.SJF:
			// Shortest Total Time First
			queue = new PriorityBlockingQueue<Process>(INIT, 
						new Schedule.SJFComparator());
			break;
		case Schedule.SRT:
			// Shortest Remaining Time First
			queue = new PriorityBlockingQueue<Process>(INIT, 
						new Schedule.SRTComparator());
			break;
		}
		return queue;
	}
	
	public static ProcessQueue getTestQueue(){
		Random random = new Random();
		ProcessQueue pq = new ProcessQueue(Schedule.FCFS);
		for (int i = 0; i < 10; i ++){
			// creating a bunch of random processes
			int totalTicks = random.nextInt(10000); // random amount of ticks less than the max
			System.out.println(totalTicks);
			int size = totalTicks * 100; // making the size (kb) a function of the amount of ticks
			
			try {
				pq.add(new Process(
					size,    		// KB size 
					false,			// High Priority?
					totalTicks,     // Estimated total CPU ticks to complete process
					Config.getRandomResources()		// Resources that process needs over lifetime
				));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return pq;
	}
}
