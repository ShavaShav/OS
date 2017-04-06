package sim;

import machine.CPU;

public class SimController {

	public static void main (String[] args){
		SimWindow simWindow = new SimWindow();
		
		CPU cpu = CPU.getInstance();
		cpu.addObserver(simWindow);
	}
}
