package org.app4mc.cdgen.tool.java.checks;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.app4mc.cdgen.tool.java.LabelFileCreation;
import org.app4mc.cdgen.tool.java.MainFileCreation;
import org.app4mc.cdgen.tool.java.RunFileCreation;
import org.app4mc.cdgen.tool.java.TaskFileCreation;
import org.app4mc.cdgen.tool.java.identifiers.Constants;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.PuType;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;

public class checkFileCreation {
	public static void main(final String[] args) throws IOException {
		// Load File
		final File inputFile = new File(Constants.democar);
		//final File inputFile = new File(Constants.WATERS2019_TEST);
		//final File inputFile = new File(Constants.WATERS2019_ANA);
		//final File inputFile = new File(Constants.WATERS2019_ANAMOD);
		final Amalthea model = AmaltheaLoader.loadFromFile(inputFile);
		if (model == null) {
			System.out.println("Error: No model loaded!");
			return;
		}
		// String path1 = System.getProperty("user.dir");
		// System.out.println(path1);
		String timestamp = new Timestamp(System.currentTimeMillis()).toString();
		timestamp = timestamp.substring(0, timestamp.length()-6).replaceAll(":","");
		timestamp = timestamp.replaceAll("-","_");
		timestamp = timestamp.replaceAll(" ","_");
		String path1 = "C:\\workspace\\01_ESM\\GSoC\\gsoc_output";
		File theDir = new File(timestamp);
		 if (!theDir.exists()) {
		     System.out.println("creating directory: " + theDir.getName());
		     boolean result = false;

		     try{
		         theDir.mkdir();
		         result = true;
		     } 
		     catch(SecurityException se){
		         //handle it
		     }        
		     if(result) {    
		         System.out.println("DIR created");  
		     }
		 }
		 path1 = path1+"/"+timestamp;
		 MainFileCreation main = new MainFileCreation(model,path1);
		 RunFileCreation run = new RunFileCreation(model, path1);
		 TaskFileCreation task = new TaskFileCreation(model, path1);
		 LabelFileCreation lab = new LabelFileCreation(model, path1);
					
	}
}
