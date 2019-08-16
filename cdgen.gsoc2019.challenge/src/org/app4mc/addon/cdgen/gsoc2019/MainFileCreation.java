package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.HardwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil.TimeType;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.TimeUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Main function in which scheduling is done.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class MainFileCreation {
	final private Amalthea model;
	/**
	 * MainFileCreation Constructor
	 * 
	 * @param Model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	public MainFileCreation(final Amalthea Model, String srcPath, int configFlag) throws IOException { this.model = Model;
	System.out.println("Main File Creation Begins");
	if (0x2000 == (configFlag & 0xF000)) {
		fileCreatePthread(model, srcPath);
	}else {
		fileCreate(model, srcPath, configFlag);
	}
	System.out.println("Main File Creation Ends");
	}

	/**
	 * MainFileCreation File Creation
	 * 
	 * @param model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	private static void fileCreate(Amalthea model, String srcPath, int configFlag) throws IOException {
		EList<Task> tasks = model.getSwModel().getTasks(); 
		String fname = srcPath + File.separator + "main.c";
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
				headerIncludesMainRMS(f1);
				mainTaskStimuli(model, f1, tasks);
				mainTaskPriority(f1, tasks);
				//mainFucntionRMS(model, f1, tasks);
			}else {
				headerIncludesMain(f1);
				sleepTimerMs(f1);
				mainTaskPriority(f1, tasks);
				mainFucntionMulticore(model, f1, tasks);
			}
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Shared Label definition and initialization structure.
	 * 
	 * @param file
	 * @param labellist
	 */
	public static List<Label> SharedLabelCoreDefinition(Amalthea model, String srcPath) {
			EList<Label> labellist = model.getSwModel().getLabels();
			//List<Label> SharedLabelCoreList = LabelFileCreation.SharedLabelFinder(model);
			List<Label> SharedLabelListSortCore = new ArrayList<Label>();
			if(labellist.size()==0) {
				System.out.println("Shared Label size 0");
			}else {
			//	System.out.println("Shared Label size "+SharedLabelList.size());
				HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation.LabelTaskMap(model, labellist);
				int i=0, k=0, j=0;
				for(Label share:labellist) {
					HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
					Collection<ProcessingUnit> puList = TaskMap.values();
					List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
					Set<Task> TaskList = TaskMap.keySet();
					if(puListUnique.size()==1 && TaskList.size()>1) {
						SharedLabelListSortCore.add(share);
						i++;
					}
					else if(puListUnique.size()>1){
						j++;
					}else if(TaskList.size()==1){
						k++;
					}
				}
				System.out.println("Total Labels :"+sharedLabelTaskMap.keySet().size()+"="+i+"+"+j+"+"+k+"="+(i+j+k)); 
			}
			return SharedLabelListSortCore;
	}

	/**
	 * Main function for RMS Scheduler
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
			for (Task task: tasks) {
				MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;

				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();

					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					//System.out.println("time obj = " +taskTime);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.US);
					//System.out.println("raw2 = " +taskTime.getValue());
					double sleepTime = TimeUtil.getAsTimeUnit(taskTime, null);
					fw.write("\tAmaltheaTask AmalTk_"+task.getName()+" = createAmaltheaTask("+task.getName()+", cIN_" + task.getName() +", cOUT_" + task.getName()+", "+task.getStimuli().get(0).getName()+", "+task.getStimuli().get(0).getName()+", "+sleepTime+");\n");
				}
			}
			//	int c=0;
			/*for (Task task : tasks) {
				fw.write("\n\ttaskList["+c+"]\t=\tAmalTk_"+task.getName()+";");
				c++;
			}*/
			fw.write("\n\tvDisplayMessage(\"created RMS sched task\\n\");\n");
			int count =0;
			for (Task task : tasks) {
				/*fw.write("\txTaskCreate(generalizedRTOSTak , \"AmalTk_"+task.getName()+"\", configMINIMAL_STACK_SIZE, &AmalTk_"+task.getName()
				+", main"+task.getName()+", taskList["+count+"].taskHandle, main" + task.getName() +", null);\n");
				 */fw.write("\txTaskCreate(generalizedRTOSTask , \"AmalTk_"+task.getName()+"\", configMINIMAL_STACK_SIZE, &AmalTk_"+task.getName()
				 +", main"+task.getName()+", NULL);\n");
				 count++;
			}
			fw.write("\tvDisplayMessage(\"created other tasks\\n\");\n");
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return EXIT_SUCCESS;\n");
			fw.write("}\n\n");
			fw.close();
		}catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void sleepTimerMsPthread(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("void sleepTimerMs(int ticks)\n");
			fw.write("{\n");
			fw.write("\tint var;\n");
			fw.write("\tfor (var = 0; var < ticks; ++var)\n");
			fw.write("\t{\n");
			fw.write("\t\tsuspendMe();\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\tusleep(1000);\n");
			fw.write("\t\t}\n");
			fw.write("\t\tresumeMe();\n");
			fw.write("\t}\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
	private static void sleepTimerMs(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("void sleepTimerMs(int ticks)\n");
			fw.write("{\n");
			fw.write("\tint var;\n");
			fw.write("\tfor (var = 0; var < ticks; ++var)\n");
			fw.write("\t{\n");
			fw.write("\t\tvTaskSuspendAll();\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\tusleep(1000);\n");
			fw.write("\t\t}\n");
			fw.write("\t\txTaskResumeAll();\n");
			fw.write("\t}\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void fileCreatePthread(Amalthea model, String path1) throws IOException {
		EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k=0;
		for(SchedulerAllocation c:CoreNo) {
			ProcessingUnit pu = c.getResponsibility().get(0);
			Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
		String fname = path1 + File.separator + "main"+k+".c";
		File f2 = new File(path1);
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
			headerIncludesMainPthread(f1, model);
			sleepTimerMsPthread(f1);
			mainFucntionPthread(f1, tasks);
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

	private static void mainFucntionPthread(File f1, Set<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\tpthread_t thread[NUM_THREADS];\n");
			fw.write("\tpthread_attr_t attr;\n\n");
			int tasksize = tasks.size();
			int init = 0;
			while (init < tasksize) {
				fw.write("\tlong rtr" + init + "=" + init + ";\n");
				init++;
			}
			fw.write("\tlong t;\n");
			fw.write("\tint rc;\n");
			fw.write("\tvoid *status;\n\n");
			fw.write("\tfor(;;)\n\t{\n");
			fw.write("\t\tpthread_attr_init(&attr);\n");
			fw.write("\t\tpthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);\n\n");
			int init1 = 0;
			while (init1 < tasksize) {
				fw.write("\t\tpthread_create (&thread[" + init1 + "], &attr, v" + tasks.iterator().next().getName()
						+ ", (void *)rtr" + init1 + ");\n");
				init1++;
			}
			fw.write("\t\tif (rc)\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\tprintf(\"ERROR; return code from pthread_join() is %d\\n\", rc);\n");
			fw.write("\t\t\texit(-1);\n");
			fw.write("\t\t}\n");

			fw.write("\n\t\tpthread_attr_destroy(&attr);\n");
			fw.write("\n\t\tfor(t=0; t<NUM_THREADS; t++)\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\trc = pthread_join(thread[t], &status);\n");
			fw.write("\t\t\tif (rc)\n");
			fw.write("\t\t\t{\n");
			fw.write("\t\t\t\tprintf(\"ERROR; return code from pthread_join() is %d\\n\", rc);\n");
			fw.write("\t\t\t\texit(-1);\n");
			fw.write("\t\t\t}\n");
			fw.write(
					"\t\t\tprintf(\"Main: completed join with thread %ld having a status of %ld\\n\",t,(long)status);\n");
			fw.write("\t\t}\n");
			fw.write("\t\tprintf(\"\\n\");\n");
			fw.write("\t}\n");
			fw.write("\tprintf(\"Main: program completed. Exiting.\\n\");\n");
			fw.write("\tpthread_exit(NULL);\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesMainPthread(File f1, Amalthea model2) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <pthread.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"taskDef.h\"\n");
			fw.write("#define NUM_THREADS\t" + model2.getSwModel().getTasks().size() + "\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void mainFucntionMulticore(Amalthea model, File f1, EList<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			for (Task task : tasks) {
				MappingModel mappingModel = model.getMappingModel();
				if (mappingModel != null) {
					List<ProcessingUnit> processingUnits = HardwareUtil.getModulesFromHwModel(ProcessingUnit.class, model);
					ArrayList<ProcessingUnit> localPU = new ArrayList<ProcessingUnit>();
					localPU.addAll(processingUnits);
					HashMap<ProcessingUnit,Long> CoreMap = new HashMap<ProcessingUnit,Long>();
					long count = 0;
					for (ProcessingUnit p: localPU) {
						CoreMap.put(p, count);	
						count++;
					}
					ProcessingUnit pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();

					Long coreID = CoreMap.get(pu);
					fw.write("\txTaskCreate( "+coreID+", v" + task.getName() + ", \"" + task.getName().toUpperCase()
							+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
				}
				else {
					fw.write("\txTaskCreate( v" + task.getName() + ", \"" + task.getName().toUpperCase()
							+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
				}
			}
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return 0;\n");
			fw.write("}\n");
			fw.close();
		}catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	private static void mainFileHeader(File f1) {
		try {
			File fn = f1;
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

	private static void headerIncludesMain(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"taskDef.h\"\n");
			fw.write("#include \"FreeRTOS.h\"\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void headerIncludesMainRMS(File f1) {
		try {
			File fn = f1;
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
			fw.write("#include \"taskDef.h\"\n\n");
			fw.write("#define READ_PRECISION_US 1000\n\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void mainFucntion(File f1, EList<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			for (Task task : tasks) {
				fw.write("\txTaskCreate( v" + task.getName() + ", \"" + task.getName().toUpperCase()
						+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
			}
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return 0;\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
	//TODO Read paper send by lukas
	private static void mainTaskPriority(File f1, EList<Task> tasks) {
		try {
			File fn = f1;
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
			for (int i=0;i<periodMapSorted.size();i++) {
				Task task = (Task) periodMapSorted.keySet().toArray()[i];
				fw.write("\t#define main" + task.getName() + "\t( tskIDLE_PRIORITY +"
						+ (i+1) + " )\n");//TODO merge this constval with the value used in time period in FreeRTOS config File - Issue001
			}
			/*
			for (Task task : tasks) {
				fw.write("\t#define main" + task.getName() + "\t( tskIDLE_PRIORITY + "
						+ task.getMultipleTaskActivationLimit() + " )\n");
			}*/
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	private static void mainTaskStimuli(Amalthea model, File f1, EList<Task> tasks) {
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
			/*for (Stimulus s : Stimuli) {
				if (s instanceof PeriodicStimulus) {
					System.out.println("Task rec "+((PeriodicStimulus) s).getRecurrence());
				}
			//	fw.write("\t#define " + task.getName() + "\t"+fileUtil.getRecurrence(task).getValue() +" \n");
			}*/
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
