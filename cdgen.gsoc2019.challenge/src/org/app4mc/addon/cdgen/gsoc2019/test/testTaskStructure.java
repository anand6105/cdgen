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
package org.app4mc.addon.cdgen.gsoc2019.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class testTaskStructure

{

		final private Amalthea model;
	
		public testTaskStructure(final Amalthea Model, String path1, int configFlag) throws IOException {
			this.model = Model;
			System.out.println("############################################################");
			System.out.println("\t\t\tTest Begins");
			System.out.println("############################################################");
			EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
			int k=0;
			for(SchedulerAllocation c:CoreNo) {
				ProcessingUnit pu = c.getResponsibility().get(0);
				Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, model);
				List<Task> tasks = new ArrayList<Task>(task);
			new testRunnable(model, path1,configFlag, k, tasks);
			new	testTaskDef(model, path1, configFlag, k, tasks);
			new	testMain(model, path1,configFlag, k, tasks);
		//	new testLabel(model, path1, k, tasks);
			k++;
			}
			System.out.println("############################################################");
			System.out.println("\t\t\tTest Ends");
			System.out.println("############################################################");
		}
}
	
