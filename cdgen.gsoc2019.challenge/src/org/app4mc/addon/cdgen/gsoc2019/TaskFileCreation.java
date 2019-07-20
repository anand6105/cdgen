package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.lang.System;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.DeploymentUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.HardwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.TimeUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Task Definition with Cin and Cout Calls.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class TaskFileCreation {
	final private Amalthea model;

	/**
	 * Constructor TaskFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param path1
	 * @param pthreadFlag
	 * @param preemptionFlag
	 * @throws IOException
	 */
	public TaskFileCreation(final Amalthea Model, String path1, String path2, int  configFlag) throws IOException {
		this.model = Model;
		
		if(0x2000 != (0xF000 & configFlag)) {
			System.out.println("Task File Creation Begins");
			fileCreate(model, path1, path2, configFlag);
			System.out.println("Task File Creation Ends");
		} else if((0x3000 == (0xF000 & configFlag))&(0x0100 != (0x0F00 & configFlag))) {
			System.out.println("Task File Creation Begins");
			fileCreate(model, path1, path2, configFlag);
			System.out.println("Task File Creation Ends");
		}
		else{
			System.out.println("Task File Creation Begins");
			fileCreatePthread(model, path1, path2, configFlag);
			System.out.println("Task File Creation Ends");

		}

	}
	

	private static void fileCreate(Amalthea model, String path1, String path2, int  configFlag)
			throws IOException {
		boolean preemptionFlag=false; 
		if(0x0020 == (0x00F0 & configFlag)){
			preemptionFlag=true; 
		}else{
			preemptionFlag=false; 
		}
		EList<Task> tasks = model.getSwModel().getTasks();

		String fname = path1 + File.separator + "taskDef.c";
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
			taskFileHeader(f1);
			//TODO Message read and write is pending 
			if((0x3000 == (0xF000 & configFlag))&(0x0100 != (0x0F00 & configFlag))) {
				headerIncludesTaskHeadRMS(f1);
		//		TaskCounter(f1, tasks);
				TaskDefinitionRMS(f1, tasks, preemptionFlag);
			}else {
				headerIncludesTaskHead(f1);
				TaskCounter(f1, tasks);
				TaskDefinition(f1, tasks, preemptionFlag);
			}
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fname2 = path1 + File.separator + "taskDef.h";
		File f4 = new File(path1);
		File f3 = new File(fname2);
		f4.mkdirs();
		try {
			f1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fn1 = f3;
		FileWriter fw1 = new FileWriter(fn1, true);
		try {
			fileUtil.fileMainHeader(f3);
			taskFileHeader(f3);
			if((0x3000 == (0xF000 & configFlag))&(0x0100 != (0x0F00 & configFlag))) {
				headerIncludesTaskRMSHead(f3);
			}else {
				headerIncludesTask(f3);
			}
			mainStaticTaskDef(f3, tasks);
			
		} finally {
			try {
				fw1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void TaskCounter(File f3, EList<Task> tasks) {
		try {
			File fn = f3;
			FileWriter fw = new FileWriter(fn, true); // the true will append the new data
			fw.write("/* Task Counter Declaration. */\n");
			for(Task ta:tasks) {
				fw.write("int taskCount"+ta.getName()+"\t=\t0;\n");
			}
			fw.write("\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());		
	}}


	private static void fileCreatePthread(Amalthea model, String path1, String path2, int  configFlag)
			throws IOException {
		boolean preemptionFlag=false; 
		if(0x0020 == (0x00F0 & configFlag)){
			preemptionFlag=true; 
		}else{
			preemptionFlag=false; 
		}
		EList<Task> tasks = model.getSwModel().getTasks();

		String fname = path1 + File.separator + "taskDef.c";
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
			taskFileHeader(f1);
			headerIncludesTaskPthreadHead(f1);
			TaskDefinitionPthread(f1, tasks, model, preemptionFlag);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fname2 = path1 + File.separator + "taskDef.h";
		File f4 = new File(path1);
		File f3 = new File(fname2);
		f4.mkdirs();
		try {
			f1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fn1 = f3;
		FileWriter fw1 = new FileWriter(fn1, true);
		try {
			fileUtil.fileMainHeader(f3);
			taskFileHeader(f3);
			headerIncludesPthreadTask(f3);
			mainStaticTaskPthreadDef(f3, tasks);
		} finally {
			try {
				fw1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void headerIncludesPthreadTask(File f3) {
		try {
			File fn = f3;
			FileWriter fw = new FileWriter(fn, true); // the true will append the new data
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("#include <stdint.h>\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void mainStaticTaskPthreadDef(File f3, EList<Task> tasks) {
		try {
			File fn = f3;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Static definition of the tasks. */\n");
			for (Task task : tasks) {
				fw.write("void v" + task.getName() + "( void *t );\n");
			}
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}
	
	
	private static void taskCounterRMS(File f3, EList<Task> tasks) {
		try {
			File fn = f3;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Static definition of the tasks. */\n");
			for (Task task : tasks) {
				fw.write("void v" + task.getName() + "( void *t );\n");
			}
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void TaskDefinitionPthread(File f1, EList<Task> tasks, Amalthea model, boolean preemptionFlag) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			for (Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				ArrayList<Label> labellist1 = new ArrayList<Label>();
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + ";\n");
				}
				fw.write("\n\n\n");
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\n\n\n");

				fw.write("\n\tvoid cIN_" + task.getName() + "()\n\t{\n");
				fw.write("\t\tvDisplayMessagePthread( \" Cin Execution\t" + task.getName() + "\\n\" );\n");

				for (Label lab : listWithoutDuplicates2) {
					fw.write("\t\t" + lab.getName() + "_" + task.getName() + "\t=\t" + lab.getName() + ";\n");
				}
				fw.write("\t}\n");

				fw.write("\n\tvoid cOUT_" + task.getName() + "()\n\t{\n");
				fw.write("\t\tvDisplayMessagePthread(\" Cout Execution\t" + task.getName() + "\\n\\n\" );\n");
				ArrayList<Label> labellist2 = new ArrayList<Label>();
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getWriteLabelSet(run, null);
					labellist2.addAll(labellist);
				}
				List<Label> listWithoutDuplicates1 = labellist2.stream().distinct().collect(Collectors.toList());

				for (Label lab : listWithoutDuplicates1) {
					fw.write("\t\t" + lab.getName() + "\t=\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\t}\n");

				fw.write("\n\tvoid v" + task.getName() + "( void *t )" + "\n\t{\n");
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
						//	System.out.println("key  "+p +"==>   Value"+count);
					}

					ProcessingUnit pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();

					Long coreID = CoreMap.get(pu);
					fw.write("\t\tcpu_set_t cpuset;\n");
					fw.write("\t\tint cpu = " + coreID + ";\n");
					fw.write("\t\tCPU_ZERO(&cpuset);\n");
					fw.write("\t\tCPU_SET( cpu , &cpuset);\n");
					Set<ProcessingUnit> procUniSet = DeploymentUtil.getAssignedCoreForProcess(task, model);
					List<ProcessingUnit> procUniList = null;
					//ProcessingUnit procUni = null;
					if (procUniSet != null) {
						procUniList = new ArrayList<ProcessingUnit>(procUniSet);
					}
					if (procUniList != null) {
						procUniList.get(0);
					}
					fw.write("\t\tsched_setaffinity(0, sizeof(cpuset), &cpuset);\n\n");
				}

				fw.write("\t\tfor( ;; )\n\t\t{\n");
				fw.write("\t\t\tsuspendMe ();\n");
				fw.write("\t\tprint_affinity();\n");
				fw.write("\t\t\tvDisplayMessagePthread( \"" + task.getName() + " is running\\r\\n\" );\n");
				fw.write("\t\t\t/*Cin - Create local variables and copy the actual variable to them */\n");
				fw.write("\t\t\tcIN_" + task.getName() + "();\n");
				if (preemptionFlag == true) {
					fw.write("\t\t\tresumeMe ();\n");
				}
				fw.write("\n\t\t\t/*Runnable calls */\n");
				for (Runnable run : runnablesOfTask) {
					fw.write("\t\t\t" + run.getName() + "();\n");
				}
				fw.write("\n\t\t\t/*Cout - Write back the local variables back to the actual variables */\n");
				if (preemptionFlag == true) {
					fw.write("\t\t\tsuspendMe ();\n");
				}
				fw.write("\t\t\tcOUT_" + task.getName() + "();\n");
				fw.write("\t\t\tresumeMe ();\n");
				fw.write("\t\t\tsleepTimerMs(DELAY_MULT*NULL);\n");
				fw.write("\t\t\tpthread_exit((void*) t);\n");
				Time tasktime = fileUtil.getRecurrence(task);
				if (tasktime != null) {
					TimeUtil.getAsTimeUnit(fileUtil.getRecurrence(task), null);

				}
				fw.write("\t\t}\n");
				fw.write("\t}\n\n");

			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void taskFileHeader(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Task Definition\n");
			fw.write("*Description	:	Task Definition with Task Structure\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void headerIncludesTaskPthreadHead(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("#define _GNU_SOURCE\n\n");
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <pthread.h>\n");
			fw.write("#include <sched.h>\n");
			fw.write("#include <stdint.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"runnable.h\"\n");
			fw.write("#include \"taskDef.h\"\n\n");
			fw.write("#define DELAY_MULT 100\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}
	
	private static void headerIncludesTaskRMSHead(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("#ifndef DEMO_PARALLELLA_TASKCODE_H_\n");
			fw.write("#define DEMO_PARALLELLA_TASKCODE_H_\n\n");
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <pthread.h>\n");
			fw.write("#include <sched.h>\n");
			fw.write("#include <stdint.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n");
			fw.write("#include \"queue.h\"\n");
			fw.write("#include \"ParallellaUtils.h\"\n");
			fw.write("#include \"debugFlags.h\"\n");
			fw.write("#include \"task.h\"\n");
		//	fw.write("#include \"taskDef.h\"\n");
			fw.write("#include \"runnable.h\"\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void headerIncludesTask(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("#include <stdint.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n\n");
			fw.write("#include \"queue.h\"\n");
			fw.write("#include \"croutine.h\"\n");
			//fw.write("#include \"partest.h\"\n");
			fw.write("#include \"runnable.h\"\n");
			fw.write("#include \"taskDef.h\"\n");
			fw.write("#include \"task.h\"\n");

			//fw.write("#define DELAY_MULT 100\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void headerIncludesTaskHead(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("#include <stdint.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n");
			fw.write("#include \"queue.h\"\n");
			fw.write("#include \"croutine.h\"\n");
		//	fw.write("#include \"partest.h\"\n");
			fw.write("#include \"task.h\"\n");
			fw.write("#include \"runnable.h\"\n\n");
			//fw.write("#define DELAY_MULT 100\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}
	
	private static void headerIncludesTaskHeadRMS(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("#include \"taskDef.h\"\n\n");
			//fw.write("#define DELAY_MULT 100\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}
	
	

	private static void TaskDefinition(File f1, EList<Task> tasks, boolean preemptionFlag) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			int taskCount = 0;
			for (Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				ArrayList<Label> labellist1 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					// long init = fileUtil.intialisation(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + ";\n");
				}
				fw.write("\n\n\n");
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					// long init = fileUtil.intialisation(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\n\n\n");

				fw.write("\n\tvoid cIN_" + task.getName() + "()\n\t{\n");
			//	fw.write("\t\tvDisplayMessage( \" Cin Execution\t" + task.getName() + "\\n\" );\n");

				for (Label lab : listWithoutDuplicates2) {
					// String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t\t" + lab.getName() + "_" + task.getName() + "\t=\t" + lab.getName() + ";\n");
				}
				fw.write("\t}\n");

				fw.write("\n\tvoid cOUT_" + task.getName() + "()\n\t{\n");
			//	fw.write("\t\tvDisplayMessage(\" Cout Execution\t" + task.getName() + "\\n\\n\" );\n");
				ArrayList<Label> labellist2 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getWriteLabelSet(run, null);
					labellist2.addAll(labellist);
				}
				List<Label> listWithoutDuplicates1 = labellist2.stream().distinct().collect(Collectors.toList());

				for (Label lab : listWithoutDuplicates1) {
					fw.write("\t\t" + lab.getName() + "\t=\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\t}\n");

				fw.write("\n\tvoid v" + task.getName() + "( void *pvParameters )" + "\n\t{\n");
			//	fw.write("\t\tconst char *pcTaskName = \"" + task.getName() + " is running\\r\\n\";\n");
			//	fw.write("\t\tportTickType xLastWakeTime;\n\n");
			//	fw.write("\t\tfor( ;; )\n\t\t{\n");
			//	fw.write("\t\t\t/*Cin - Create local variables and copy the actual variable to them */\n");
			//	fw.write("\t\t\ttaskENTER_CRITICAL ();\n");
			//	fw.write("\t\t\tvDisplayMessage( pcTaskName );\n");
			//	fw.write("\t\t\tcIN_" + task.getName() + "();\n");
			//	if (preemptionFlag == true) {
			//		fw.write("\t\t\ttaskEXIT_CRITICAL ();\n");
			//	}
			fw.write("\n\tupdateDebugFlag(700);");
			fw.write("\n\t/*Runnable calls */\n");
			for (Runnable run : runnablesOfTask) {
				fw.write("\t\t" + run.getName() + "();\n");
			}
			fw.write("\n\ttaskCount"+task.getName()+"++;");
			fw.write("\n\ttraceTaskPasses("+taskCount+", taskCount"+task.getName()+");");
			fw.write("\n\ttraceRunningTask(0);\n");
			
			//	fw.write("\n\t\t\t/*Cout - Write back the local variables back to the actual variables */\n");
			//	if (preemptionFlag == true) {
			//		fw.write("\t\t\ttaskENTER_CRITICAL ();\n");
			//	}
			//	fw.write("\t\t\tcOUT_" + task.getName() + "();\n");
			//	fw.write("\t\t\ttaskEXIT_CRITICAL ();\n");
			//	fw.write("\t\t\ttaskYIELD();\n");
				Time tasktime = fileUtil.getRecurrence(task);
				double sleepTime = 0;
				if (tasktime != null) {
					sleepTime = TimeUtil.getAsTimeUnit(fileUtil.getRecurrence(task), null);

				}
			//	fw.write("\t\t\tvTaskDelayUntil(&xLastWakeTime, " + sleepTime + "*DELAY_MULT);\n");
			//	fw.write("\t\t}\n");
				fw.write("\t}\n\n");
				taskCount++;
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static void mainStaticTaskDef(File f1, EList<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Static definition of the tasks. */\n");
			for (Task task : tasks) {
				fw.write("void v" + task.getName() + "( void *pvParameters );\n");
			}
			fw.write("\n");
			for (Task task : tasks) {
				fw.write("void cOUT_" + task.getName() + "();\n");
			}
			fw.write("\n");
			for (Task task : tasks) {
				fw.write("void cIN_" + task.getName() + "();\n");
			}
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	private static void TaskDefinitionRMS(File f1, EList<Task> tasks, boolean preemptionFlag) {
		try {
			File fn = f1;
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
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					// long init = fileUtil.intialisation(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + ";\n");
				}
				fw.write("\n\n\n");
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					// long init = fileUtil.intialisation(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\n\n\n");

				fw.write("\n\tvoid cIN_" + task.getName() + "()\n\t{\n");
				fw.write("\t\tvDisplayMessage( \" Cin Execution\t" + task.getName() + "\\n\" );\n");

				for (Label lab : listWithoutDuplicates2) {
					// String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t\t" + lab.getName() + "_" + task.getName() + "\t=\t" + lab.getName() + ";\n");
				}
				fw.write("\t}\n");

				fw.write("\n\tvoid cOUT_" + task.getName() + "()\n\t{\n");
				fw.write("\t\tvDisplayMessage(\" Cout Execution\t" + task.getName() + "\\n\\n\" );\n");
				ArrayList<Label> labellist2 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getWriteLabelSet(run, null);
					labellist2.addAll(labellist);
				}
				List<Label> listWithoutDuplicates1 = labellist2.stream().distinct().collect(Collectors.toList());

				for (Label lab : listWithoutDuplicates1) {
					fw.write("\t\t" + lab.getName() + "\t=\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\t}\n");

				fw.write("\n\tvoid v" + task.getName() + "( void *pvParameters )" + "\n\t{\n");
				fw.write("\t\tconst char *pcTaskName = \"" + task.getName() + " is running\\r\\n\";\n");
				fw.write("\t\tportTickType xLastWakeTime;\n\n");
				fw.write("\t\tfor( ;; )\n\t\t{\n");
				fw.write("\t\ttraceTaskStatus(1,1);\n");
				fw.write("\t\t\t/*Cin - Create local variables and copy the actual variable to them */\n");
				fw.write("\t\t\ttaskENTER_CRITICAL ();\n");
				fw.write("\t\t\tvDisplayMessage( pcTaskName );\n");
				fw.write("\t\t\tcIN_" + task.getName() + "();\n");
				if (preemptionFlag == true) {
					fw.write("\t\t\ttaskEXIT_CRITICAL ();\n");
				}
				fw.write("\n\t\t\t/*Runnable calls */\n");
				for (Runnable run : runnablesOfTask) {
					fw.write("\t\t\t" + run.getName() + "();\n");
				}
				fw.write("\n\t\t\t/*Cout - Write back the local variables back to the actual variables */\n");
				if (preemptionFlag == true) {
					fw.write("\t\t\ttaskENTER_CRITICAL ();\n");
				}
				fw.write("\t\t\tcOUT_" + task.getName() + "();\n");
				fw.write("\t\t\ttaskEXIT_CRITICAL ();\n");
				fw.write("\t\ttraceTaskStatus(0,1);\n");
				//	fw.write("\t\t\ttaskYIELD();\n");
				Time tasktime = fileUtil.getRecurrence(task);
				double sleepTime = 0;
				if (tasktime != null) {
					sleepTime = TimeUtil.getAsTimeUnit(fileUtil.getRecurrence(task), null);
				}
				fw.write("\t\t\tvTaskDelayUntil(&xLastWakeTime, " + sleepTime + "*DELAY_MULT" +tasktime +");\n");
				fw.write("\t\t}\n");
				fw.write("\t}\n\n");

			}
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
