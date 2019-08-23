/*******************************************************************************
 *   Copyright (c) 2019 Dortmund University of Applied Sciences and Arts and others.
 *
 *   This program and the accompanying materials are made
 *   available under the terms of the Eclipse Public License 2.0
 *   which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 *   SPDX-License-Identifier: EPL-2.0
 *
 *   Contributors:
 *       Dortmund University of Applied Sciences and Arts - initial API and implementation
 *******************************************************************************/
package org.eclipse.app4mc.cdgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Main function in which scheduling is done.
 *
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
	public MainFileCreation(final Amalthea Model, final String srcPath, final int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Main File Creation Begins");
		if (0x2000 == (configFlag & 0xF000)) {
			fileCreatePthread(this.model, srcPath);
		}
		System.out.println("Main File Creation Ends");
	}


	/**
	 * Shared Label definition and initialization structure.
	 *
	 * @param file
	 * @param labellist
	 */
	public static List<Label> SharedLabelCoreDefinition(final Amalthea model, final String srcPath) {
		final EList<Label> labellist = model.getSwModel().getLabels();
		final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
		if (labellist.size() == 0) {
			System.out.println("Shared Label size 0");
		}
		else {
			final HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation
					.LabelTaskMap(model, labellist);
			int i = 0, k = 0, j = 0;
			for (final Label share : labellist) {
				final HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
				final Collection<ProcessingUnit> puList = TaskMap.values();
				final List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
				final Set<Task> TaskList = TaskMap.keySet();
				if (puListUnique.size() == 1 && TaskList.size() > 1) {
					SharedLabelListSortCore.add(share);
					i++;
				}
				else if (puListUnique.size() > 1) {
					j++;
				}
				else if (TaskList.size() == 1) {
					k++;
				}
			}
			System.out.println("Total Labels :" + sharedLabelTaskMap.keySet().size() + "=" + i + "+" + j + "+" + k + "="
					+ (i + j + k));
		}
		return SharedLabelListSortCore;
	}


	private static void sleepTimerMsPthread(final File f1) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
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
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void fileCreatePthread(final Amalthea model, final String path1) throws IOException {
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			final Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
			final String fname = path1 + File.separator + "main" + k + ".c";
			final File f2 = new File(path1);
			final File f1 = new File(fname);
			f2.mkdirs();
			try {
				f1.createNewFile();
			}
			catch (final IOException e) {
				e.printStackTrace();
			}

			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			try {
				fileUtil.fileMainHeader(f1);
				mainFileHeader(f1);
				headerIncludesMainPthread(f1, model, k, tasks);
				sleepTimerMsPthread(f1);
				mainFucntionPthread(f1, tasks);
			}
			finally {
				try {
					fw.close();
				}
				catch (final IOException e) {
					e.printStackTrace();
				}
			}
			k++;
		}
	}

	private static void mainFucntionPthread(final File f1, final Set<Task> tasks) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\tpthread_t thread[NUM_THREADS];\n");
			fw.write("\tpthread_attr_t attr;\n\n");
			final int tasksize = tasks.size();
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
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesMainPthread(final File f1, final Amalthea model2, final int k,
			final Set<Task> tasks) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <pthread.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"taskDef" + k + ".h\"\n");
			fw.write("#define NUM_THREADS\t" + tasks.size() + "\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void mainFileHeader(final File f1) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   C File for Tasks Call\n");
			fw.write("*Description	:	Main file in which scheduling is done \n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}
}
