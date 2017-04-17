package Logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

public class CSVLogger {
	public boolean ON = true;
	private CSVWriter csvWriter;
	private boolean writerOpened;
	public final char COMMA = ',';
	
	private ArrayList<String> entries = new ArrayList<String>();
	
	public void logRecord(Object... objects){
		if (writerOpened){
			for (Object o : objects){
				entries.add(String.valueOf(o));
			}
			endRecord();			
		}
	}
	
	// csv entry
	public void logEntry(String entry){
		if (writerOpened){
			entries.add(entry);
		}
	}
	
	// csv entry
	public void logEntry(double entry){
		if (writerOpened){
			entries.add(String.valueOf(entry));
		}
	}
	
	// csv entry
	public void logEntry(int entry){
		if (writerOpened){
			entries.add(String.valueOf(entry));	
		}
	}
	
	// line of csv
	public void endRecord(){
		if (writerOpened){
			csvWriter.writeNext(entries.toArray(new String[entries.size()]));
			entries.clear();
		}
	}
	
	public void closeCSV(){
		if (writerOpened){
			try {
				csvWriter.flush();
				csvWriter.close();
				writerOpened = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File already closed for writing");
		}
	}
	
	// open file for logging
	public void openCSV(String name) {
		if (!writerOpened){
			try {
				File file = new File("./data/"+name+".csv");
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				csvWriter = new CSVWriter(writer, COMMA);
				writerOpened = true;
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} else {
			System.out.println("File already opened for writing, or logging is turned off");
		}
		
	}
}
