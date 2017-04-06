package sim;

import kernel.ShortTermScheduler;
import machine.DiskDrive;

public class SimulationUI {
	public static final boolean DETAILS = true;
	
	public static void startSimulation(){
		ShortTermScheduler sts = new ShortTermScheduler();
		sts.start();
	}
	
	public static void main(String args[]){
		startSimulation();
	}
}
