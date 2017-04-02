package machine;

import structures.IODevice;

public class Keyboard extends IODevice {
	// STDIN should only have one instance available
	private static Keyboard instance = new Keyboard();
	
	private Keyboard(){}; // disallow constuction
	
	// gets STDIN
	public IODevice getInstance(){
		return instance;
	}
}
