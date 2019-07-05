package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.ModelUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.HwStructure;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskAllocation;
import org.eclipse.app4mc.amalthea.model.Time;
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
	 * Constructor MainFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param path1
	 * @param pthreadFlag
	 * @param i
	 * @throws IOException
	 */
	public MainFileCreation(final Amalthea Model, String path1, boolean pthreadFlag) throws IOException {
		this.model = Model;
		if (pthreadFlag == false) {
			System.out.println("Main File Creation Begins");
			fileCreate(model, path1);
			System.out.println("Main File Creation Ends");
		} else if (pthreadFlag != false) {
			System.out.println("Main File Creation Begins");
			fileCreatePthread(model, path1);
			System.out.println("Main File Creation Ends");
		}
	}

	private static void fileCreate(Amalthea model, String path1) throws IOException {
		EList<Task> tasks = model.getSwModel().getTasks();

		String fname = path1 + File.separator + "main.c";
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
			headerIncludesMain(f1);
			sleepTimerMs(f1);
			mainTaskPriority(f1, tasks);
			//mainFucntion(f1, tasks);
			mainFucntionMulticore(model, f1, tasks);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		EList<Task> tasks = model.getSwModel().getTasks();

		String fname = path1 + File.separator + "main.c";
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
	}

	private static void mainFucntionPthread(File f1, EList<Task> tasks) {
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
				fw.write("\t\tpthread_create (&thread[" + init1 + "], &attr, v" + tasks.get(init1).getName()
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
					EList<SchedulerAllocation> PU = mappingModel.getSchedulerAllocation();
					ArrayList<SchedulerAllocation> localPU = new ArrayList<SchedulerAllocation>();
					localPU.addAll(PU);

					HashMap<SchedulerAllocation,Long> CoreMap = new HashMap<SchedulerAllocation,Long>();
					long count = 0;
					for (SchedulerAllocation p: PU) {
							CoreMap.put(p, count);	
							count++;
							System.out.println("key  "+p +"==>   Value"+count);
					}
					ProcessingUnit pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
					
					Long coreID = CoreMap.get(pu);
						fw.write("\txTaskCreate("+coreID+", v" + task.getName() + ", \"" + task.getName().toUpperCase()
								+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
						
						
					}

					//Map<Task, Long> CoreMapSorted = fileUtil.sortByValue(CoreMap);
					ProcessingUnit pu = DeploymentUtil.getTaskAllocations(task, model).get(0).getAffinity().get(0);
					if(pu != null) {
						fw.write("\txTaskCreate("+pu.getName()+", v" + task.getName() + ", \"" + task.getName().toUpperCase()
							+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
					}else {
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

	/**
	 * helper function to get the Amalthea Model
	 * 
	 */
	public Amalthea getModel() {
		return this.model;
	}

}
