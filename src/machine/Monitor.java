package machine;

import structures.IODevice;

public class Monitor extends IODevice {
	// STDOUT should only have one instance available
	private static final Monitor instance = new Monitor();
	public static Monitor getInstance(){ return instance; }

	private Monitor(){
		super();
		speed = FAST_SPEED; // we are taking monitor output to be the fastest i/o device
		name = "Monitor";
	}
}
