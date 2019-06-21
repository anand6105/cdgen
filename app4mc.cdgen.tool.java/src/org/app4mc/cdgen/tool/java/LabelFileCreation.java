package org.app4mc.cdgen.tool.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.cdgen.tool.java.utils.*;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Event;
import org.eclipse.app4mc.amalthea.model.EventChain;
import org.eclipse.app4mc.amalthea.model.EventChainItem;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessEvent;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.emf.common.util.EList;

//import org.eclipse.app4mc.amalthea.model.TimeUnit;
/**
 * Implementation to evaluate latencies for End to End Event Chains/Effect Chains.
 * 
 * @author RockNRollMan
 *
 */
public class LabelFileCreation {
	final private Amalthea model;
	
	/**
	 * Constructor E2E - Debugging purpose
	 *
	 * @param Model Amalthea Model
	 * @param path1 
	 * @throws IOException 
	 */
	public LabelFileCreation(final Amalthea Model, String path1) throws IOException {
		this.model = Model;
		final EList<EventChain> eventChains = this.model.getConstraintsModel().getEventChains();
		System.out.println("Label File Creation Begins");
		fileCreate(model,path1);
		System.out.println("Label File Creation Ends");
	}

	public static void fileCreate(Amalthea model, String path1) throws IOException {
		EList<Label> labellist = model.getSwModel().getLabels();
	String fname= path1+File.separator+"labelDeclaration.h";
		    File f2 = new File(path1);
		    File f1 = new File(fname);	    
		    f2.mkdirs() ;
		    try {
		        f1.createNewFile();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		    
		    File fn= f1;
        	FileWriter fw = new FileWriter(fn,true); //the true will append the new data
            try {
	        	fileUtil.fileMainHeader(f1);
	            fileUtil.labelFileHeader(f1);
	            fileUtil.headerIncludesLabel(f1);
	            fileUtil.LabelDeclaration(f1,labellist);
	            }finally{
	            try {
	                fw.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	}
	
	/**
	 * helper function to get the Amalthea Model
	 * 
	 */
	public Amalthea getModel() {
		return this.model;
	}

}
