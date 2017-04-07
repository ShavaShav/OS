package sim;

import java.util.Scanner;

import kernel.Process;
import machine.Config;
import machine.HardDiskDrive;
import machine.Keyboard;
import structures.IODevice;

public class ResourceSim {
	
	public static void main(String []args){
		HardDiskDrive hdd = HardDiskDrive.getInstance();
		Keyboard stdin = Keyboard.getInstance();
		IODevice usb = new IODevice("USB Drive", IODevice.MEDIUM_SPEED);
		
		
		new Thread(hdd).start();
		new Thread(stdin).start();
		new Thread(usb).start();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Press key to insert new process into 0) HDD, 1) Keyboard, 2) USB:");
		while (true){
			int option = sc.nextInt();
			Process p = new Process(
					(int) (Math.random() * 100),    // KB size 
					false,							// High Priority?
					(int) (Math.random() * 550),    // Average CPU Burst ticks (before interrupt)
					(int)(Math.random() * 100000),  // Estimated total CPU ticks to complete process
					Config.getRandomResources()		// Resources that process needs over lifetime
				);
			System.out.println("Adding " + p);
			try {
				switch (option){
				case 0:
					hdd.addToQueue(p);
					break;
				case 1:
					stdin.addToQueue(p);
					break;
				case 2:
					usb.addToQueue(p);
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
