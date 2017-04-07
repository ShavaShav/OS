package machine;

import structures.IODevice;

public class Keyboard extends IODevice {
	// STDIN should only have one instance available
	private static final Keyboard instance = new Keyboard();
	public static Keyboard getInstance(){ return instance; }

	private Keyboard(){
		super();
		speed = SLOW_SPEED; // user input is considered the slowest input
		name = "Keyboard";
	}
}
