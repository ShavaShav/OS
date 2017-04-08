package sim;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import kernel.CPUScheduler;
import java.awt.event.ActionEvent;

public class SimWindow extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CPUScheduler schedulerModel;
	private JPanel panelSettings;

	/**
	 * Create the frame.
	 */
	public SimWindow(CPUScheduler schedulerModel) {
		setBackground(new Color(255, 255, 255));
		setTitle("Process Synchronization Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1152, 751);
		
		this.schedulerModel = schedulerModel;	
		
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.setLayout(new BorderLayout(5, 5));
		setContentPane(contentPane);
		
		JPanel panelTitle = new JPanel();
		contentPane.add(panelTitle, BorderLayout.NORTH);
		
		JLabel lblProcessSynchronizationSimulation = new JLabel("Process Synchronization Simulation");
		lblProcessSynchronizationSimulation.setFont(new Font("Verdana", Font.BOLD, 20));
		panelTitle.add(lblProcessSynchronizationSimulation);
		
		panelSettings = new JPanel();
		contentPane.add(panelSettings, BorderLayout.WEST);
		GridBagLayout gbl_panelSettings = new GridBagLayout();
		gbl_panelSettings.columnWidths = new int[]{95, 0};
		gbl_panelSettings.rowHeights = new int[]{243, 14, 0, 0};
		gbl_panelSettings.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelSettings.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelSettings.setLayout(gbl_panelSettings);
	}
	
	public void addNewProcessPanel(NewProcessPanel npPanel){
		GridBagConstraints gbc_panelNewProcess = new GridBagConstraints();
		gbc_panelNewProcess.insets = new Insets(0, 0, 5, 0);
		gbc_panelNewProcess.anchor = GridBagConstraints.NORTHWEST;
		gbc_panelNewProcess.gridx = 0;
		gbc_panelNewProcess.gridy = 0;
		panelSettings.add(npPanel, gbc_panelNewProcess);
	}
	
	public void addSimulationConfigPanel(SimConfigPanel nsPanel){
		GridBagConstraints gbc_panelNewSimulation = new GridBagConstraints();
		gbc_panelNewSimulation.insets = new Insets(0, 0, 5, 0);
		gbc_panelNewSimulation.fill = GridBagConstraints.BOTH;
		gbc_panelNewSimulation.gridx = 0;
		gbc_panelNewSimulation.gridy = 1;
		panelSettings.add(nsPanel, gbc_panelNewSimulation);
	}
	
	public void addSimulationPanel(SimPanel simPanel){
		contentPane.add(simPanel, BorderLayout.CENTER);
	}

	// called by the CPU Scheduler model
	@Override
	public void update(Observable obs, Object o) {
		System.out.println ("View      : Observable is " + obs.getClass() + ", object passed is " + o.getClass());
	}

}
