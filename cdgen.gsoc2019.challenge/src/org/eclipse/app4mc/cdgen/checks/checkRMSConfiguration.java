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

public class checkRMSConfiguration{


	public checkRMSConfiguration (Amalthea model, String srcPath, String headerPath, int configFlag) throws IOException {
		RMSConfiguration(model, srcPath, headerPath, configFlag);
	}

	public void RMSConfiguration(Amalthea model, String srcPath, String headerPath, int configFlag) {
		/*try {
			String path = System.getProperty("user.dir");
			File l_SourceDirectory = null;
			if(0x3110 == (configFlag & 0xFFF0)) {
				l_SourceDirectory = new File(path + "/ref/rms_coop/");
			}else if(0x3120 == (configFlag & 0xFFF0)){
				l_SourceDirectory = new File(path + "/ref/freertos_preem/");
			}
			String[] filesName = l_SourceDirectory.list();
			for(String pathi:filesName) {
				File SourceFile = new File(l_SourceDirectory.toString() +"/" + pathi);
				File DestinationFile = new File(Paths.get(srcPath).toString() + "/" + pathi );
				com.google.common.io.Files.copy(SourceFile , DestinationFile );
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
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
			new FreeRTOSConfigFileCreation(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new MakeFileCreation(model, srcPath, configFlag);
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
			new ArmCodeFileCreation(model, srcPath, headerPath, configFlag);
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
		try {
			Desktop.getDesktop().open(new File(srcPath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

}
