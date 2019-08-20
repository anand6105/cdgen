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

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 *
 */


public class testRunnable {
	public testRunnable(final Amalthea Model, final String path1, final int configFlag, final int k,
			final List<Task> tasks) throws IOException {
		this.model = Model;
		boolean pthreadFlag = false;
		if (0x2000 != (configFlag & 0xF000)) {
			pthreadFlag = false;
			fileTestRunnable(this.model, path1, k, tasks);
		}
		else {
			pthreadFlag = true;
			fileTestRunnablePthread(this.model, path1, k, tasks);
		}
	}


	private void fileTestRunnable(final Amalthea model2, final String path1, final int k, final List<Task> tasks)
			throws IOException {
		final String fname = path1 + File.separator + "runnable" + k + ".c";
		final List<Runnable> runmod = new ArrayList<Runnable>();
		for (final Task ta : tasks) {
			runmod.addAll(SoftwareUtil.getRunnableList(ta, null));
		}

		final File f1 = new File(fname);
		String[] words = null;
		final FileReader fr = new FileReader(f1);
		final BufferedReader br = new BufferedReader(fr);
		String s;
		final String input = "\tRunnable", input1 = "void";
		final int count = 0;
		int count1 = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (final String word : words) {
				/*
				 * if (word.equals(input)) { count++; }
				 */
				if (word.equals(input1)) {
					count1++;
				}
			}
		}
		/*
		 * if (count != runmod.size()) { System.out.println(runmod.size() +
		 * "\tRunnable"+k+" : ERROR: Runnable size Not matching count\t" +
		 * count); } else {
		 * System.out.println("Runnable : Number of Runnables OK"); }
		 */
		if (count1 == runmod.size()) {
			System.out.println("Runnable" + k + " : Number of Runnables  void OK");
		}
		else {

			System.out.println(
					runmod.size() + "\tRunnable" + k + " : ERROR: Runnable VOID Not matching count\t" + count1);
		}
		fr.close();
	}

	private void fileTestRunnablePthread(final Amalthea model2, final String path1, final int k, final List<Task> tasks)
			throws IOException {
		final String fname = path1 + File.separator + "runnable" + k + ".c";
		final List<Runnable> runmod = new ArrayList<Runnable>();
		for (final Task ta : tasks) {
			runmod.addAll(SoftwareUtil.getRunnableList(ta, null));
		}

		final File f1 = new File(fname);
		String[] words = null;
		final FileReader fr = new FileReader(f1);
		final BufferedReader br = new BufferedReader(fr);
		String s;
		boolean flagRun = false;
		final String input = "\tRunnable", input1 = "void";
		int count = 0, count1 = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (final String word : words) {
				if (flagRun) {
					// System.out.println("\nRunnable "+word);
					flagRun = false;
				}
				if (word.equals(input)) {
					count++;
				}
				if (word.equals(input1)) {
					count1++;
					flagRun = true;
				}
			}
		}
		if (count != runmod.size()) {
			System.out
					.println(runmod.size() + "\tRunnable" + k + " : ERROR: Runnable size Not matching count\t" + count);
		}
		else {
			System.out.println("Runnable" + k + " : Number of Runnables OK");
		}
		if (count1 != runmod.size()) {
			System.out.println(
					runmod.size() + "\tRunnable" + k + " : ERROR: Runnable VOID Not matching count\t" + count1);
		}
		else {
			System.out.println("Runnable" + k + " : Number of Runnables  void OK");
		}
		fr.close();
	}


	final private Amalthea model;


}
