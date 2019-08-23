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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 *
 */

public class testLabel

{

	public testLabel(final Amalthea Model, final String path1, final int k, final List<Task> taskmod)
			throws IOException {
		fileTestLabel(Model, path1, k, taskmod);
	}

	private void fileTestLabel(final Amalthea model2, final String path1, final int k, final List<Task> tasks)
			throws IOException {
		final String fname = path1 + File.separator + "label" + k + ".c";
		// EList<Label> label = model2.getSwModel().getLabels();
		final List<Label> label = new ArrayList<Label>();
		for (final Task ta : tasks) {
			label.addAll(SoftwareUtil.getAccessedLabelSet(ta, null));
		}
		final File f1 = new File(fname);
		String[] words = null;
		@SuppressWarnings("resource")
		final FileReader fr = new FileReader(f1);
		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(fr);
		String s;
		final String input = "\tuint", input1 = "\t=\t";
		int count = 0, count1 = 0;
		int Counter = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (final String word : words) {
				if (word.contains(input)) {
					count++;
				}
				if (word.equals(input1)) {
					count1++;
				}
			}
		}
		for (final Task task : tasks) {
			List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
			final ArrayList<Label> labellist1 = new ArrayList<Label>();
			runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
			for (final Runnable run : runnablesOfTask) {
				final Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
				labellist1.addAll(labellist);
			}
			final List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
			Counter = Counter + listWithoutDuplicates2.size();
		}
		if (count != label.size() + Counter) {
			System.out.println((label.size() + Counter) + "\tLabel : ERROR: Label2 count\t" + count);
		}
		else {
			System.out.println("Label : Label2 count OK");
		}
		if (count1 != label.size()) {
			System.out.println(label.size() + "\tLabel : ERROR: Label count\t" + count1);
		}
		else {
			System.out.println("Label : Label1 count OK");
		}
		fr.close();
	}

}
