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
package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.DeploymentUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessStatistic;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.emf.common.util.EList;

/**
 * Declaration of Labels with initial values .
 * 
 * @author Ram Prasath Govindarajan
 *
 */

/**
 * @author rpras
 *
 */
public class LabelFileCreation {
	final private Amalthea model;

	/**
	 * Constructor LabelFileCreation
	 *
	 * @param Model
	 * Amalthea Model
	 * @param srcPath
	 * @throws IOException
	 */
	public LabelFileCreation(final Amalthea Model, String srcPath) throws IOException {
		this.model = Model;
		System.out.println("Label File Creation Begins");
		fileCreate(model, srcPath);
		System.out.println("Label File Creation Ends");
	}

	/**
	 * FileCreation LabelFileCreation
	 * 
	 * @param model
	 * @param srcPath
	 * @throws IOException
	 */
	private static void fileCreate(Amalthea model, String srcPath) throws IOException {
		EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k=0;
		for(SchedulerAllocation c:CoreNo) {
			ProcessingUnit pu = c.getResponsibility().get(0);
			Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, model);
			List<Task> tasks = new ArrayList<Task>(task);
			List<Label> labelCoreCommonList = fileUtil.CoreSpecificLabel(model, tasks);
			List<Label> sharedLabelList = fileUtil.SharedLabelDeclarationHead(model, tasks);
			String fname1 = srcPath + File.separator + "label"+k+".c";
			String fname2 = srcPath + File.separator + "label"+k+".h";
			File f2 = new File(srcPath);
			File f1 = new File(fname1);
			File f3 = new File(fname2);
			f2.mkdirs();
			try {
				f1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			try {
				fileUtil.fileMainHeader(f1);
				labelFileHeader(f1);
				headerIncludesLabelHead(f1, k);
				LabelDeclaration(f1, tasks, labelCoreCommonList);
				LabelDeclarationLocal(f1, tasks, labelCoreCommonList,sharedLabelList);
				//	SharedLabelCoreDefinition(f1, model, labelList);
				//	SharedLabelFinder(f1, tasks);
				//	SharedLabelFinder1(f1, tasks);
			} finally {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				fileUtil.fileMainHeader(f3);
				labelFileHeader(f3);
				headerIncludesLabel(f3);
				//LabelDeclaration(f3, labelList);
				LabelDeclarationLocalHeader(f3, tasks, labelCoreCommonList);
				//	SharedLabelCoreDefinition(f1, model, labelList);
				//	SharedLabelFinder(f1, tasks);
				//	SharedLabelFinder1(f1, tasks);
			} finally {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			k++;
		}}

	/**
	 * Title card - LabelFileCreation
	 * 
	 * @param file
	 */
	private static void labelFileHeader(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Label Declaration\n");
			fw.write("*Description	:	Declaration and Initialisation of Label\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Header inclusion - LabelFileCreation
	 * 
	 * @param file
	 */
	private static void headerIncludesLabel(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdint.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"shared_comms.h\"\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Header inclusion - LabelFileCreation
	 * 
	 * @param file
	 */
	private static void headerIncludesLabelHead(File file, int k) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdint.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"shared_comms.h\"\n");
			fw.write("#include \"label"+k+".h\"\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}



	/**
	 * Label definition and initialization structure.
	 * 
	 * @param file
	 * @param tasks 
	 * @param labellist
	 */
	private static void LabelDeclaration(File file, List<Task> tasks, List<Label> labellist) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			labellist.stream().distinct().collect(Collectors.toList());
			for (Label label : labellist) {
				String type = fileUtil.datatype(label.getSize().toString());
				long init = fileUtil.intialisation(label.getSize().toString());
				fw.write("\t" + type + "	" + label.getName() + " \t=\t " + init + ";\n");
			}

			fw.write("\n\n\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Local label definition and initialization structure. Task specific local labels are defined to perform Cin and Cout operation.
	 * 
	 * @param file
	 * @param tasks
	 * @param sharedLabelList 
	 */
	private static void LabelDeclarationLocal(File file, List<Task> tasks, List<Label> labelCoreCommonList, List<Label> sharedLabelList) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			for (Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				ArrayList<Label> labellist1 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				Set<Label> TaskLabel = SoftwareUtil.getAccessedLabelSet(task, null);
				fw.write("\n //local variable for " + task.getName() + "\n");
				for (Label lab : listWithoutDuplicates2) {
					if((TaskLabel.contains(lab))&&!(sharedLabelList.contains(lab))&&(labelCoreCommonList.contains(lab)) ) {
						String type = fileUtil.datatype(lab.getSize().toString());
						long init = fileUtil.intialisation(lab.getSize().toString());
						fw.write("\t\t" + type + "\t" + lab.getName() + "_" + task.getName() + "\t=\t" + init + ";\n");	
					}
				}
				fw.write("\n //Shared label \n");
				for (Label lab : sharedLabelList) {
					if(TaskLabel.contains(lab)) {
						String type = fileUtil.datatype(lab.getSize().toString());
						long init = fileUtil.intialisation(lab.getSize().toString());
						fw.write("\t\t" + type + "\t" + lab.getName() + "_" + task.getName() + " \t=\t " + init + ";\n");	

					}
				}
				Set<Label> readLabels = SoftwareUtil.getReadLabelSet(task, null);
				Set<Label> writeLabels = SoftwareUtil.getWriteLabelSet(task, null);
				fw.write("\n\tvoid cIN_" + task.getName() + "()\n\t{\n");
				List<String> readLabelType = new ArrayList<String>();;
				HashMap<String, HashMap<Label, Integer>> SharedLabelTypeMapIndexed = new HashMap<String, HashMap<Label, Integer>> ();
				for(Label share:sharedLabelList) {
					String type = share.getSize().toString();
					readLabelType.add(type);
				}
				readLabelType = readLabelType.stream().distinct().collect(Collectors.toList());
				//System.out.println(" Share size ==>"+readLabelType.size());
				for(String rLT:readLabelType) {
					
					HashMap<Label, Integer> SharedLabelTypeMap = new HashMap<Label, Integer>();
					int k = 0;
					for(Label share:sharedLabelList) {
						String type = share.getSize().toString();
						if(type.equals(rLT)) {
							SharedLabelTypeMap.put(share, new Integer(k));
							k++;	
						}
					}
					SharedLabelTypeMapIndexed.put(rLT, SharedLabelTypeMap);
				}
				HashMap<Label, Integer> LabelIndexedType = new HashMap<Label, Integer>();
				for(String rLT:readLabelType) {
					LabelIndexedType = SharedLabelTypeMapIndexed.get(rLT);		
					for(Label share:sharedLabelList) {
						String type = share.getSize().toString();
						if((type.equals(rLT))&(readLabels.contains(share))) {
							fw.write("\t\t" + share.getName()+"_"+task.getName() + "\t=\tshared_label_" + type.replace(" ", "") + "_read("+LabelIndexedType.get(share)+");\n");
						}
					}
				}
				
				//fw.write("\t\tvDisplayMessage( \" Cin Execution\t" + task.getName() + "\\n\" );\n");
				for (Label lab : listWithoutDuplicates2) {
					if(labelCoreCommonList.contains(lab)) {
						fw.write("\t\t" + lab.getName() + "_" + task.getName() + "\t=\t" + lab.getName() + ";\n");
					}
				}
				/*for (Label lab : listWithoutDuplicates2) {
					String la = lab.getSize().toString().replace(" ", "");
					if((sharedLabelList.contains(lab)) & (readLabels.contains(lab))) {
						fw.write("\t\t" + lab.getName()+"_"+task.getName() + "\t=\tshared_label_" + la + "_read("+SharedLabelTypeMap.get(lab)+");\n");
					}
				}*/
				fw.write("\t}\n");
				fw.write("\n\tvoid cOUT_" + task.getName() + "()\n\t{\n");
				//fw.write("\t\tvDisplayMessage(\" Cout Execution\t" + task.getName() + "\\n\\n\" );\n");
				ArrayList<Label> labellist2 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getWriteLabelSet(run, null);
					labellist2.addAll(labellist);
				}
				List<Label> listWithoutDuplicates1 = labellist2.stream().distinct().collect(Collectors.toList());
				for (Label lab : listWithoutDuplicates1) {
					if(labelCoreCommonList.contains(lab) & (writeLabels.contains(lab))) {
						fw.write("\t\t" + lab.getName() + "\t=\t" + lab.getName() + "_" + task.getName() + ";\n");
					}
				}
			/*	for (Label lab : listWithoutDuplicates2) {
					String la = lab.getSize().toString().replace(" ", "");
					if(sharedLabelList.contains(lab)) {
						fw.write("\t\tshared_label_" + la + "_write("+SharedLabelTypeMap.get(lab)+"," + lab.getName()+"_"+task.getName() + " );\n");
					}
				}*/
				HashMap<Label, Integer> LabelWriteIndexedType = new HashMap<Label, Integer>();
				for(String rLT:readLabelType) {
					LabelWriteIndexedType = SharedLabelTypeMapIndexed.get(rLT);		
					for(Label share:sharedLabelList) {
						String type = share.getSize().toString();
						if((type.equals(rLT))&(readLabels.contains(share))) {
							//fw.write("\t\t" + share.getName()+"_"+task.getName() + "\t=\tshared_label_" + type + "_read("+LabelIndexedType.get(share)+");\n");
							fw.write("\t\t" + share.getName()+"_"+task.getName() + "++;\n");
							fw.write("\t\tshared_label_" + type.replace(" ", "") + "_write("+LabelWriteIndexedType.get(share)+"," + share.getName()+"_"+task.getName() + " );\n");
						}
					}
				}
				
				fw.write("\t}\n");
				fw.write("\n\n");
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void LabelDeclarationLocalHeader(File file, List<Task> tasks, List<Label> labelList) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			for (Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				ArrayList<Label> labellist1 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				fw.write("\n\tvoid cIN_" + task.getName() + "();");
				fw.write("\n\tvoid cOUT_" + task.getName() + "();");
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void SharedLabelFinder1(File file, EList<Task> tasks) {
		ArrayList<Label> labelCombined = new ArrayList<Label>();
		ArrayList<Label> labelOhneDuplicate = new ArrayList<Label>();
		List<Label> labelNoDup = new ArrayList<Label>();
		for(Task task:tasks)	{
			Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(task, null);
			labelCombined.addAll(labellist);
		}
		labelNoDup =labelCombined.stream().distinct().collect(Collectors.toList()); 
		HashMap<String, Label> LabelType1= new HashMap<String, Label>();
		for(Label la:labelNoDup) {
			String latype = la.getSize().toString();
			LabelType1.put(latype, la);
		}
		HashMap<String, List<Label>> LabelTypeMap= new HashMap<String, List<Label>>();
		Set<String> regex = LabelType1.keySet();
		Iterator<String> itr = regex.iterator();
		while(itr.hasNext()){
			ArrayList<Label> labelTypeList = new ArrayList<Label>();
			String maptypeset = itr.next();
			for(Label la:labelNoDup) {
				String latype = la.getSize().toString();
				if(latype.equals(maptypeset)) {
					labelTypeList.add(la);
				}

			}	
			LabelTypeMap.put(itr.next(), labelTypeList);
		}
		//Shared labels list
		Set<Label> uniques = new HashSet<>();
		for(Label t : labelCombined) {
			if(!uniques.add(t)) {
				labelOhneDuplicate.add(t);
				//System.out.println("\n==>"+t.getName());	
			}
		}
		List<Label> sharedLabelList = labelOhneDuplicate.stream().distinct().collect(Collectors.toList());
		HashMap<String, List<Label>> LabelTypeMap1= new HashMap<String, List<Label>>();
		Set<String> regex1 = LabelType1.keySet();
		Iterator<String> itr1 = regex1.iterator();
		while(itr1.hasNext()){
			ArrayList<Label> labelTypeList = new ArrayList<Label>();
			String maptypeset = itr1.next();
			for(Label la:sharedLabelList) {
				String latype = la.getSize().toString();
				if(latype.equals(maptypeset)) {
					labelTypeList.add(la);
				}

			}	
			LabelTypeMap1.put(itr.next(), labelTypeList);
		}
	}


	public static List<Label> SharedLabelFinder(Amalthea model) {
		EList<Task> tasks = model.getSwModel().getTasks();
		ArrayList<Label> labelCombined = new ArrayList<Label>();
		ArrayList<Label> labelOhneDuplicate = new ArrayList<Label>();
		for(Task task:tasks)	{
			Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(task, null);
			labelCombined.addAll(labellist);
		}
		Set<Label> uniques = new HashSet<>();
		for(Label t : labelCombined) {
			if(!uniques.add(t)) {
				labelOhneDuplicate.add(t);
			}
		}
		List<Label> sharedLabelList = labelOhneDuplicate.stream().distinct().collect(Collectors.toList());
		return sharedLabelList;
	}





	public static HashMap<Label, HashMap<Task,ProcessingUnit>> LabelTaskMap(Amalthea model, List<Label> labelList) {
		EList<Task> tasks = model.getSwModel().getTasks();
		HashMap<Label,HashMap<Task,ProcessingUnit>> localLabelAllocation = new HashMap<Label,HashMap<Task,ProcessingUnit>>();

		for (Label label:labelList) {
			HashMap<Task,ProcessingUnit> localAllocation = new HashMap<Task,ProcessingUnit>();
			for(Task task:tasks) {
				ProcessingUnit pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
				ArrayList<Label> labelListLocalTask = new ArrayList<Label>(SoftwareUtil.getAccessedLabelSet(task, null));
				if (labelListLocalTask.contains(label)) {
					localAllocation.put(task,pu);
				}
			}
			if (localAllocation!=null) {
				localLabelAllocation.put(label, localAllocation);
			}
		}
		return localLabelAllocation;
	}


	/**
	 * helper function to get the Amalthea Model
	 * 
	 */
	public Amalthea getModel() {
		return this.model;
	}
}
