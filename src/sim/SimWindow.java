package sim;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.border.BevelBorder;


public class SimWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelSettings;

	/**
	 * Create the frame.
	 */
	public SimWindow() {
		setBackground(new Color(255, 255, 255));
		setTitle("Process Synchronization Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1648, 900);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 204, 153));
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.setLayout(new BorderLayout(5, 5));
		setContentPane(contentPane);
		
		JPanel panelTitle = new JPanel();
		panelTitle.setBackground(new Color(204, 204, 153));
		panelTitle.setOpaque(true);
		contentPane.add(panelTitle, BorderLayout.NORTH);
		
		JLabel lblProcessSynchronizationSimulation = new JLabel("Process Synchronization Simulation");
		lblProcessSynchronizationSimulation.setForeground(new Color(0, 0, 0));
		lblProcessSynchronizationSimulation.setBackground(new Color(51, 51, 51));
		
		lblProcessSynchronizationSimulation.setFont(new Font("Verdana", Font.BOLD, 30));
		panelTitle.add(lblProcessSynchronizationSimulation);
		
		panelSettings = new JPanel();
		panelSettings.setBackground(new Color(204, 204, 153));
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
}
