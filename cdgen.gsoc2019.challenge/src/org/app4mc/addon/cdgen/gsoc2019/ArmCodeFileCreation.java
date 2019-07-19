package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.DeploymentUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.HardwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.RuntimeUtil.TimeType;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.TimeUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.MappingModel;
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

public class ArmCodeFileCreation {
	final private Amalthea model;

	/**
	 * Constructor RunFileCreation
	 *
	 * @param Model
	 * Amalthea Model
	 * @param path1
	 * @param pthreadFlag
	 * @throws IOException
	 */
	public ArmCodeFileCreation(final Amalthea Model, String path1, String path2, int  configFlag) throws IOException {
		this.model = Model;
		EList<Task> tasks = model.getSwModel().getTasks();
		EList<Runnable> runnables = model.getSwModel().getRunnables();
		System.out.println("Runnable File Creation Begins");
		fileCreate(model, path1, path2, configFlag, tasks, runnables);
		System.out.println("Runnable File Creation Ends");

	}


	private static void fileCreate(Amalthea model, String path1, String path2, int configFlag, EList<Task> tasks,
			EList<Runnable> runnables) throws IOException {
		String fname = path1 + File.separator + "armcode.c";
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
			if (0x3110 == (configFlag & 0xFFF0)) {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesArmCode(f1);
				nsleep(f1);
				zynqmain(model, f1);
			} else {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesArmCode(f1);
				nsleep(f1);
				zynqmain(model, f1);
			}

		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void runFileHeader(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   ArmCode Header\n");
			fw.write("*Description	:	Header file for Deploy/Offloading of the task to EPI\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void nsleep(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("int nsleep(long miliseconds){\n");
			fw.write("\tstruct timespec req, rem;\n");
			fw.write("\tif(miliseconds > 999){\n");
			fw.write("\t\treq.tv_sec = (int)(miliseconds / 1000);\n");
			fw.write("\t\treq.tv_nsec = (miliseconds - ((long)req.tv_sec * 1000)) * 1000000;\n");
			fw.write("\t} else {\n");
			fw.write("\t\treq.tv_sec = 0;\n");
			fw.write("\t\treq.tv_nsec = miliseconds * 1000000;\n");
			fw.write("\t}\n");
			fw.write("\treturn nanosleep(&req , &rem);\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void zynqmain(Amalthea model, File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			MappingModel mappingModel = model.getMappingModel();
			if (mappingModel != null) {
				EList<SchedulerAllocation> processingUnits = model.getMappingModel().getSchedulerAllocation();
				ArrayList<SchedulerAllocation> localPU = new ArrayList<SchedulerAllocation>();
				localPU.addAll(processingUnits);
				HashMap<SchedulerAllocation,Long> CoreMap = new HashMap<SchedulerAllocation,Long>();
				long count = 0;
				for (SchedulerAllocation pu: localPU) {
					CoreMap.put(pu, count);	
					count++;
				}
				/*	ProcessingUnit pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
				Long coreID = CoreMap.get(pu);*/

				fw.write("int main(){\n");
				fw.write("\tunsigned int shared_label_to_read;\n");
				fw.write("\tunsigned   row_loop,col_loop;\n");
				fw.write("\te_platform_t epiphany;\n");
				fw.write("\te_epiphany_t dev;\n");
				fw.write("\te_return_stat_t result;\n");
				fw.write("\tunsigned int message[9];\n");
				fw.write("\tunsigned int message2[9];\n");
				fw.write("\tint loop;\n");
				fw.write("\tint addr;\n");
				fw.write("\te_mem_t emem;\n");
				fw.write("\te_init(NULL);\n");
				fw.write("\te_reset_system();\n");
				fw.write("\te_get_platform_info(&epiphany);\n");

				if(processingUnits.size() == 1) {
					fw.write("\te_open(&dev,0,0,1,1);\n");
				}else if(processingUnits.size() > 1 && processingUnits.size() < 5) {
					fw.write("\te_open(&dev,0,0,2,2);\n");
				}else if(processingUnits.size() > 4 && processingUnits.size() < 10) {
					fw.write("\te_open(&dev,0,0,3,3);\n");
				}else if(processingUnits.size() > 9 && processingUnits.size() < 17) {
					fw.write("\te_open(&dev,0,0,4,4);\n");
				}


				fw.write("\te_reset_group(&dev);\n");
				int k=0;
				String resultCheck[] = null;
				
				//for(SchedulerAllocation proc:localPU) {
					//fw.write("\te_return_stat_t result"+count3+";\n");
					//System.out.println(" processingUnits.size()==>"+p.size());
					for(int i=0; i<localPU.size();i++) {
						for(int j=0; j<localPU.size();j++) {
							if(k<localPU.size()) {
								fw.write("\tresult"+k+"=  e_load(\"main"+k+".elf\",&dev,"+i+","+j+",E_FALSE);\n");
								//resultCheck[k] = (String)("result"+k);
								k++;
							}
						}
					}
					/*String conditionstatement = null;
					for(int m=0; m<k;m++) {
						conditionstatement = conditionstatement + resultCheck[m];
						if((m+1)<k) {
							conditionstatement = conditionstatement + "||";
						}
					}*/
				/*	resultCheck = resultCheck+ "result"+count3+" != E_OK ||";
					count3++;
				*/


				fw.write("\tif ("+resultCheck+"){\n");
			//	fw.write("\tif (result1 != E_OK || result2 != E_OK){\n");
				fw.write("\t\tfprintf(stderr,\"Error Loading the Epiphany Application 1 %i\\n\", result);");
				fw.write("\t}\n");
				fw.write("\te_start_group(&dev);\n");
				fw.write("\tfprintf(stderr,\"FreeRTOS started \\n\");\n");
				fw.write("\taddr = cnt_address;\n");
				fw.write("\tint pollLoopCounter = 0;\n");
				fw.write("\tint taskMessage;\n");
				fw.write("\tint prevtaskMessage;\n");
				fw.write("\tint prevpollLoopCounter = 0;\n");
				fw.write("\tfor (pollLoopCounter=0;pollLoopCounter<=40;pollLoopCounter++){\n");
				fw.write("\t\tfprintf(stderr,\"\\n\");\n");
				fw.write("\t\tusleep(READ_PRECISION_US);\n");

				fw.write("\t}\n");
				fw.write("\tfprintf(stderr,\"----------------------------------------------\\n\");\n");
				fw.write("\te_close(&dev);\n");
				fw.write("\te_finalize();\n");
				fw.write("\tfprintf(stderr,\"demo complete \\n \");\n");
				fw.write("\treturn 0;\n");
				fw.write("}\n");
				fw.close();
			}} catch (IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
			}
	}


	private static void headerIncludesArmCode(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <unistd.h>\n");
			fw.write("#include <e-hal.h>\n");
			fw.write("#include <time.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"runnable.h\"\n");
			fw.write("#include \"debugFlags.h\"\n");
	//		fw.write("#include \"debugFlags.h\"\n");
			fw.write("#define READ_PRECISION_US 1000\n\n\n");
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
