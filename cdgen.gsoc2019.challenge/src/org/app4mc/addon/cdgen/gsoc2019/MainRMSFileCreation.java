package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil.TimeType;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.TimeUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
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

public class MainRMSFileCreation {
	final private Amalthea model;

	public MainRMSFileCreation(final Amalthea Model, String path1, int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Main File Creation Begins");
		fileCreate(model, path1, configFlag);
		System.out.println("Main File Creation Ends");
	}

	private static void fileCreate(Amalthea model, String path1, int configFlag) throws IOException {
		//EList<Task> tasks = model.getSwModel().getTasks();
		model.getMappingModel().getTaskAllocation();
		EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		//ssmodel.getMappingModel().getSchedulerAllocation().get
		//EList<Task> tasks = model.getSwModel().getTasks();
	//for(int k=0; k<CoreNo.size(); k++) {
			//tasks = org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.DeploymentUtil.getProcessesMappedToCore((ProcessingUnit)CoreNo.get(k), model);
		int k=0;
		for(SchedulerAllocation c:CoreNo) {
			ProcessingUnit pu = c.getResponsibility().get(0);
		//	System.out.println("Core ==> "+pu);
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
				if((0x0100 == (0x0F00 & configFlag)) & (0x3000 == (0xF000 & configFlag)) ) {
					headerIncludesMainRMS(f1);
					mainTaskStimuli(model, f1, tasks);
					mainTaskPriority(f1, tasks);
					mainFucntionRMS(model, f1, tasks);
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

	private static void mainFucntionRMS(Amalthea model, File f1, Set<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\toutbuf_init();\n");
			for (Task task: tasks) {
				MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;
				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.US);
					//System.out.println("\ntaskTime == "+task.getName()+" ==> "+taskTime + "==>"+fileUtil.getRecurrence(task));
					double sleepTime = TimeUtil.getAsTimeUnit(taskTime, null);
					fw.write("\tAmaltheaTask AmalTk_"+task.getName()+" = createAmaltheaTask( v"+task.getName()+", cIN_" + task.getName() +", cOUT_" + task.getName()+", "+task.getStimuli().get(0).getName()+", "+task.getStimuli().get(0).getName()+", "+sleepTime+");\n");
				}
			}
			//fw.write("\n\tvDisplayMessage(\"created RMS sched task\\n\");\n");
			int count =0;
			for (Task task : tasks) {
				fw.write("\txTaskCreate(generalizedRTOSTask , \"AmalTk_"+task.getName()+"\", configMINIMAL_STACK_SIZE, &AmalTk_"+task.getName()
				+", main"+task.getName()+", NULL);\n");
				count++;
			}
			//fw.write("\tvDisplayMessage(\"created other tasks\\n\");\n");
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return EXIT_SUCCESS;\n");
			fw.write("}\n\n");
			fw.close();
		}catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/*private static void createTask(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("AmaltheaTask createTask(void *taskHandler,\n");
			fw.write("\t\t\tvoid *cInHandler,\n");
			fw.write("\t\t\tvoid *cOutHandler,\n");
			fw.write("\t\t\tunsigned int period,\n");
			fw.write("\t\t\tunsigned int deadline,\n");
			fw.write("\t\t\tunsigned int WCET,\n");
			fw.write("\t\t\txTaskHandle tHandle\n");
			fw.write("\t\t\t){\n");
			fw.write("\tif (WCET >= period){\n");
			fw.write("\t\tAmaltheaTask retValNull = {0,0,NULL,0,0,0,NULL,NULL,NULL};\n");
			fw.write("\t\treturn retValNull;\n");
			fw.write("\t}else{\n");
			fw.write("\t\tAmaltheaTask retVal = {0,0,taskHandler,WCET,deadline,period,cInHandler,cOutHandler,tHandle};\n");
			fw.write("\t\treturn retVal;\n");
			fw.write("\t}\n");
			fw.write("}\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}*/

	/*private static void sleepTimerUsRMS(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("void sleepTimerMs(int ticks, int taskNum)\n");
			fw.write("{\n");
			fw.write("\tint var;\n");
			fw.write("\tfor (var = 0; var < ticks; ++var)\n");
			fw.write("\t{\n");
			fw.write("\t\ttaskENTER_CRITICAL();\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\tvTaskIncrementTick();\n");
			fw.write("\t\t\ttraceTaskStatus(3,taskNum);\n");
			fw.write("\t\t\tsleep (1);\n");
			fw.write("\t\t}\n");
			fw.write("\t\ttaskEXIT_CRITICAL();\n");
			fw.write("\t}\n");
			fw.write("}\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}*/

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

	/*private static void generalizedRTOSTaskRMS(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("void generalizedRTOSTak(AmaltheaTask *task){\n");
			fw.write("\tportTickType xLastWakeTime = xTaskGetTickCount();\n");
			fw.write("\tfor (;;){\n");
			fw.write("\t\ttraceTaskStatus(1,task->period);\n");
			fw.write("\t\tsleepTimerMs(task->executionTime,task->period);\n");
			fw.write("\t\ttraceTaskStatus(0,task->period);\n");
			fw.write("\t\tvTaskDelayUntil( &xLastWakeTime, task->period);\n");
			fw.write("\t}\n");
			fw.write("}\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}*/


	/*private static void createRTOSTaskRMS(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("void createRTOSTask(AmaltheaTask task,int prio){\n");
			fw.write("\txTaskCreate(generalizedRTOSTak,\"t5ms\",configMINIMAL_STACK_SIZE,&task,prio,task1);\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void taskHandlerDisplayRMS(File f1, EList<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			for (Task task: tasks) {
				fw.write("void "+task.getName()+"Handler(void ){\n");
				fw.write("\tvDisplayMessage(\""+task.getName()+" handler\\n\");\n");
				fw.write("}\n\n");

			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void AmaltheaTaskRMS(Amalthea model, FileWriter fw, EList<Task> tasks) {
		try {
			for (Task task: tasks) {
				MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;

				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();

					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.MS);
					double sleepTime = TimeUtil.getAsTimeUnit(taskTime, null);
					fw.write("\tAmaltheaTask AmalTk_"+task.getName()+" = createTask("+task.getName()+", NULL,NULL,"+
							task.getStimuli().get(0).getName()+", "+task.getStimuli().get(0).getName()+", "+sleepTime
							+", "+"taskHandle"+task.getName()+");\n");
				}
			}
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	private static void taskStructureRMS(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("typedef struct{\n");
			fw.write("\tunsigned isDone;\n");
			fw.write("\tunsigned isReady;\n");
			fw.write("\tvoid(* taskHandler)();\n");
			fw.write("\tunsigned executionTime;\n");
			fw.write("\tunsigned deadline;\n");
			fw.write("\tunsigned period;\n");
			fw.write("\tvoid(* cInHandler)();\n");
			fw.write("\tvoid(* cOutHandler)();\n");
			fw.write("\txTaskHandle taskHandle;\n");
			fw.write("}AmaltheaTask;\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void traceTaskStatusRMS(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("void traceTaskStatus(int taskStatus, int taskNum){\n");
			fw.write("\tchar bufLocal[256];\n");
			fw.write("\ttaskENTER_CRITICAL();\n");
			fw.write("\tportTickType currentTickCount = xTaskGetTickCount();//*passes1;\n");
			fw.write("\ttaskEXIT_CRITICAL();\n");
			fw.write("\tif (taskStatus ==1){\n");
			fw.write("\t\tsprintf(bufLocal,\"Task %d released at time %d \\n\",taskNum,currentTickCount);\n");
			fw.write("\t}else if(taskStatus ==0) {\n");
			fw.write("\t\tsprintf(bufLocal,\"\\tTask %d finished at time %d \\n\",taskNum,currentTickCount);\n");
			fw.write("\t}else if(taskStatus ==3){\n");
			fw.write("\t\tsprintf(bufLocal,\"\\t\\t\\t\\tTask %d holding at time %d \\n\",taskNum,currentTickCount);\n");
			fw.write("\t}else {\n");
			fw.write("\t\tsprintf(bufLocal,\"\\t\\t\\t\\t\\t\\tTask %d yielded at time %d \\n\",taskNum,currentTickCount);\n");
			fw.write("\t}\n");
			fw.write("\tvDisplayMessage(bufLocal);\n");
			fw.write("\tif (currentTickCount==20){\n");
			fw.write("\t\tvDisplayMessage(\"======================================\\n\");\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}}

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
	}*/


	/*private static void mainFucntionMulticore(Amalthea model, File f1, EList<Task> tasks) {
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
	 */

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
			fw.write("#include \"ParallellaUtils.h\"\n");
			fw.write("#include \"taskDef.h\"\n\n");
			fw.write("#define READ_PRECISION_US 1000\n\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/*	private static void mainFucntion(File f1, EList<Task> tasks) {
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
	}*/
	//TODO Read paper send by lukas
	private static void mainTaskPriority(File f1, Set<Task> tasks) {
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
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void mainTaskStimuli(Amalthea model, File f1, Set<Task> tasks) {
		try {
			File fn = f1;

			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* TaskStimuli. */\n");
			EList<Stimulus> Stimuli = model.getStimuliModel().getStimuli();
			for (Stimulus s : Stimuli)  {
				if (s instanceof PeriodicStimulus) {
					fw.write("\t#define " + s.getName() + "\t"+((PeriodicStimulus) s).getRecurrence().getValue() +"*1000 // To account for MS to US conversion \n");
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
