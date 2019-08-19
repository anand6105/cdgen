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

import java.io.*;
import java.util.List;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class testMain

{

	public testMain(Amalthea model, String path1, int configFlag, int k, List<Task> taskmod) throws IOException {
		if ((0x1000 == (configFlag & 0xF000))) {
			fileTestmainTask(model, path1, k, taskmod);
		} else if(0x2000 == (configFlag & 0xF000)){
			fileTestmainTaskPthread(model, path1, k, taskmod);
		}
	}

	private void fileTestmainTaskPthread(Amalthea model2, String path1, int k, List<Task> taskmod) throws IOException {
		String fname = path1 + File.separator + "main"+k+".c";
		//EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		String input = "\t\tpthread_create";
		int count = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(input)) {
					count++;
				}
			}
		}
		if (count != taskmod.size()) {
			System.out.println(taskmod.size() + "\tMain"+k+" : ERROR: Task create count\t" + count);
		} else {
			System.out.println("Main"+k+" : Task create count OK");
		}
		fr.close();
	}

	private void fileTestmainTask(Amalthea model2, String path1, int k, List<Task> taskmod) throws IOException {
		String fname = path1 + File.separator + "main"+k+".c";
		//EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		String input = "\txTaskCreate(";
		String input2 = "configMINIMAL_STACK_SIZE,";
		int count = 0, count1 = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(input)) {
					count++;
				} else if (word.equals(input2)) {
					count1++;
				}
			}
		}
		if (count != taskmod.size()) {
			System.out.println(taskmod.size() + "\tMain"+k+" : ERROR: Task create count\t" + count);
		} else {
			System.out.println("Main"+k+" : Task create count OK");
		}
		if (count1 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tMain"+k+" : ERROR: Task Priorities count\t" + count1);
		} else {
			System.out.println("Main"+k+" : Task Priorities count OK");
		}
		fr.close();
	}
}

