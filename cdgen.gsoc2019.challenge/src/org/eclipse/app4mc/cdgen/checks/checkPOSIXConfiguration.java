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
import java.awt.Desktop;


/**
 * Implementation of GUI Design and Action on Button Click.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class checkPOSIXConfiguration{
	public checkPOSIXConfiguration (Amalthea model, String srcPath, String headerPath, int configFlag) throws IOException {
		POSIXConfiguration(model, srcPath, headerPath, configFlag);
	}

	public void POSIXConfiguration(Amalthea model, String srcPath, String headerPath, int configFlag) {
		try {
			new MainFileCreation(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new RunFileCreation(model, srcPath, headerPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new LabelFileCreation(model, srcPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new FreeRTOSConfigFileCreation(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new TaskFileCreation(model, srcPath, headerPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new TaskFileCreation(model, srcPath, headerPath, configFlag);
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
