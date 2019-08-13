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
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
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
public class SharedLabelsFileCreation {
	final private Amalthea model;

	/**
	 * Constructor LabelFileCreation
	 *
	 * @param Model
	 * Amalthea Model
	 * @param srcPath
	 * @throws IOException
	 */
	public SharedLabelsFileCreation(final Amalthea Model, String srcPath) throws IOException {
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
		EList<Task> tasks = model.getSwModel().getTasks();
		EList<Label> labellist = model.getSwModel().getLabels();
		String fname1 = srcPath + File.separator + "shared_comms.c";
		String fname2 = srcPath + File.separator + "shared_comms.h";
		File f2 = new File(srcPath);
		File f1 = new File(fname1);
		File f3 = new File(fname2);
		f2.mkdirs();
		try {
			f1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fn1 = f1;
		File fn2 = f3;
		
		FileWriter fw = new FileWriter(fn1, true);
		try {
			fileUtil.fileMainHeader(f1);
			sharedLabelFileHeader(f1);
			headerIncludesSharedLabel(f1);
			SharedLabelDeclaration(f1, model, labellist);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileUtil.fileMainHeader(f3);
			sharedLabelFileHeaderHead(f3);
			headerIncludesSharedLabelHead(f3);
			SharedLabelDeclarationHead(f3, model, labellist);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Title card - LabelFileCreation
	 * 
	 * @param file
	 */
	private static void sharedLabelFileHeader(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Shared Label Declaration\n");
			fw.write("*Description	:	Declaration and Initialisation of Shared Label\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Title card - LabelFileCreation
	 * 
	 * @param file
	 */
	private static void sharedLabelFileHeaderHead(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Shared Label Declaration\n");
			fw.write("*Description	:	Header file for Declaration and Initialisation of Shared Label\n");
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
	private static void headerIncludesSharedLabelHead(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("#ifndef DEMO_PARALLELLA_SHARED_COMMS_H_\n");
			fw.write("#define DEMO_PARALLELLA_SHARED_COMMS_H_\n\n");
			
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <stdint.h>\n\n");
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
	private static void headerIncludesSharedLabel(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include \"shared_comms.h\"\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}	


	/**
	 * Shared Label definition and initialization structure.
	 * 
	 * @param file
	 * @param labellist
	 */
	private static void SharedLabelDeclaration(File file, Amalthea model, EList<Label> labellist) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			List<Label> SharedLabelList = LabelFileCreation.SharedLabelFinder(model);
			List<Label> SharedLabelListSortCore = new ArrayList<Label>();
			if(SharedLabelList.size()==0) {
				System.out.println("Shared Label size 0");
			}else {
				System.out.println("Shared Label size "+SharedLabelList.size());
				HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation.LabelTaskMap(model, SharedLabelList);
				for(Label share:SharedLabelList) {
					HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
					Collection<ProcessingUnit> puList = TaskMap.values();
					List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
					if(puListUnique.size()>1) {
						SharedLabelListSortCore.add(share);
					}
				}
			}

			HashMap<Label, String> SharedLabelTypeMap = new HashMap<Label, String>();
			for(Label share:SharedLabelListSortCore) {
				SharedLabelTypeMap.put(share, share.getSize().toString());
			}
			List<String> SharedTypeMapList = new ArrayList<>(SharedLabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
			List<Label> SharedLabelMapList =  new ArrayList<Label>(SharedLabelTypeMap.keySet());
			for(int k=0;k<SharedTypeMapList.size();k++) {
				List<Label> SharedLabel=new ArrayList<Label>();
				String sh = SharedTypeMapList.get(k);
				for(Label s:SharedLabelMapList) {
					String ShTy = SharedLabelTypeMap.get(s);
					if(sh.equals(ShTy)) {
						SharedLabel.add(s);
					}
				}
				int SharedLabelCounter = SharedLabel.size();
				if(SharedLabelCounter!=0) {
					
					
					fw.write("unsigned int *outbuf_shared"+sh.toString().replace(" ", "")+"["+SharedLabelCounter+"];\n\n");
					fw.write("void shared_label_"+sh.toString().replace(" ", "")+"_init(){\n");
					fw.write("\toutbuf_shared"+sh.toString().replace(" ", "")+"[0] = (unsigned int *) shared_mem_section"+sh.toString().replace(" ", "")+";\n");
					for(int i=1;i<SharedLabelCounter;i++) {
						fw.write("\toutbuf_shared"+sh.toString().replace(" ", "")+"["+i+"]=outbuf_shared"+sh.toString().replace(" ", "")+"["+i+"] + 1;\n");
					}
					fw.write("\tfor (int i=0;i<"+SharedLabelCounter+";i++){\n");
					fw.write("\t\t*outbuf_shared"+sh.toString().replace(" ", "")+"[i] =0;\n");
					fw.write("\t}\n");
					fw.write("}\n\n");
					fw.write(""+fileUtil.datatype(sh.toString())+" shared_label_"+sh.toString().replace(" ", "")+"_write(int label_indx,int payload){\n");
					fw.write("\t"+fileUtil.datatype(sh.toString())+" retval=NULL;\n");
					fw.write("\t*outbuf_shared"+sh.toString().replace(" ", "")+"[label_indx] = payload;\n");
					fw.write("\treturn retval;\n\n");
					fw.write("}\n\n");
					fw.write("unsigned int shared_label_"+sh.toString().replace(" ", "")+"_read(int label_indx){\n");
					fw.write("\treturn *outbuf_shared"+sh.toString().replace(" ", "")+"[label_indx];\n");
					fw.write("}\n\n");
					}
				SharedLabelCounter=0;
			}
			fw.write("\n\n\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Shared Label definition and initialization structure.
	 * 
	 * @param file
	 * @param labellist
	 */
	private static void SharedLabelDeclarationHead(File file, Amalthea model, EList<Label> labellist) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			List<Label> SharedLabelList = LabelFileCreation.SharedLabelFinder(model);
			List<Label> SharedLabelListSortCore = new ArrayList<Label>();
			if(SharedLabelList.size()==0) {
				System.out.println("Shared Label size 0");
			}else {
			//	System.out.println("Shared Label size "+SharedLabelList.size());
				HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation.LabelTaskMap(model, SharedLabelList);
				for(Label share:SharedLabelList) {
					HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
					Collection<ProcessingUnit> puList = TaskMap.values();
					List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
					if(puListUnique.size()>1) {
						SharedLabelListSortCore.add(share);
					}
				}
			}

			HashMap<Label, String> SharedLabelTypeMap = new HashMap<Label, String>();
			for(Label share:SharedLabelListSortCore) {
				SharedLabelTypeMap.put(share, share.getSize().toString());
			}
			List<String> SharedTypeMapList = new ArrayList<>(SharedLabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
			List<Label> SharedLabelMapList =  new ArrayList<Label>(SharedLabelTypeMap.keySet());
			for(int k=0;k<SharedTypeMapList.size();k++) {
				List<Label> SharedLabel=new ArrayList<Label>();
				String sh = SharedTypeMapList.get(k);
				for(Label s:SharedLabelMapList) {
					String ShTy = SharedLabelTypeMap.get(s);
					if(sh.equals(ShTy)) {
						SharedLabel.add(s);
					}
				}
				int SharedLabelCounter = SharedLabel.size();
				if(SharedLabelCounter!=0) {
					fw.write("#define shared_mem_section"+sh.toString().replace(" ", "")+"	0x0"+k+"000000\n\n");
					fw.write("void shared_label_"+sh.toString().replace(" ", "")+"_init();\n");
					fw.write(""+fileUtil.datatype(sh.toString())+" shared_label_"+sh.toString().replace(" ", "")+"_write(int label_indx,int payload);\n");
					fw.write("unsigned int shared_label_"+sh.toString().replace(" ", "")+"_read(int label_indx);\n");
					}
				SharedLabelCounter=0;
			}
			fw.write("\n\n");
			fw.write("#endif");
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
	 */
	private static void LabelDeclarationLocal(File file, EList<Task> tasks) {
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
				fw.write("\n //local variable for " + task.getName() + "\n");
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					long init = fileUtil.intialisation(lab.getSize().toString());
					fw.write("\t\t" + type + "\t" + lab.getName() + "\t=\t" + init + ";\n");
				}
				fw.write("\n\n");
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void SharedLabelFinder(File file, EList<Task> tasks) {
		ArrayList<Label> labelCombined = new ArrayList<Label>();
		ArrayList<Label> labelOhneDuplicate = new ArrayList<Label>();
		List<Label> labelNoDup = new ArrayList<Label>();
		for(Task task:tasks)	{
			Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(task, null);
			labelCombined.addAll(labellist);
		}

		//Set of label types
		List<String> labeltype = new ArrayList<String>();
		labelNoDup = labelCombined.stream().distinct().collect(Collectors.toList());
		HashMap<Label,String> LabelTypeMap = new HashMap<Label,String>();
		for (Label La: labelNoDup) {
			String LaType = La.getSize().toString();
			labeltype.add(La.getSize().toString());
			LabelTypeMap.put(La, LaType);
		}
		List<String> labeltype1=labeltype.stream().distinct().collect(Collectors.toList());
		for(String l:labeltype1) {
			//System.out.println("\n==>"+l+labeltype1.size());
		}
		System.out.println("Key Set "+LabelTypeMap.size());
		//Categories based on type
		//List<String> labelDatatypeList = new ArrayList<String>();
		HashMap<String, List<Label>> LabelType= new HashMap<String, List<Label>>();
		//	Map<String, <List>String> doubleBraceMap  = new HashMap<String, <List>String>() {
		Set<String> labelDatatypeList = LabelType.keySet();
		int n=labelDatatypeList.size();
		ArrayList<Label>[] al = new ArrayList[n]; 
		for (Label La: labelNoDup) {
			String LaType = La.getSize().toString();
			//LabelType.add(La.getSize().toString());
			//	LabelType.put(LaType,La);
		}
		Iterator<String> iterator = labelDatatypeList.iterator();
		// initializing 
		int i=0;
		while(iterator.hasNext()){ 
			String element = (String) iterator.next();
			al[i] = new ArrayList<Label>(); 
			for (Label La: labelNoDup) {
				if(La.getDataType().toString()==element) {
					al[i].add(La);
				}
			}
			i++;
		} 
		for (int j=0;j<LabelType.size();j++) { 
			System.out.println("\nDataType "+j+" "+al[j].size());
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
		for(Label l:sharedLabelList) {
			//System.out.println("\n==>"+l.getName());
		}
	}

	private static void SharedLabelFinder1(File file, EList<Task> tasks) {
		ArrayList<Label> labelCombined = new ArrayList<Label>();
		//	ArrayList<Label> labelTypeList = new ArrayList<Label>();
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
		/*for(int k=0; k<LabelType.size();k++) {

		}*/

	}


	/**
	 * helper function to get the Amalthea Model
	 * 
	 */
	public Amalthea getModel() {
		return this.model;
	}
}