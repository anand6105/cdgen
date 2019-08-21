/*******************************************************************************
 *   Copyright (c) 2019 Dortmund University of Applied Sciences and Arts and others.
 *   
 *   This program and the accompanying materials are made
 *   available under the terms of the Eclipse Public License 2.0
 *   which is available at https://www.eclipse.org/legal/epl-2.0/
 *   
 *   SPDX-License-Identifier: EPL-2.0
 *   
 *   Contributors:
 *       Dortmund University of Applied Sciences and Arts - initial API and implementation
 *******************************************************************************/
package org.eclipse.app4mc.cdgen.checks;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;
import org.eclipse.app4mc.cdgen.identifiers.Constants;

/**
 * Implementation of GUI Design and Action on Button Click.
 */

public class checkFileCreateGUI {
	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				checkFileCreateGUI window = null;
				try {
					window = new checkFileCreateGUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
				window.frame.setVisible(true);
				window.frame.setTitle("CDGen");

			}
		});

	}

	public checkFileCreateGUI() throws IOException {
		initialize();
	}
	int modelIndex;
	String fileInput;
	File inputFile;
	JTextField txtField = new JTextField("FreeRTOS / RMS / POSIX Browse path");
	private void initialize() throws IOException {

		frame = new JFrame();
		frame.setBounds(100, 100, 864, 272);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JRadioButton cdgenFreeRTOS = new JRadioButton("FreeRTOS");
		cdgenFreeRTOS.setBounds(40, 80, 145, 25);
		frame.getContentPane().add(cdgenFreeRTOS);

		JRadioButton cdgenPosix = new JRadioButton("POSIX");
		cdgenPosix.setBounds(40, 110, 145, 25);
		frame.getContentPane().add(cdgenPosix);

		JRadioButton cdgenCustom = new JRadioButton("RMS(Rate Monotonic)");
		cdgenCustom.setBounds(40, 140, 210, 25);
		frame.getContentPane().add(cdgenCustom);

		ButtonGroup group = new ButtonGroup();
		group.add(cdgenFreeRTOS);
		group.add(cdgenPosix);
		group.add(cdgenCustom);

		JRadioButton cdgenCooperative = new JRadioButton("Cooperative");
		cdgenCooperative.setBounds(190, 80, 145, 25);
		frame.getContentPane().add(cdgenCooperative);

		JRadioButton cdgenPreemptive = new JRadioButton("Preemptive");
		cdgenPreemptive.setBounds(190, 110, 145, 25);
		frame.getContentPane().add(cdgenPreemptive);

		ButtonGroup group3 = new ButtonGroup();
		group3.add(cdgenCooperative);
		group3.add(cdgenPreemptive);
		JComboBox<String> comboModel = new JComboBox<String>();
		comboModel.addItem("DemoCarMulti Parallella");
		comboModel.addItem("DemoCarMulti Raspberry Pi");
		comboModel.setBounds(370, 140, 200, 25);
		frame.getContentPane().add(comboModel);

		comboModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				@SuppressWarnings("unchecked")
				JComboBox<String> combo = (JComboBox<String>) event.getSource();
				String selectedBook = (String) combo.getSelectedItem();
				if (selectedBook.equals("DemoCarMulti Parallella")) {
					modelIndex = 0;
				} else if (selectedBook.equals("DemoCarMulti Raspberry Pi")) {
					modelIndex = 1;
				}
			}
		});

		
		BufferedImage browseButtonIcon = ImageIO.read(new File("/home/rprasathg/Downloads/browse.png"));
		JButton btnBrowse = new JButton(new ImageIcon(browseButtonIcon));
		final JFileChooser fc = new JFileChooser();
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnBrowse)
				{
					int returnVal = fc.showOpenDialog(frame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getCurrentDirectory();
						fileInput = file.getPath();
						txtField.setText(fileInput);
					}
				}
			}
		});
		btnBrowse.setBounds(673, 75, 120, 35);
		frame.getContentPane().add(btnBrowse);

		//JButton btnHelp = new JButton("Help");
		BufferedImage helpButtonIcon = ImageIO.read(new File("/home/rprasathg/Downloads/Untitled Diagram1.png"));
		//	button = new JButton(new ImageIcon(buttonIcon));
		JButton btnHelp = new JButton(new ImageIcon(helpButtonIcon));
		/*btnHelp.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/home/rprasathg/Pictures/helpButton.png"))
				.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH)));*/
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("https://cdgendoc.readthedocs.io/en/latest/GUI_Layout.html").toURI());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnHelp.setBounds(800, 10, 30, 30);
		frame.getContentPane().add(btnHelp);

		txtField.setBounds(370, 80, 280, 25);
		frame.getContentPane().add(txtField);

		if(modelIndex == 0) {
			inputFile = new File(Constants.DEMOCARMULTI);
		}else if(modelIndex == 1) {
			inputFile = new File(Constants.DEMOCARMULTIRASPBERRYPI);
		}
		final Amalthea model = AmaltheaLoader.loadFromFile(inputFile);
		BufferedImage startButtonIcon = ImageIO.read(new File("/home/rprasathg/Downloads/start.png"));
		JButton btnSelectTasks = new JButton(new ImageIcon(startButtonIcon));
		btnSelectTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model == null) {
					System.out.println("Error: No model loaded!");
					return;
				}
				String path = System.getProperty("user.dir");
				String timestamp = new Timestamp(System.currentTimeMillis()).toString();
				timestamp = timestamp.substring(0, timestamp.length() - 6).replaceAll(":", "");
				timestamp = timestamp.replaceAll("-", "_");
				timestamp = timestamp.replaceAll(" ", "_");
				File theDir = new File(timestamp);
				if (!theDir.exists()) {
					System.out.println("creating directory: " + theDir.getName());
					boolean result = false;
					try {
						theDir.mkdir();
						result = true;
					} catch (SecurityException se) {
					}
					if (result) {
						System.out.println("DIR created");
					}
				}
				String path1 = path + "/" + timestamp;
				//System.out.println("\n"+fileInput);
				String path2 = txtField.getText();
				int configFlag = 0xFFFF;
				/*
				 * 
				 * 0X1000 ==> FreeRTOS
				 * 0X2000 ==> POSIX
				 * 0x3000 ==> RMS
				 * 
				 * 0X0010 ==> Cooperative
				 * 0X0020 ==> Preemptive
				 * 
				 * 0X0001 ==> MultiCore
				 * 0X0002 ==> SingleCore
				 * 
				 * FreeRTOS == 	Cooperative ==	0x1X10
				 * FreeRTOS == 	Preemptive 	==	0x1X20
				 * POSIX  	== 	Cooperative ==	0x2X10
				 * POSIX 	== 	Preemptive 	==	0x2X20
				 * RMS  	== 	Cooperative ==	0x3X10
				 * RMS  	== 	Preemptive 	==	0x3X20
				 * 
				 */

				System.out.println("############################################################");
				if (cdgenFreeRTOS.isSelected() & cdgenPreemptive.isSelected() ) {
					configFlag = 0x1320;
					System.out.println("\t\tFreeRTOS\tPreemptive");
					System.out.println("############################################################");
					new checkFreeRTOSConfiguration(model, path1, path2, configFlag);
				}else if (cdgenFreeRTOS.isSelected() & cdgenCooperative.isSelected()) {
					configFlag = 0x1310;
					System.out.println("\t\tFreeRTOS\tCooperative");
					System.out.println("############################################################");
					new checkFreeRTOSConfiguration(model, path1, path2, configFlag);
				}else if (cdgenPosix.isSelected()  & cdgenPreemptive.isSelected() ) {
					configFlag = 0x2320;
					System.out.println("\t\tPosix\tPreemptive");
					System.out.println("############################################################");
					try {
						new checkPOSIXConfiguration(model, path1, path2, configFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else if (cdgenPosix.isSelected() & cdgenCooperative.isSelected()) {
					configFlag = 0x2310;
					System.out.println("\t\tPosix\tCooperative");
					System.out.println("############################################################");
					try {
						new checkPOSIXConfiguration(model, path1, path2, configFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else if (cdgenCustom.isSelected() & cdgenPreemptive.isSelected() ) {
					configFlag = 0x3120;
					System.out.println("\t\tRMS\tPreemptive");
					System.out.println("############################################################");
					try {
						new checkRMSConfiguration(model, path1, path2, configFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else if (cdgenCustom.isSelected() & cdgenCooperative.isSelected()) {
					configFlag = 0x3110;
					System.out.println("\t\tRMS\tCooperative");
					System.out.println("############################################################");
					try {
						new checkRMSConfiguration(model, path1, path2, configFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					System.out.println("Configuration Not Defined!");
				}
			}
		});
		btnSelectTasks.setBounds(673, 120, 120, 35);
		frame.getContentPane().add(btnSelectTasks);
		BufferedImage closeButtonIcon = ImageIO.read(new File("/home/rprasathg/Downloads/close.png"));
		JButton btnClose = new JButton(new ImageIcon(closeButtonIcon));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setBounds(673, 165, 120, 35);
		frame.getContentPane().add(btnClose);
		JLabel lblAllTasks = new JLabel("OS Options");
		lblAllTasks.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAllTasks.setBounds(40, 55, 110, 16);
		frame.getContentPane().add(lblAllTasks);

		JLabel lblModelSelection = new JLabel("Model selection");
		lblModelSelection.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblModelSelection.setBounds(370, 110, 145, 25);
		frame.getContentPane().add(lblModelSelection);

		JLabel lblSourcePath = new JLabel("Source Path");
		lblSourcePath.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSourcePath.setBounds(370, 50, 145, 25);
		frame.getContentPane().add(lblSourcePath);

		JLabel lblResponsetime = new JLabel("Task Preemption");
		lblResponsetime.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblResponsetime.setBounds(190, 55, 150, 16);
		frame.getContentPane().add(lblResponsetime);

		JLabel lblPerformanceMetric = new JLabel("CDGen - Code Generator for APP4MC");
		lblPerformanceMetric.setForeground(Color.ORANGE);
		lblPerformanceMetric.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblPerformanceMetric.setBounds(27, 13, 450, 30);
		frame.getContentPane().add(lblPerformanceMetric);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 42, 822, 272);
		frame.getContentPane().add(separator);
	}
}
