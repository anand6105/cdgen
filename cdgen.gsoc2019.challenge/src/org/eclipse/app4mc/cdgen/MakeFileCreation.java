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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Main function in which scheduling is done.
 *
 * @author Ram Prasath Govindarajan
 *
 */

public class MakeFileCreation {
	final private Amalthea model;

	public MakeFileCreation(final Amalthea Model, final String path1, final int configFlag) throws IOException {
		this.model = Model;
		System.out.println("MAKEFILE Creation Begins");
		fileCreate(this.model, path1, configFlag);
		System.out.println("MAKEFILE Creation Ends");
	}

	private static void fileCreate(final Amalthea model, final String path1, final int configFlag) throws IOException {
		model.getMappingModel().getTaskAllocation();
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			System.out.println("Core ==> " + pu);
			final Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
			final String fname = path1 + File.separator + "Makefile";
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
			final FileWriter fw = new FileWriter(fn, true);
			try {
				//fileUtil.fileMainHeader(f1);
				makeFileHeader(f1);
				headerIncludesMainRMS(f1,CoreNo);
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


	private static void makeFileHeader(final File f1) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("#******************************************************************\n");
			fw.write("#*Title 		:   Makefile Setup\n");
			fw.write("#*Description	:	Makefile Setup for the Scheduler \n");
			fw.write("#******************************************************************\n");
			fw.write("#******************************************************************\n");
			fw.write("#******************************************************************\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesMainRMS(final File f1, EList<SchedulerAllocation> coreNo) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("EPIPHANY_HOME=/opt/adapteva/esdk\n");
			fw.write("#host compiler path\n");
			fw.write("LCC=/opt/gcc-linaro-7.2.1-2017.11-x86_64_arm-linux-gnueabihf/bin/arm-linux-gnueabihf-gcc\n");
			fw.write("#device compiler path\n");
			fw.write("CC=e-gcc\n");
			fw.write("#FreeRTOS dependencies\n");
			fw.write("CFLAGS=-I.\n");
			fw.write("FREERTOSSRC=../../Source\n");
			fw.write("INCLUDES= -g -I$(FREERTOSSRC)/include -I$(FREERTOSSRC)/portable/GCC/Epiphany -I.\n");
			fw.write("DEPS = $(FREERTOSSRC)/portable/GCC/Epiphany/");
			fw.write("portmacro.h ");
			fw.write("Makefile ");
			fw.write("FreeRTOSConfig.h ");
			fw.write("debugFlags.h ");
			fw.write("AmaltheaConverter.h ");
			int coreIndex = 0;
			for(SchedulerAllocation core:coreNo) {
				fw.write("taskDef"+coreIndex+".h ");
			}
			coreIndex = 0;
			for(SchedulerAllocation core:coreNo) {
				fw.write("label"+coreIndex+".h ");
			}
			coreIndex = 0;
			for(SchedulerAllocation core:coreNo) {
				fw.write("runnable"+coreIndex+".h ");
			}
			fw.write("ParallellaUtils.h \n");
			fw.write("#Epiphany SDK dependencies\n");
			fw.write("ESDK=${EPIPHANY_HOME} \n");
			fw.write("ELIBS=${ESDK}/tools/host.armv7l/lib \n");
			fw.write("EINCS=${ESDK}/tools/host.armv7l/include \n");
			fw.write("ELDF=${ESDK}/bsps/current/fast.ldf \n");
			fw.write("EHDF=${EPIPHANY_HDF} \n");
			fw.write("#search paths for C source code files \n");
			fw.write("vpath %.c .:$(FREERTOSSRC)/:$(FREERTOSSRC)/portable/MemMang:$(FREERTOSSRC)/portable/GCC/Epiphany:/ \n");
			fw.write("#search path for assembly listings \n");
			fw.write("vpath %.s $(FREERTOSSRC)/portable/GCC/Epiphany \n");
			fw.write("#main target  \n");
			fw.write("#run: armcode \n");
			coreIndex = 0;
			for(SchedulerAllocation core:coreNo) {
				fw.write("main"+coreIndex+".elf ");
			}
			fw.write("\n	@echo build status : successful\n\n");
			fw.write("#rule for every device target\n");
			fw.write("%.elf: $(ELDF) tasks.o queue.o list.o port.o portasm.o heap_1.o c2c.o debugFlags.o AmaltheaConverter.o  \n");
			coreIndex = 0;
			for(SchedulerAllocation core:coreNo) {
				fw.write("taskDef"+coreIndex+".o ");
			}
			coreIndex = 0;
			for(SchedulerAllocation core:coreNo) {
				fw.write("label"+coreIndex+".o ");
			}
			coreIndex = 0;
			for(SchedulerAllocation core:coreNo) {
				fw.write("runnable"+coreIndex+".o ");
			}

			fw.write("ParallellaUtils.o shared_comms.o %.o  \n");
			fw.write("	$(CC) -g -T$< -Wl,--gc-sections -o $@ $(filter-out $<,$^) -le-lib\n\n");
			fw.write("#host target\n");	
			fw.write("armcode: armcode.c $(DEPS)\n");	
			fw.write("	$(LCC) $< -o $@  -I ${EINCS} -L ${ELIBS} -lpal -le-hal -le-loader -lpthread\n");	
			fw.write("#clean target\n");	
			fw.write("clean\n");	
			fw.write("	rm -f *.o *.srec *.elf armcode\n\n");	
			fw.write(".SECONDARY:\n");
			fw.write("%.o: %.c $(DEPS)\n");
			fw.write("	$(CC) -fdata-sections -ffunction-sections  -c -o $@ $< $(INCLUDES)\n\n");
			fw.write("%.o: %.s $(DEPS)\n");
			fw.write("	$(CC) -c -o $@ $< $(INCLUDES)\n");
			fw.close();
		}
		catch (final IOException ioe) {
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
