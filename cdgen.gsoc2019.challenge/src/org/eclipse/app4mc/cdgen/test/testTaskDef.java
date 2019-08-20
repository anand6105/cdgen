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

import java.io.*;
import java.util.List;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 *
 */

public class testTaskDef

{


		public testTaskDef(final Amalthea model, String path1, int configFlag, int k, List<Task> tasks) throws IOException {

			boolean pthreadFlag = false;
			if (0x2000 != (configFlag & 0xF000)) {
				pthreadFlag = false;
				fileTestTask(model, path1, configFlag, k, tasks);
			} else{
				pthreadFlag = true;
				fileTestTaskPthread(model, path1, configFlag, k, tasks);
			}
		}
	
		/**
		 * 
		 * @param model2
		 * @param path1
		 * @param configFlag
		 * @throws IOException
		 */
	private void fileTestTask(Amalthea model2, String path1, int configFlag, int k,List<Task> taskmod) throws IOException {
		String fname = path1 + File.separator + "taskDef"+k+".c";
	//	EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		//TODO 
		String input = "Cout", input2 = "Cin", input3 = "\t\t\ttaskENTER_CRITICAL", input4 = "\t\t\ttaskEXIT_CRITICAL";
		String input5 = "\tvoid";
		int count = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(input)) {
					count++;
				} else if (word.equals(input2)) {
					count1++;
				} else if (word.equals(input3)) {
					count2++;
				} else if (word.equals(input4)) {
					count3++;
				} else if (word.equals(input5)) {
					count4++;
				}
			}
		}
	if (0x1000 == (configFlag & 0xF000)) {
		if (count != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask"+k+" : ERROR: Cout count\t" + count);
		} else {
			System.out.println("Task"+k+" : Cout count OK");
		}
		if (count1 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask"+k+" : ERROR: Cin count\t" + count1);
		} else {
			System.out.println("Task"+k+" : Cin count OK");
		}
		//if(0x0020 == (configFlag & 0x00F0)) {
			if (count2 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask"+k+" : ERROR: taskENTER_CRITICAL\t" + count2);
			} else {
				System.out.println("Task"+k+" : taskENTER_CRITICAL OK");
			}
	/*	else {
			if (count2 != taskmod.size()) {
				System.out.println(taskmod.size() * 2 + "\tTask"+k+" : ERROR: taskENTER_CRITICAL\t" + count2);
			} else {
				System.out.println("Task"+k+" : taskENTER_CRITICAL OK");
			}
		}*/
	//	if(0x0020 == (configFlag & 0x00F0)) {
			if (count3 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask"+k+" : ERROR: taskEXIT_CRITICAL\t" + count2);
			} else {
				System.out.println("Task"+k+" : taskEXIT_CRITICAL OK");
			}
	/*	else {
			if (count3 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask"+k+" : ERROR: taskEXIT_CRITICAL\t" + count2);
			} else {
				System.out.println("Task"+k+" : taskEXIT_CRITICAL OK");
			}*/
		
		if (count3 != count2) {
			System.out.println(taskmod.size() + "\tTask"+k+" : ERROR: taskEXIT_CRITICAL\t" + count2);
		} else {
			System.out.println("Task"+k+" : taskEXIT_CRITICAL and taskENTER_CRITICAL OK");
		}
	}
		if (count4 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask"+k+" : ERROR: taskEXIT_CRITICAL\t" + count4);
		} else {
			System.out.println("Task"+k+" : Task Count OK");
		}

		fr.close();
	}

	private void fileTestTaskPthread(Amalthea model2, String path1, int configFlag, int k, List<Task> taskmod) throws IOException {
		String fname = path1 + File.separator + "taskDef"+k+".c";
		//EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		String input1 = "Cout", input2 = "Cin", input3 = "\t\t\tsuspendMe", input4 = "\t\t\tresumeMe", input5 = "void";
		int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(input1)) {
					count1++;
				} else if (word.equals(input2)) {
					count2++;
				} else if (word.equals(input3)) {
					count3++;
				} else if (word.equals(input4)) {
					count4++;
				} else if (word.equals(input5)) {
					count5++;
				}
			}
		}
		if(0x3110 == (configFlag & 0xFFF0)) {
		if (count1 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask"+k+" : ERROR: Cout count\t" + count1);
		} else {
			System.out.println("Task"+k+" : Cout count OK");
		}
		if (count2 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask"+k+" : ERROR: Cin count\t" + count2);
		} else {
			System.out.println("Task"+k+" : Cin count OK");
		}

		//if(0x0020 == (configFlag & 0x00F0)){
			if (count3 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask"+k+" : ERROR: suspendMe\t" + count3);
			} else {
				System.out.println("Task"+k+" : suspendMe OK");
			}
	/*	}else {
			if (count3 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask"+k+" : ERROR: suspendMe\t" + count3);
			} else {
				System.out.println("Task"+k+" : suspendMe OK");
			}
		}*/
		
		//if(0x0020 == (configFlag & 0x00F0)) {
			if (count4 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask"+k+" : ERROR: resumeMe\t" + count4);
			} else {
				System.out.println("Task"+k+" : resumeMe OK");
			}
		/*}else {
			if (count4 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask"+k+" : ERROR: resumeMe\t" + count4);
			} else {
				System.out.println("Task"+k+" : resumeMe OK");
			}
		}*/
		
		if (count4 != count3) {
			System.out.println(count4 + "\tTask"+k+" : ERROR: suspendMe \t" + count3);
		} else {
			System.out.println("Task"+k+" : suspendMe and resumeMe OK");
		}
		if (count5 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask"+k+" : ERROR: taskEXIT_CRITICAL\t" + count5);
		} else {
			System.out.println("Task"+k+" : Task Count OK");
		}
		}
		fr.close();
	}

	
}
