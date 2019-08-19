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

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.cdgen.*;
import org.eclipse.app4mc.cdgen.test.testTaskStructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.awt.Desktop;


/**
 * Implementation of GUI Design and Action on Button Click.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class checkFreeRTOSConfiguration{
	public checkFreeRTOSConfiguration(Amalthea model, String srcPath, String headerPath, int configFlag) {
		FreeRTOSConfiguration(model, srcPath, headerPath, configFlag);
	}

	public void FreeRTOSConfiguration(Amalthea model, String srcPath, String headerPath, int configFlag) {
		try {
			String path = System.getProperty("user.dir");
			// create new file
			File l_SourceDirectory = null;
			if(0x1310 == (configFlag & 0xFFF0)) {
				l_SourceDirectory = new File(path + "/ref/freertos_coop/");
			}else if(0x1320 == (configFlag & 0xFFF0)){
				l_SourceDirectory = new File(path + "/ref/freertos_preem/");
			}
			
			// array of files and directory
			String[] filesName = l_SourceDirectory.list();
			// for each name in the path array
			for(String pathi:filesName) {
				File SourceFile = new File(l_SourceDirectory.toString() +"/" + pathi);
				File DestinationFile = new File(Paths.get(srcPath).toString() + "/" + pathi );
				com.google.common.io.Files.copy(SourceFile , DestinationFile );
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new MainRMSFileCreation(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new RunFileCreation(model, srcPath, headerPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new TaskFileCreation(model, srcPath, headerPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new SharedLabelsFileCreation(model, srcPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new LabelFileCreation(model, srcPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new testTaskStructure(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Generation completed, Check path 	" + srcPath);
		//TODO: Set hyperlink for path
		//System.out.println("<a href=\"http://www.google.com\">whatever</a>");
		try {
			Desktop.getDesktop().open(new File(srcPath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

}
