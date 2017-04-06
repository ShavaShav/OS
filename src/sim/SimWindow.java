package sim;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import kernel.ShortTermScheduler;
import machine.CPU;

import javax.swing.JRadioButton;

public class SimWindow extends JFrame implements Observer {
	private CPU cpu;
	private JPanel contentPane;
	private JRadioButton CPUWorking;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimWindow frame = new SimWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SimWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		CPUWorking = new JRadioButton("CPU");
		contentPane.add(CPUWorking, BorderLayout.CENTER);
		
		ShortTermScheduler sts = new ShortTermScheduler();
		sts.start();
	}

	@Override
	public void update(Observable o, Object arg) {
		CPUWorking.setSelected(!CPUWorking.isSelected());
		
	}

}
