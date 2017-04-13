package sim;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;

import kernel.CPUScheduler;
import kernel.Process;

public class StartSimulator {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CPUScheduler model = new CPUScheduler(0, new ArrayList<Process>());
					SimWindow view = new SimWindow();
					new SimController(view, model);
					view.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
					view.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
