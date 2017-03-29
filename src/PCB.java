// store the context of the process when it is being sqitched.
public class PCB {
	private Job job;
	private int register; // this is our pseudo registers
	
	public PCB(Job job, int register){
		this.job = job;
		this.register = register;
	}
}
