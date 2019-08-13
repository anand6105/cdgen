package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.DeploymentUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil.TimeType;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.TimeUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Runnable Definition with Runnable specific delay.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

// TODO Merger of Runnable Definition

public class RunFileCreation {
	final private Amalthea model;

	/**
	 * Constructor RunFileCreation
	 *
	 * @param Model
	 * Amalthea Model
	 * @param srcPath
	 * @param pthreadFlag
	 * @throws IOException
	 */
	public RunFileCreation(final Amalthea Model, String srcPath, String path2, int  configFlag) throws IOException {
		this.model = Model;
		EList<Runnable> runnables = model.getSwModel().getRunnables();
		System.out.println("Runnable File Creation Begins");
		fileCreate(model, srcPath, path2, configFlag, runnables);
		System.out.println("Runnable File Creation Ends");

	}


	/**
	 * Runnable File Creation
	 * 
	 * @param model
	 * @param srcPath
	 * @param path2
	 * @param configFlag
	 * @param tasks
	 * @param runnables
	 * @throws IOException
	 */
	private static void fileCreate(Amalthea model, String srcPath, String path2, int configFlag,
		EList<Runnable> runnables) throws IOException {
		EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k=0;
		for(SchedulerAllocation c:CoreNo) {
			ProcessingUnit pu = c.getResponsibility().get(0);
			Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, model);
			List<Task> tasks = new ArrayList<Task>(task);
		String fname = srcPath + File.separator + "runnable"+k+".c";
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
			if (0x2000 == (configFlag & 0xF000)) {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesRun(f1,k);
				runnablePthreadDefinition(f1, tasks, model);
			} else {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesRun(f1,k);
				runnableDefinition(f1, tasks, model);
			}

		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fname2 = srcPath + File.separator + "runnable"+k+".h";
		File f4 = new File(srcPath);
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
			if (0x2000 == (configFlag & 0xF000)) {
				fileUtil.fileMainHeader(f3);
				runFileHeader(f3);
				headerIncludesRunPthreadHead(f3);
				runnableDeclaration(f3, runnables);
			} else {
				fileUtil.fileMainHeader(f3);
				runFileHeader(f3);
				headerIncludesRunHead(f3);
				runnableDeclaration(f3, runnables);
			}

		} finally {
			try {
				fw1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		k++;
		}
	}

	/**
	 * Title Card for RunFileCreation
	 * 
	 * @param file
	 */
	private static void runFileHeader(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Runnable Header\n");
			fw.write("*Description	:	Runnable Definition with Runnable delay\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Header inclusion for the Runnable.c file
	 * 
	 * @param file
	 */
	private static void headerIncludesRun(File file, int k) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"runnable"+k+".h\"\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * RunFileCreation - Header inclusion for runnable.h
	 * 
	 * @param file
	 */
	private static void headerIncludesRunHead(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			//	fw.write("#include \"runnable.h\"\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * RunFileCreation - Header inclusion for runnable.h pthread specific
	 * 
	 * @param file
	 */
	private static void headerIncludesRunPthreadHead(File file) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * RunFileCreation - Runnable Definition Pthread specific
	 * 
	 * @param file
	 * @param tasks
	 * @param model
	 */
	private static void runnablePthreadDefinition(File file, List<Task> tasks, Amalthea model) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			//	int taskCounter =1;
			for (Task t : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(t, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				//	int runnableCounter =1;
				for (Runnable Run : runnablesOfTask) {
					fw.write("void " + Run.getName() + " (void)\t{\n");
					/*	fw.write("\tvDisplayMessagePthread(\" " + t.getName() + " \tRunnable Execution	" + "\t" + Run.getName()
					+ "\\n\");\n");*/
					Process RunTaskName = SoftwareUtil.getProcesses(Run, null).get(0);
					Set<ProcessingUnit> pu = DeploymentUtil.getAssignedCoreForProcess(RunTaskName, model);
					if (pu != null) {
						for (ProcessingUnit p : pu) {
							Time RunTime1 = RuntimeUtil.getExecutionTimeForRunnable(Run, p, null, TimeType.WCET);
							RunTime1 = TimeUtil.convertToTimeUnit(RunTime1, TimeUnit.US);

							double sleepTime = TimeUtil.getAsTimeUnit(RunTime1, null);

							fw.write("\tusleep(" + sleepTime + ");\n");
							break;
						}
					}
					fw.write("}\n");
					//runnableCounter++;
				}
				//taskCounter++;
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Runnable Definition(Generic)
	 * 
	 * @param file
	 * @param tasks
	 * @param model
	 */
	private static void runnableDefinition(File file, List<Task> tasks, Amalthea model) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			int taskCounter =1;
			for (Task t : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(t, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				int runnableCounter =1;
				for (Runnable Run : runnablesOfTask) {
					fw.write("void " + Run.getName() + " \t(void)\t{\n");
					/*fw.write("\tvDisplayMessage(\" " + t.getName() + " \tRunnable Execution	" + "\t" + Run.getName()
					+ "\\n\");\n");*/
					Process RunTaskName = SoftwareUtil.getProcesses(Run, null).get(0);
					Set<ProcessingUnit> pu = DeploymentUtil.getAssignedCoreForProcess(RunTaskName, model);
					for (ProcessingUnit p : pu) {
						Time RunTime1 = RuntimeUtil.getExecutionTimeForRunnable(Run, p, null, TimeType.WCET);
						RunTime1 = TimeUtil.convertToTimeUnit(RunTime1, TimeUnit.US);
						//double sleepTime = TimeUtil.getAsTimeUnit(RunTime1, null);
						long sleepTime = RunTime1.getValue().longValue();
						//			System.out.println("Time w/ unit: "+RunTime1 + ",time after: "+sleepTime);
						//	fw.write("\tusleep(" + sleepTime + ");\n");
					//	fw.write("\tsleepTimerUs(" + sleepTime + ", "+taskCounter+runnableCounter+");\n");

						break;
					}
					fw.write("}\n");
					runnableCounter++;
				}
				taskCounter++;
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Runnable Declaration in runnable.h
	 * 
	 * @param file
	 * @param runnables
	 */
	private static void runnableDeclaration(File file, List<Runnable> runnables) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			for (Runnable Run : runnables) {
				fw.write("void " + Run.getName() + " (void);\n");
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
