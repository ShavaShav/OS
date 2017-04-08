package structures;

import java.util.Comparator;

import kernel.Process;

public interface Schedule {
	public static final int FCFS = 0; // first-come first serve
	public static final int SJF = 1; // shortest job first
	public static final int PRIORITY = 2; // highest priority first
	public static final int SRT = 3; // shortest remaining time
	
	public static int NUM_SCHEDULES = 4;
	
	public static String getName(int schedule){
		switch (schedule){
		case FCFS:
			return "FCFS";
		case SJF:
			return "SJF";
		case SRT:
			return "SRT";
		case PRIORITY:
			return "Priority";
		default:
			return "Unknown";
		}
	}
	
	public static Comparator<Process> getComparator(int schedule){
		switch (schedule){
		case FCFS:
			return null;
		case SJF:
			return new SJFComparator();
		case SRT:
			return new SRTComparator();
		case PRIORITY:
			return new PriorityComparator();
		default:
			return null;
		}
	}
	
	public class PriorityComparator implements Comparator<Process> {
		@Override
		public int compare(Process p1, Process p2) {
			if (p1.isHighPriority() == p2.isHighPriority())
				return 0;
			else {
				if (p1.isHighPriority())
					return -1;
				else
					return 1;
			}
		}
	}
	
	public class SJFComparator implements Comparator<Process> {
		@Override
		public int compare(Process p1, Process p2) {
			return Integer.compare(p1.getTotalTicks(), p2.getTotalTicks());
		}
	}
	
	public class SRTComparator implements Comparator<Process> {
		@Override
		public int compare(Process p1, Process p2) {
			return Integer.compare(p1.getTicksRemaining(), p2.getTicksRemaining());
		}
	}
}
