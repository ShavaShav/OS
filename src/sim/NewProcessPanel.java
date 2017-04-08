package sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import machine.CPU;
import machine.Config;
import machine.RAM;
import structures.IODevice;

import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.InputMap;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class NewProcessPanel extends JPanel {
	private JTextField textFieldSize;
	private JTextField textFieldTicks;
	private JComboBox comboBoxPriority;
	private JCheckBox[] chckbxResources;
	private JButton btnAddProcess;
	/**
	 * Create the panel.
	 */
	public NewProcessPanel() {
		super();
		setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setBackground(new Color(102, 204, 255));
		setOpaque(true);
		GridBagLayout gbl_panelNewProcess = new GridBagLayout();
		gbl_panelNewProcess.columnWidths = new int[] {36, 109, 26};
		gbl_panelNewProcess.rowHeights = new int[]{28, 22, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelNewProcess.columnWeights = new double[]{0.0, 0.0};
		gbl_panelNewProcess.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_panelNewProcess);
		
		JLabel label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(label, gbc_label);
		
		JPanel panelProcessTitle = new JPanel();
		panelProcessTitle.setOpaque(false);
		FlowLayout fl_panelProcessTitle = (FlowLayout) panelProcessTitle.getLayout();
		GridBagConstraints gbc_panelProcessTitle = new GridBagConstraints();
		gbc_panelProcessTitle.insets = new Insets(0, 0, 5, 5);
		gbc_panelProcessTitle.fill = GridBagConstraints.BOTH;
		gbc_panelProcessTitle.gridx = 1;
		gbc_panelProcessTitle.gridy = 0;
		add(panelProcessTitle, gbc_panelProcessTitle);
		
		JLabel lblNewProcess = new JLabel("Process");
		lblNewProcess.setFont(new Font("Verdana", Font.BOLD, 13));
		panelProcessTitle.add(lblNewProcess);
		
		JLabel label_1 = new JLabel("");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 2;
		gbc_label_1.gridy = 0;
		add(label_1, gbc_label_1);
		
		JPanel panelSize = new JPanel();
		panelSize.setOpaque(false);
		GridBagConstraints gbc_panelSize = new GridBagConstraints();
		gbc_panelSize.insets = new Insets(0, 0, 5, 5);
		gbc_panelSize.fill = GridBagConstraints.BOTH;
		gbc_panelSize.gridx = 1;
		gbc_panelSize.gridy = 1;
		add(panelSize, gbc_panelSize);
		panelSize.setLayout(new BorderLayout(0, 0));
		
		JLabel lblSize = new JLabel("Size:");
		panelSize.add(lblSize, BorderLayout.NORTH);
		
		textFieldSize = new JTextField();
		panelSize.add(textFieldSize, BorderLayout.SOUTH);
		textFieldSize.setColumns(10);
		
		JPanel panelPriority = new JPanel();
		panelPriority.setOpaque(false);
		GridBagConstraints gbc_panelPriority = new GridBagConstraints();
		gbc_panelPriority.insets = new Insets(0, 0, 5, 5);
		gbc_panelPriority.fill = GridBagConstraints.BOTH;
		gbc_panelPriority.gridx = 1;
		gbc_panelPriority.gridy = 2;
		add(panelPriority, gbc_panelPriority);
		panelPriority.setLayout(new BorderLayout(0, 0));
		
		JLabel lblPriority = new JLabel("Priority");
		panelPriority.add(lblPriority, BorderLayout.NORTH);
		
		comboBoxPriority = new JComboBox(new String[]{"High Priority", "Low Priority"});
		panelPriority.add(comboBoxPriority, BorderLayout.SOUTH);
		
		JPanel panelResources = new JPanel();
		panelResources.setOpaque(false);
		GridBagConstraints gbc_panelResources = new GridBagConstraints();
		gbc_panelResources.insets = new Insets(0, 0, 5, 5);
		gbc_panelResources.fill = GridBagConstraints.BOTH;
		gbc_panelResources.gridx = 1;
		gbc_panelResources.gridy = 3;
		add(panelResources, gbc_panelResources);
		panelResources.setLayout(new BorderLayout(0, 0));
		
		JLabel lblResources = new JLabel("Resources");
		panelResources.add(lblResources, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panelResources.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		chckbxResources = new JCheckBox[Config.RESOURCES.length];
		for (int i = 0; i < Config.RESOURCES.length; i++){
			chckbxResources[i] = new JCheckBox(Config.RESOURCES[i].getName());
			chckbxResources[i].setOpaque(false);
			panel.add(chckbxResources[i]);
		}
		
		JPanel panelTicks = new JPanel();
		panelTicks.setOpaque(false);
		GridBagConstraints gbc_panelTicks = new GridBagConstraints();
		gbc_panelTicks.insets = new Insets(0, 0, 5, 5);
		gbc_panelTicks.fill = GridBagConstraints.BOTH;
		gbc_panelTicks.gridx = 1;
		gbc_panelTicks.gridy = 4;
		add(panelTicks, gbc_panelTicks);
		panelTicks.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCpuTicks = new JLabel("CPU Ticks");
		panelTicks.add(lblCpuTicks, BorderLayout.NORTH);
		
		textFieldTicks = new JTextField();
		panelTicks.add(textFieldTicks, BorderLayout.SOUTH);
		textFieldTicks.setColumns(10);
		
		JButton btnRandomize = new JButton("Randomize");
		btnRandomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				randomize();
			}
		});
		
		GridBagConstraints gbc_btnRandomize = new GridBagConstraints();
		gbc_btnRandomize.insets = new Insets(0, 0, 5, 5);
		gbc_btnRandomize.gridx = 1;
		gbc_btnRandomize.gridy = 5;
		add(btnRandomize, gbc_btnRandomize);
		
		btnAddProcess = new JButton("Add Process");
		GridBagConstraints gbc_btnAddProcess = new GridBagConstraints();
		gbc_btnAddProcess.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddProcess.gridx = 1;
		gbc_btnAddProcess.gridy = 6;
		add(btnAddProcess, gbc_btnAddProcess);
		
		JLabel label_3 = new JLabel("");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.insets = new Insets(0, 0, 0, 5);
		gbc_label_3.gridx = 1;
		gbc_label_3.gridy = 7;
		add(label_3, gbc_label_3);
		
		randomize(); // randomize the default values
	}
	
	public void setFieldsEditable(boolean bool){
		textFieldSize.setEditable(bool);
		textFieldTicks.setEditable(bool);
		comboBoxPriority.setEditable(bool);
		for (JCheckBox checkBox : chckbxResources)
			checkBox.setEnabled(bool);
//		for (JCheckBox checkBox : chckbxResources){
//			MouseListener[] ml = (MouseListener[])checkBox.getListeners(MouseListener.class);
//
//			for (int i = 0; i < ml.length; i++)
//			    checkBox.removeMouseListener( ml[i] );
//
//			InputMap im = checkBox.getInputMap();
//			im.put(KeyStroke.getKeyStroke("SPACE"), "none");
//			im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
//		}
		btnAddProcess.setEnabled(bool);
	}
	
	public int getProcessSize() { return Integer.parseInt(textFieldSize.getText()); }
	public boolean getHighPriority() { return comboBoxPriority.getSelectedIndex() == 0 ? true : false; }
	public ArrayList<IODevice> getResources() { 
		ArrayList<IODevice> resources = new ArrayList<IODevice>();
		for (int i = 0; i < chckbxResources.length; i++){
			if (chckbxResources[i].isSelected())
				resources.add(Config.RESOURCES[i]);
		}
		return resources;
	}
	public int getTotalTicks() { return Integer.parseInt(textFieldTicks.getText()); }
	
	
	private void randomize(){
		textFieldSize.setText(String.valueOf((int)(Math.random() * (RAM.CAPACITY/10)))); // random size less than 10th of ram
		comboBoxPriority.setSelectedIndex((int) Math.round(Math.random())); // 0 ro 1 
		for (JCheckBox chckbxResource : chckbxResources){
			if (Math.random() > 0.5) // 50/50 shot of resource being needed
				chckbxResource.setSelected(true);
			else
				chckbxResource.setSelected(false);
		}
		textFieldTicks.setText(String.valueOf((int) (CPU.CLOCK_SPEED * (Math.random() * 60)))); // random between 0 and 60 seconds worth of ticks
	}
	
	public void addController(ActionListener controller){
		btnAddProcess.addActionListener(controller);
	}
}
