package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
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

public class MakeFileCreation {
	final private Amalthea model;

	public MakeFileCreation(final Amalthea Model, String path1, int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Main File Creation Begins");
		fileCreate(model, path1, configFlag);
		System.out.println("Main File Creation Ends");
	}

	private static void fileCreate(Amalthea model, String path1, int configFlag) throws IOException {
		model.getMappingModel().getTaskAllocation();
		EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k=0;
		for(SchedulerAllocation c:CoreNo) {
			ProcessingUnit pu = c.getResponsibility().get(0);
			System.out.println("Core ==> "+pu);
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
					BigInteger sleepTime = taskTime.getValue();
					fw.write("\tAmaltheaTask AmalTk_"+task.getName()+" = createAmaltheaTask( v"+task.getName()+", cIN_" + task.getName() +", cOUT_" + task.getName()+", "+task.getStimuli().get(0).getName()+", "+task.getStimuli().get(0).getName()+", "+sleepTime+");\n");
				}
			}

			int count =0;
			for (Task task : tasks) {
				fw.write("\txTaskCreate(generalizedRTOSTask , \"AmalTk_"+task.getName()+"\", configMINIMAL_STACK_SIZE, &AmalTk_"+task.getName()
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
