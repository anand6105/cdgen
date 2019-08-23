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
package org.eclipse.app4mc.cdgen.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 *
 */

public class testTaskStructure

{
	final private Amalthea model;

	public testTaskStructure(final Amalthea Model, final String path1, final int configFlag) throws IOException {
		this.model = Model;
		System.out.println("############################################################");
		System.out.println("\t\t\tTest Begins");
		System.out.println("############################################################");
		final EList<SchedulerAllocation> CoreNo = this.model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			final Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, this.model);
			final List<Task> tasks = new ArrayList<Task>(task);
			new testRunnable(this.model, path1, configFlag, k, tasks);
			new testTaskDef(this.model, path1, configFlag, k, tasks);
			new testMain(this.model, path1, configFlag, k, tasks);
			// new testLabel(model, path1, k, tasks);
			k++;
		}
		System.out.println("############################################################");
		System.out.println("\t\t\tTest Ends");
		System.out.println("############################################################");
	}
}

