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
				fw.write("int main(){\n");
				fw.write("\tunsigned int shared_label_to_read;\n");
				fw.write("\tunsigned   row_loop,col_loop;\n");
				fw.write("\te_platform_t epiphany;\n");
				fw.write("\te_epiphany_t dev;\n");
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
				ArrayList<String> result = new ArrayList<String>();
				for(int i=0; i<localPU.size();i++) {
					for(int j=0; j<localPU.size();j++) {
						if(k<localPU.size()) {
							fw.write("\te_return_stat_t\tresult"+k+";\n");
							k++;
						}
					}
				}
				int k3=0;
				for(int i=0; i<localPU.size();i++) {
					for(int j=0; j<localPU.size();j++) {
						if(k3<localPU.size()) {
							fw.write("\tunsigned int message"+k3+"[9];\n");
							k3++;
						}
					}
				}
				int k1=0;
				for(int i=0; i<localPU.size();i++) {
					for(int j=0; j<localPU.size();j++) {
						if(k1<localPU.size()) {
							fw.write("\tresult"+k1+"=  e_load(\"main"+k1+".elf\",&dev,"+i+","+j+",E_FALSE);\n");
							result.add("result"+k1+"!=E_OK");
							k1++;
						}
					}
				}
				String resultFinal = "";
				for(int k2=0; k2<result.size();k2++) {
					System.out.println("Size ==> "+result.size());
					resultFinal = resultFinal + (result.get(k2) + "||");
					if(k2==(result.size()-2)) {
						k2++;
						resultFinal = resultFinal+result.get(k2);
						break;
					}
				}
				int k4=0;
				for(int i=0; i<localPU.size();i++) {
					for(int j=0; j<localPU.size();j++) {
						if(k4<localPU.size()) {
							if(k4==0)
							fw.write("\tif (result"+k4+"!=E_OK){\n");
							else
							fw.write("\telse if (result"+k4+"!=E_OK){\n");
							fw.write("\t\tfprintf(stderr,\"Error Loading the Epiphany Application "+k4+" %i\\n\", result"+k4+");");
							fw.write("\n\t}\n");
							k4++;
						}
					}
				}
				fw.write("\te_start_group(&dev);\n");
				fw.write("\tfprintf(stderr,\"RMS Multicore on FreeRTOS started \\n\");\n");
				fw.write("\taddr = cnt_address;\n");
				fw.write("\tint pollLoopCounter = 0;\n");
				fw.write("\tint taskMessage;\n");
				fw.write("\tint prevtaskMessage;\n");
				fw.write("\tint prevpollLoopCounter = 0;\n");
				fw.write("\tfor (pollLoopCounter=0;pollLoopCounter<=40;pollLoopCounter++){\n");
				int k2=0;
				for(int i=0; i<localPU.size();i++) {
					for(int j=0; j<localPU.size();j++) {
						if(k2<localPU.size()) {
							fw.write("\t\te_read(&dev,"+i+","+j+",addr, &message"+k2+", sizeof(message"+k2+"));\n");
							fw.write("\t\tfprintf(stderr, \"tick1 %3d||\",message"+k2+"[8]+1);\n");
							fw.write("\t\tfprintf(stderr,\"task holding core"+k2+" %2u||\", message"+k2+"[6]);\n");
							k2++;
						}
					}
				}
				fw.write("\t\tfprintf(stderr,\"\\n\");\n");				
				fw.write("\t\tusleep(READ_PRECISION_US);\n");
				fw.write("\t}\n");
				fw.write("\tfprintf(stderr,\"----------------------------------------------\\n\");\n");
				fw.write("\te_close(&dev);\n");
				fw.write("\te_finalize();\n");
				fw.write("\tfprintf(stderr,\"RMS Multicore on FreeRTOS Complete \\n \");\n");
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
			fw.write("#include \"debugFlags.h\"\n");
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
