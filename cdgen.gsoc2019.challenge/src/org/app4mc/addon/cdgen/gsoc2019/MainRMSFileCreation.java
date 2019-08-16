package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil.TimeType;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.TimeUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Main function in which scheduling is done.
 * Specific to RMS Scheduler
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class MainRMSFileCreation {
	final private Amalthea model;

	/**
	 * MainRMSFileCreation  Constructor
	 * 
	 * @param Model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	public MainRMSFileCreation(final Amalthea Model, String srcPath, int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Main File Creation Begins");
		fileCreate(model, srcPath, configFlag);
		System.out.println("Main File Creation Ends");
	}

	/**
	 * MainRMSFileCreation - File Creation
	 * 
	 * @param model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	private static void fileCreate(Amalthea model, String srcPath, int configFlag) throws IOException {
		EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k=0;
		for(SchedulerAllocation c:CoreNo) {
			ProcessingUnit pu = c.getResponsibility().get(0);
			Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
			String fname = srcPath + File.separator + "main"+k+".c";
			File f2 = new File(srcPath);
			File f1 = new File(fname);
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
				mainFileHeader(f1);
				if((0x0100 == (0x0F00 & configFlag)) & (0x3000 == (0xF000 & configFlag)) ) {
					headerIncludesMainRMS(f1, k);
					mainTaskStimuli(model, f1, tasks);
					mainTaskPriority(f1, tasks);
					mainFucntionRMS(model, f1, tasks);
					//	SharedLabelDeclarationHead(f1, model);
				}else {
					headerIncludesMainFreeRTOS(f1, k);
					mainTaskStimuli(model, f1, tasks);
					mainTaskPriority(f1, tasks);
					mainFucntionFreeRTOS(model, f1, tasks);
				}
			} finally {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			k++;
		}
	}

	/**
	 * Main function in Main file of RMS specific scheduler
	 * 
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void mainFucntionRMS(Amalthea model, File file, Set<Task> tasks) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\toutbuf_init();\n");

			EList<Label> labellist = model.getSwModel().getLabels();
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
					fw.write("\tvoid shared_label_"+sh.toString().replace(" ", "")+"_init();\n");
					//		fw.write("void shared_label_"+sh.toString().replace(" ", "")+"_init_core();\n");

				}
				SharedLabelCounter=0;
			}

			for (Task task: tasks) {
				MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;
				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.MS);
					//System.out.println("\ntaskTime == "+task.getName()+" ==> "+taskTime + "==>"+fileUtil.getRecurrence(task));
					BigInteger sleepTime = taskTime.getValue();
					BigInteger b2 = new BigInteger("1000"); 
					int comparevalue = sleepTime.compareTo(b2); 
					//System.out.println("Sleep time ==>"+comparevalue);
					if(comparevalue < 0) {
						fw.write("\tAmaltheaTask AmalTk_"+task.getName()+" = createAmaltheaTask( v"+task.getName()+", cIN_" + task.getName() +", cOUT_" + task.getName()+", "+task.getStimuli().get(0).getName()+", "+task.getStimuli().get(0).getName()+", 1);\n");
					}else {
						fw.write("\tAmaltheaTask AmalTk_"+task.getName()+" = createAmaltheaTask( v"+task.getName()+", cIN_" + task.getName() +", cOUT_" + task.getName()+", "+task.getStimuli().get(0).getName()+", "+task.getStimuli().get(0).getName()+", "+sleepTime+");\n");
					}
					//fw.write("\tAmaltheaTask AmalTk_"+task.getName()+" = createAmaltheaTask( v"+task.getName()+", cIN_" + task.getName() +", cOUT_" + task.getName()+", "+task.getStimuli().get(0).getName()+", "+task.getStimuli().get(0).getName()+", "+sleepTime+");\n");
				}
			}
			int count =0;
			for (Task task : tasks) {
				Set<Label> taskLabel = SoftwareUtil.getAccessedLabelSet(task, null);
				List<Label> taskLabelList = new ArrayList<>(taskLabel);
				HashMap<Label, String> LabelTypeMap = new HashMap<Label, String>();
				for(Label tl:taskLabelList) {
					LabelTypeMap.put(tl, tl.getSize().toString());
				}
				List<String> TypeList = new ArrayList<>(LabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
				List<Label> LabelList = new ArrayList<>(LabelTypeMap.keySet().stream().distinct().collect(Collectors.toList()));
				fw.write("\tcreateRTOSTask( &AmalTk_"+task.getName()+", main"+task.getName()+", " +TypeList.size()+ ",");
				List<Label> dataTypeList=new ArrayList<Label>();
				int k=0;
				for(String tl:TypeList) {
					fw.write(fileUtil.datatype(tl)+", ");
					for (Label La:LabelList) {
						if(LabelTypeMap.get(La).contains(tl)) {
							dataTypeList.add(La);
						}
					}
					fw.write(""+dataTypeList.size()+"");
					k++;
					if(k < TypeList.size()) {
						fw.write(", ");
							
					}
				}
				fw.write(");\n");
				count++;
			}
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return EXIT_SUCCESS;\n");
			fw.write("}\n\n");
			fw.close();
		}catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	/**
	 * Main function in Main file of RMS specific scheduler
	 * 
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void mainFucntionFreeRTOS(Amalthea model, File file, Set<Task> tasks) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\toutbuf_init();\n");

			EList<Label> labellist = model.getSwModel().getLabels();
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
					fw.write("\tvoid shared_label_"+sh.toString().replace(" ", "")+"_init();\n");
					//		fw.write("void shared_label_"+sh.toString().replace(" ", "")+"_init_core();\n");

				}
				SharedLabelCounter=0;
			}

			int count =0;
			for (Task task : tasks) {
				fw.write("\txTaskCreate(v"+task.getName()+" , \""+task.getName()+"\", configMINIMAL_STACK_SIZE, &v"
						+task.getName()
						+", main"+task.getName()+", NULL);\n");
				count++;
			}
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return EXIT_SUCCESS;\n");
			fw.write("}\n\n");
			fw.close();
		}catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}



	private static void taskHandleRMS(File f1, EList<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			List<Task> localTaskPriority = new ArrayList<Task>();
			localTaskPriority.addAll(tasks);
			fw.write("/* TaskHandler. */\n");
			for (Task task: tasks) {
				fw.write("\txTaskHandle\t\ttaskHandle"+task.getName()+";\n");
				//TODO merge this constval with the value used in time period in FreeRTOS config File - Issue001
			}
			fw.write("\tAmaltheaTask taskList[];\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Title Card of MainRMSFileCreation
	 * 
	 * @param file
	 */
	private static void mainFileHeader(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   C File for Tasks Call\n");
			fw.write("*Description	:	Main file in which scheduling is done \n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	/**
	 * MainRMSFileCreation Header inclusion
	 * 
	 * @param file
	 */
	private static void headerIncludesMainRMS(File file, int k) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <e_lib.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n");
			fw.write("#include \"task.h\"\n");
			fw.write("#include \"queue.h\"\n");
			fw.write("#include \"AmaltheaConverter.h\"\n");
			fw.write("#include \"debugFlags.h\"\n");
			fw.write("#include \"ParallellaUtils.h\"\n");
			fw.write("#include \"taskDef"+k+".h\"\n");
			fw.write("#include \"shared_comms.h\"\n\n");
			//	fw.write("#include \"c2c.h\"\n\n");
			//fw.write("#define READ_PRECISION_US 1000\n\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Assign Priority to task in RMS
	 * 
	 * @param file
	 * @param tasks
	 */
	private static void mainTaskPriority(File file, Set<Task> tasks) {
		try {
			File fn = file;
			List<Task> localTaskPriority = new ArrayList<Task>();
			localTaskPriority.addAll(tasks);
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* TaskPriorities. */\n");
			HashMap<Task,Long> periodMap = new HashMap<Task,Long>();
			for (Task task: tasks) {
				long period = fileUtil.getRecurrence(task).getValue().longValue();
				periodMap.put(task, period);
			}
			Map<Task, Long> periodMapSorted = fileUtil.sortByValue(periodMap);
		//	System.out.println("periodMapSorted Size "+ periodMapSorted.size());
			//	for (int i=0;i<(periodMapSorted.size());i++) {
			for (int i=(periodMapSorted.size()), k=0;i>0;i--,k++) {
				Task task = (Task) periodMapSorted.keySet().toArray()[k];
				fw.write("\t#define main" + task.getName() + "\t( tskIDLE_PRIORITY +"
						+ (i) + " )\n");//TODO merge this constval with the value used in time period in FreeRTOS config File - Issue001
			}
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
	
	/**
	 * MainRMSFileCreation Header inclusion
	 * 
	 * @param file
	 */
	private static void headerIncludesMainFreeRTOS(File file, int k) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <e_lib.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n");
			fw.write("#include \"task.h\"\n");
			fw.write("#include \"queue.h\"\n");
			fw.write("#include \"taskDef"+k+".h\"\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Macro for Stimuli in the model
	 * 
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void mainTaskStimuli(Amalthea model, File f1, Set<Task> tasks) {
		try {
			File fn = f1;

			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* TaskStimuli. */\n");
			EList<Stimulus> Stimuli = model.getStimuliModel().getStimuli();
			for (Stimulus s : Stimuli)  {
				if (s instanceof PeriodicStimulus) {
					fw.write("\t#define " + s.getName() + "\t"+((PeriodicStimulus) s).getRecurrence().getValue() +" \n");
				}
			}
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
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
