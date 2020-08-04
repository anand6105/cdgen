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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.cdgen.ArmCodeFileCreation;
import org.eclipse.app4mc.cdgen.FreeRTOSConfigFileCreation;
import org.eclipse.app4mc.cdgen.LabelFileCreation;
import org.eclipse.app4mc.cdgen.MainRMSFileCreation;
import org.eclipse.app4mc.cdgen.MakeFileCreation;
import org.eclipse.app4mc.cdgen.ModelEnumFileCreation;
import org.eclipse.app4mc.cdgen.RTFConfigFileCreation;
import org.eclipse.app4mc.cdgen.RunFileCreation;
import org.eclipse.app4mc.cdgen.SharedLabelsFileCreation;
import org.eclipse.app4mc.cdgen.TaskFileCreation;
import org.eclipse.app4mc.cdgen.test.testTaskStructure;
import org.eclipse.app4mc.cdgen.utils.fileUtil;

/**
 * Implementation of GUI Design and Action on Button Click.
 *
 *
 */

public class checkRMSConfiguration {


	public checkRMSConfiguration(final Amalthea model, final String srcPath, final String headerPath,
			final int configFlag) {
		RMSConfiguration(model, srcPath, headerPath, configFlag);
	}

	public void RMSConfiguration(final Amalthea model, final String srcPath, final String headerPath,
			final int configFlag) {
		/*
		 * try { String path = System.getProperty("user.dir"); File
		 * l_SourceDirectory = null; if(0x3110 == (configFlag & 0xFFF0)) {
		 * l_SourceDirectory = new File(path + "/ref/rms_coop/"); }else
		 * if(0x3120 == (configFlag & 0xFFF0)){ l_SourceDirectory = new
		 * File(path + "/ref/freertos_preem/"); } String[] filesName =
		 * l_SourceDirectory.list(); for(String pathi:filesName) { File
		 * SourceFile = new File(l_SourceDirectory.toString() +"/" + pathi);
		 * File DestinationFile = new File(Paths.get(srcPath).toString() + "/" +
		 * pathi ); com.google.common.io.Files.copy(SourceFile , DestinationFile
		 * ); } } catch (IOException e1) { e1.printStackTrace(); }
		 */
		try {
			// String path = System.getProperty("user.dir");
			// create new file
			final File l_SourceDirectory = new File(headerPath);
			// array of files and directory
			final String[] filesName = l_SourceDirectory.list();
			// for each name in the path array
			for (final String pathi : filesName) {
				final File SourceFile = new File(l_SourceDirectory.toString() + "/" + pathi);
				final File DestinationFile = new File(Paths.get(srcPath).toString() + "/" + pathi);
				if (fileUtil.getFileExtension(SourceFile) == "c" || fileUtil.getFileExtension(SourceFile) == "h"
						|| fileUtil.getFileExtension(SourceFile) == "") {
					com.google.common.io.Files.copy(SourceFile, DestinationFile);
				}
			}
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new MainRMSFileCreation(model, srcPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			if (0x0001 == (0x0001 & configFlag)) {
				new RTFConfigFileCreation(model, srcPath, configFlag);
			}

		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			if (0x0001 == (0x0001 & configFlag)) {
				new ModelEnumFileCreation(model, srcPath);
			}

		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new RunFileCreation(model, srcPath, headerPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new TaskFileCreation(model, srcPath, headerPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new FreeRTOSConfigFileCreation(model, srcPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new MakeFileCreation(model, srcPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new SharedLabelsFileCreation(model, srcPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new LabelFileCreation(model, srcPath);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new ArmCodeFileCreation(model, srcPath, headerPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		try {
			new testTaskStructure(model, srcPath, configFlag);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Generation completed, Check path 	" + srcPath);
		// TODO: Set hyperlink for path
		try {
			Desktop.getDesktop().open(new File(srcPath));
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

}
