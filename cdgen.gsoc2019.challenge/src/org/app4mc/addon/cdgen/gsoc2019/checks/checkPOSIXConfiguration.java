package org.app4mc.addon.cdgen.gsoc2019.checks;

import org.app4mc.addon.cdgen.gsoc2019.*;
import org.app4mc.addon.cdgen.gsoc2019.test.testTaskStructure;
import org.eclipse.app4mc.amalthea.model.Amalthea;
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
		//System.out.println("<a href=\"http://www.google.com\">whatever</a>");
		try {
			Desktop.getDesktop().open(new File(srcPath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.exit(0);

	} 

}
