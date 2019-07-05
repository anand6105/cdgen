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
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.TimeUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil.TimeType;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
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
	 *            Amalthea Model
	 * @param path1
	 * @param pthreadFlag
	 * @throws IOException
	 */
	public RunFileCreation(final Amalthea Model, String path1, String path2, boolean pthreadFlag) throws IOException {
		this.model = Model;

		EList<Task> tasks = model.getSwModel().getTasks();
		EList<Runnable> runnables = model.getSwModel().getRunnables();

		System.out.println("Runnable File Creation Begins");
		fileCreate(model, path1, path2, pthreadFlag, tasks, runnables);
		System.out.println("Runnable File Creation Ends");

	}

	private static void fileCreate(Amalthea model, String path1, String path2, boolean pthreadFlag, EList<Task> tasks,
			EList<Runnable> runnables) throws IOException {
		String fname = path1 + File.separator + "runnable.c";
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
			if (pthreadFlag == false) {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesRun(f1);
				runnableDefinition(f1, tasks, model);
			} else {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesRun(f1);
				runnablePthreadDefinition(f1, tasks, model);
			}

		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fname2 = path2 + File.separator + "runnable.h";
		File f4 = new File(path2);
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
			if (pthreadFlag == false) {
				fileUtil.fileMainHeader(f3);
				runFileHeader(f3);
				headerIncludesRunHead(f3);
				runnableDeclaration(f3, runnables);
			} else {
				fileUtil.fileMainHeader(f3);
				runFileHeader(f3);
				headerIncludesRunPthreadHead(f3);
				runnableDeclaration(f3, runnables);
			}

		} finally {
			try {
				fw1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void runFileHeader(File f1) {
		try {
			File fn = f1;
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

	private static void headerIncludesRun(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"runnable.h\"\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void headerIncludesRunHead(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void headerIncludesRunPthreadHead(File f1) {
		try {
			File fn = f1;
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

	private static void runnablePthreadDefinition(File f1, EList<Task> tasks, Amalthea model) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			for (Task t : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(t, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable Run : runnablesOfTask) {
					fw.write("void " + Run.getName() + "(void)\t{\n");
					fw.write("\tvDisplayMessagePthread(\" " + t.getName() + " \tRunnable Execution	" + "\t" + Run.getName()
					+ "\\n\");\n");
			
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
					} /*else {
						fw.write("\tusleep(10000);\n");
					}*/
					fw.write("}\n");
				}
				
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void runnableDefinition(File f1, EList<Task> tasks, Amalthea model) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			for (Task t : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(t, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable Run : runnablesOfTask) {
					fw.write("void " + Run.getName() + " (void)\t{\n");
					fw.write("\tvDisplayMessage(\" " + t.getName() + " \tRunnable Execution	" + "\t" + Run.getName()
							+ "\\n\");\n");
					Process RunTaskName = SoftwareUtil.getProcesses(Run, null).get(0);
				/*	if (RunTaskName == null) {
					//	System.out.println("RunTaskName is NULL!!!");
					}
				//	System.out.println(RunTaskName.getName());
*/					Set<ProcessingUnit> pu = DeploymentUtil.getAssignedCoreForProcess(RunTaskName, model);
				//	if (pu != null) {
						for (ProcessingUnit p : pu) {
							Time RunTime1 = RuntimeUtil.getExecutionTimeForRunnable(Run, p, null, TimeType.WCET);
							RunTime1 = TimeUtil.convertToTimeUnit(RunTime1, TimeUnit.US);
							double sleepTime = TimeUtil.getAsTimeUnit(RunTime1, null);
							fw.write("\tusleep(" + sleepTime + ");\n");

							break;
						}
						//}
					fw.write("}\n");

				}

			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void runnableDeclaration(File f1, EList<Runnable> runnables) {
		try {
			File fn = f1;
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
