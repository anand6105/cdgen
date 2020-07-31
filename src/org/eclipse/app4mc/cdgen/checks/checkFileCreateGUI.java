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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;

/**
 * Implementation of GUI Design and Action on Button Click.
 */

public class checkFileCreateGUI {
	JFrame frame;

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("null")
			@Override
			public void run() {
				checkFileCreateGUI window = null;
				try {
					window = new checkFileCreateGUI();
				}
				catch (final IOException e) {
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
	String fileInput1;
	JTextField txtFieldScheduler = new JTextField("FreeRTOS / RMS / POSIX Browse path");
	JTextField txtFieldModel = new JTextField("Amalthea Model Browse path");

	private void initialize() throws IOException {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 864, 272);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);

		final JRadioButton cdgenFreeRTOS = new JRadioButton("FreeRTOS");
		cdgenFreeRTOS.setBounds(40, 80, 145, 25);
		this.frame.getContentPane().add(cdgenFreeRTOS);

		final JRadioButton cdgenPosix = new JRadioButton("POSIX");
		cdgenPosix.setBounds(40, 110, 145, 25);
		this.frame.getContentPane().add(cdgenPosix);

		final JRadioButton cdgenCustom = new JRadioButton("RMS(Rate Monotonic)");
		cdgenCustom.setBounds(40, 140, 210, 25);
		this.frame.getContentPane().add(cdgenCustom);

		final ButtonGroup group = new ButtonGroup();
		group.add(cdgenFreeRTOS);
		group.add(cdgenPosix);
		group.add(cdgenCustom);

		final JRadioButton cdgenCooperative = new JRadioButton("Cooperative");
		cdgenCooperative.setBounds(190, 80, 145, 25);
		this.frame.getContentPane().add(cdgenCooperative);

		final JRadioButton cdgenPreemptive = new JRadioButton("Preemptive");
		cdgenPreemptive.setBounds(190, 110, 145, 25);
		this.frame.getContentPane().add(cdgenPreemptive);

		final ButtonGroup group3 = new ButtonGroup();
		group3.add(cdgenCooperative);
		group3.add(cdgenPreemptive);
		final BufferedImage browseButtonIconScheduler = ImageIO.read(new File("./cdgen.graphics/browse.png"));
		final JButton btnBrowseScheduler = new JButton(new ImageIcon(browseButtonIconScheduler));
		final JFileChooser fc = new JFileChooser();
		btnBrowseScheduler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (e.getSource() == btnBrowseScheduler) {
					final int returnVal = fc.showOpenDialog(checkFileCreateGUI.this.frame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final File file = fc.getCurrentDirectory();
						checkFileCreateGUI.this.fileInput = file.getPath();
						checkFileCreateGUI.this.txtFieldScheduler.setText(checkFileCreateGUI.this.fileInput);
					}
				}
			}
		});
		btnBrowseScheduler.setBounds(673, 75, 120, 35);
		this.frame.getContentPane().add(btnBrowseScheduler);

		final BufferedImage browseButtonIconModel = ImageIO.read(new File("./cdgen.graphics/browse.png"));
		final JButton btnBrowseModel = new JButton(new ImageIcon(browseButtonIconModel));
		final JFileChooser fc1 = new JFileChooser();
		// final String fileInput1;
		btnBrowseModel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {

				if (e.getSource() == btnBrowseModel) {
					final FileNameExtensionFilter filter = new FileNameExtensionFilter("*.amxmi", "*.amxmi");
					fc1.addChoosableFileFilter(filter);
					fc1.setMultiSelectionEnabled(false);
					// fc1.showOpenDialog(null);
					final int returnVal = fc1.showOpenDialog(checkFileCreateGUI.this.frame);
					// System.out.println("\nRam " +
					// fileUtil.getFileExtension(fc1.getSelectedFile()));
					// if(fileUtil.getFileExtension(fc1.getSelectedFile())=="amxmi"){
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final File file = fc1.getSelectedFile();
						checkFileCreateGUI.this.fileInput1 = file.getPath();
						checkFileCreateGUI.this.txtFieldModel.setText(checkFileCreateGUI.this.fileInput1);
					}
					/*
					 * }else { JOptionPane.showMessageDialog(null,
					 * " choose a file with \".amxmi\" file ","Wrong model file"
					 * ,2); }
					 */
				}
			}
		});
		btnBrowseModel.setBounds(673, 130, 120, 35);
		this.frame.getContentPane().add(btnBrowseModel);
		final BufferedImage helpButtonIcon = ImageIO.read(new File("./cdgen.graphics/help.png"));
		final JButton btnHelp = new JButton(new ImageIcon(helpButtonIcon));
		btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					Desktop.getDesktop()
							.browse(new URL("https://cdgendoc.readthedocs.io/en/latest/GUI_Layout.html").toURI());
				}
				catch (final Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnHelp.setBounds(800, 10, 30, 30);
		this.frame.getContentPane().add(btnHelp);

		this.txtFieldScheduler.setBounds(370, 80, 280, 25);
		this.frame.getContentPane().add(this.txtFieldScheduler);


		this.txtFieldModel.setBounds(370, 140, 280, 25);
		this.frame.getContentPane().add(this.txtFieldModel);
		
		final JCheckBox cbBTFTrace = new JCheckBox("Enable BTF");
		cbBTFTrace.setFont(new Font("Tahoma", Font.BOLD, 13));
		cbBTFTrace.setBounds(670, 190, 120, 35);
		this.frame.getContentPane().add(cbBTFTrace);


		final BufferedImage startButtonIcon = ImageIO.read(new File("./cdgen.graphics/start.png"));
		final JButton btnSelectTasks = new JButton(new ImageIcon(startButtonIcon));
		btnSelectTasks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// File inputFile = new File();
				// String inputFile = Constants.DEMOCARMULTI;
				final Amalthea model = AmaltheaLoader
						.loadFromFileNamed(checkFileCreateGUI.this.txtFieldModel.getText());
				System.out.println("\n Model file " + checkFileCreateGUI.this.txtFieldModel.getText());
				if (model == null) {
					System.out.println("Error: No model loaded!");
					return;
				}
				final String path = System.getProperty("user.dir");
				String timestamp = new Timestamp(System.currentTimeMillis()).toString();
				timestamp = timestamp.substring(0, timestamp.length() - 6).replaceAll(":", "");
				timestamp = timestamp.replaceAll("-", "_");
				timestamp = timestamp.replaceAll(" ", "_");
				final File theDir = new File(timestamp);
				if (!theDir.exists()) {
					System.out.println("creating directory: " + theDir.getName());
					boolean result = false;
					try {
						theDir.mkdir();
						result = true;
					}
					catch (final SecurityException se) {
						System.exit(0);
					}
					if (result) {
						System.out.println("DIR created");
					}
				}
				final String path1 = path + "/" + timestamp;
				final String path2 = checkFileCreateGUI.this.txtFieldScheduler.getText();
				int configFlag = 0xFFFF;
				/*
				 *
				 * 0X1000 ==> FreeRTOS 0X2000 ==> POSIX 0x3000 ==> RMS
				 *
				 * 0X0010 ==> Cooperative 0X0020 ==> Preemptive
				 * 0xXXX1 ==> BTF Trace Enabled
				 *
				 * FreeRTOS == Cooperative == 0x1X10 FreeRTOS == Preemptive ==
				 * 0x1X20 POSIX == Cooperative == 0x2X10 POSIX == Preemptive ==
				 * 0x3X20 RMS == Cooperative == 0x3X10 RMS == Preemptive ==
				 * 0x3X20 X == Don't care
				 */

				System.out.println("############################################################");
				if (cdgenFreeRTOS.isSelected() & cdgenPreemptive.isSelected()) {
					configFlag = 0x1320;
					System.out.println("\t\tFreeRTOS\tPreemptive");
					System.out.println("############################################################");
					new checkFreeRTOSConfiguration(model, path1, path2, configFlag);
				}
				else if (cdgenFreeRTOS.isSelected() & cdgenCooperative.isSelected()) {
					configFlag = 0x1310;
					System.out.println("\t\tFreeRTOS\tCooperative");
					System.out.println("############################################################");
					new checkFreeRTOSConfiguration(model, path1, path2, configFlag);
				}
				else if (cdgenPosix.isSelected() & cdgenPreemptive.isSelected()) {
					configFlag = 0x2320;
					System.out.println("\t\tPosix\tPreemptive");
					System.out.println("############################################################");
					new checkPOSIXConfiguration(model, path1, path2, configFlag);
				}
				else if (cdgenPosix.isSelected() & cdgenCooperative.isSelected()) {
					configFlag = 0x2310;
					System.out.println("\t\tPosix\tCooperative");
					System.out.println("############################################################");
					new checkPOSIXConfiguration(model, path1, path2, configFlag);
				}
				else if (cdgenCustom.isSelected() & cdgenPreemptive.isSelected()) {
					if (cbBTFTrace.isSelected() == true) {
						configFlag = 0x3121;
						System.out.println("\t\tRMS\tPreemptive with BTF Trace");
					}
					else {
						configFlag = 0x3120;
						System.out.println("\t\tRMS\tPreemptive");
					}
					System.out.println("############################################################");
					new checkRMSConfiguration(model, path1, path2, configFlag);
				}
				else if (cdgenCustom.isSelected() & cdgenCooperative.isSelected()) {
					if (cbBTFTrace.isSelected() == true) {
						configFlag = 0x3111;
						System.out.println("\t\tRMS\tCooperative with BTF Trace");
					}
					else {
						configFlag = 0x3110;
						System.out.println("\t\tRMS\tCooperative");
					}
					System.out.println("############################################################");
					new checkRMSConfiguration(model, path1, path2, configFlag);
				}
				else {
					System.out.println("Configuration Not Defined!");
				}
			}
		});
		btnSelectTasks.setBounds(190, 190, 120, 35);
		this.frame.getContentPane().add(btnSelectTasks);
		final BufferedImage closeButtonIcon = ImageIO.read(new File("./cdgen.graphics/close.png"));
		final JButton btnClose = new JButton(new ImageIcon(closeButtonIcon));
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setBounds(370, 190, 120, 35);
		this.frame.getContentPane().add(btnClose);
		final JLabel lblAllTasks = new JLabel("OS Options");
		lblAllTasks.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAllTasks.setBounds(40, 55, 110, 16);
		this.frame.getContentPane().add(lblAllTasks);

		final JLabel lblModelSelection = new JLabel("Model selection");
		lblModelSelection.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblModelSelection.setBounds(370, 110, 145, 25);
		this.frame.getContentPane().add(lblModelSelection);

		final JLabel lblSourcePath = new JLabel("Source Path");
		lblSourcePath.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSourcePath.setBounds(370, 50, 145, 25);
		this.frame.getContentPane().add(lblSourcePath);

		final JLabel lblResponsetime = new JLabel("Task Preemption");
		lblResponsetime.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblResponsetime.setBounds(190, 55, 150, 16);
		this.frame.getContentPane().add(lblResponsetime);

		final JLabel lblPerformanceMetric = new JLabel("CDGen - Code Generator for APP4MC");
		lblPerformanceMetric.setForeground(Color.ORANGE);
		lblPerformanceMetric.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblPerformanceMetric.setBounds(27, 13, 450, 30);
		this.frame.getContentPane().add(lblPerformanceMetric);

		final JSeparator separator = new JSeparator();
		separator.setBounds(12, 42, 822, 272);
		this.frame.getContentPane().add(separator);
	}
}
