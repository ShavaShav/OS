package structures;

import kernel.Process;

// resources can only be used by one process at a time
public abstract class Resource {
	protected ProcessQueue queue;	// processes in line to use resource
	protected int id;				// id of resource
	public void addToQueue(Process process){
		queue.add(process);
	};
	
	public Process getNextCompletedProcess(){
		return queue.getNext();
	}
}
