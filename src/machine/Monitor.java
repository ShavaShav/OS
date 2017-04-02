package machine;

import structures.IODevice;

public class Monitor extends IODevice{
	// STDOUT should only have one instance available
	private static Monitor instance = new Monitor();
	
	private Monitor(){}; // disallow constuction
	
	// gets STDOUT
	public IODevice getInstance(){
		return instance;
	}
}
