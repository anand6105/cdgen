package org.app4mc.addon.cdgen.gsoc2019.checks;

import org.app4mc.addon.cdgen.gsoc2019.*;

import org.app4mc.addon.cdgen.gsoc2019.test.testTaskStructure;
import org.eclipse.app4mc.amalthea.model.Amalthea;
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
		try {
			String path = System.getProperty("user.dir");
			// create new file
			File l_SourceDirectory = null;
			if(0x3110 == (configFlag & 0xFFF0)) {
				l_SourceDirectory = new File(path + "/ref/rms_coop/");
			}else if(0x3120 == (configFlag & 0xFFF0)){
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
			new LabelFileCreation(model, srcPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/*try {
			new FreeRTOSConfigFileCreation(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new FreeRTOSConfigFileCreation(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		try {
			new TaskFileCreation(model, srcPath, headerPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			new ArmCodeFileCreation(model, srcPath, headerPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/*try {
			new testTaskStructure(model, srcPath, configFlag);
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
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
