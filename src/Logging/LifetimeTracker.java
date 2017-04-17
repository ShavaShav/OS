package Logging;

public class LifetimeTracker extends CSVLogger{
	private static final LifetimeTracker instance = new LifetimeTracker();
	private LifetimeTracker(){
		super();
		super.openCSV("Process LifeTime");
	};	
	public static LifetimeTracker getInstance(){ return instance; }
	
	public long startTime, endTime;
}
