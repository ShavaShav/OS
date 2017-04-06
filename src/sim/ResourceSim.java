package sim;

import java.util.Scanner;

import kernel.Process;
import machine.DiskDrive;
import machine.Keyboard;
import machine.Monitor;

public class ResourceSim {
	
	public static void main(String []args){
		DiskDrive hdd = DiskDrive.getInstance();
		Keyboard stdin = Keyboard.getInstance();
		Monitor stdout = Monitor.getInstance();
		
		new Thread(hdd).start();
		new Thread(stdin).start();
		new Thread(stdout).start();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Press key to insert new process into 1) HDD, 2) Keyboard, 3) Monitor:");
		while (true){
			int option = sc.nextInt();
			Process p = new Process((int) (Math.random() * 100), false, (int) (Math.random() * 550), (int)(Math.random() * 100000));
			System.out.println("Adding " + p);
			try {
				switch (option){
				case 1:
					hdd.addToQueue(p);
					break;
				case 2:
					stdin.addToQueue(p);
					break;
				case 3:
					stdout.addToQueue(p);
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
