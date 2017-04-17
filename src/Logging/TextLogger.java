package Logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextLogger {
	private FileWriter writer;
	private boolean writerOpened;
	public int dec = 2;
	
	public void close(){
		if (writerOpened){
			try {
				writer.flush();
				writer.close();
				writerOpened = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File already closed for writing");
		}
	}
	
	// open file for logging
	public void open(String name) {
		if (!writerOpened){
			try {
				File file = new File("./data/"+name+".csv");
				file.createNewFile();
				writer = new FileWriter(file);
				writerOpened = true;
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} else {
			System.out.println("File already opened for writing, or logging is turned off");
		}
		
	}
	
	public void print(String s){
		if (writerOpened){
			try {
				writer.write(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// to console
			System.out.print(s);
		}
	}
	
	public void println(String s){
		if (writerOpened){
			try {
				writer.write(s+"\n");
			//	writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// to console
			System.out.println(s);
		}
	}
}
