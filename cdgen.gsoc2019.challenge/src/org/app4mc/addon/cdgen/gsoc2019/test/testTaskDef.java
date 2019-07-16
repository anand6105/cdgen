package org.app4mc.addon.cdgen.gsoc2019.test;

import java.io.*;

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

public class testTaskDef

{


		public testTaskDef(final Amalthea model, String path1, int configFlag) throws IOException {

			boolean pthreadFlag = false;
			if (0x2000 != (configFlag & 0xF000)) {
				pthreadFlag = false;
				fileTestTask(model, path1, configFlag);
			} else{
				pthreadFlag = true;
				fileTestTaskPthread(model, path1, configFlag);
			}
		}
	
		/**
		 * 
		 * @param model2
		 * @param path1
		 * @param configFlag
		 * @throws IOException
		 */
	private void fileTestTask(Amalthea model2, String path1, int configFlag) throws IOException {
		String fname = path1 + File.separator + "taskDef.c";
		EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		//TODO 
		String input = "Cout", input2 = "Cin", input3 = "\t\t\ttaskENTER_CRITICAL", input4 = "\t\t\ttaskEXIT_CRITICAL";
		String input5 = "void";
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
		if (count != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask : ERROR: Cout count\t" + count);
		} else {
			System.out.println("Task : Cout count OK");
		}
		if (count1 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask : ERROR: Cin count\t" + count1);
		} else {
			System.out.println("Task : Cin count OK");
		}
		if(0x0020 == (configFlag & 0x00F0)) {
			if (count2 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: taskENTER_CRITICAL\t" + count2);
			} else {
				System.out.println("Task : taskENTER_CRITICAL OK");
			}
		}else {
			if (count2 != taskmod.size()) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: taskENTER_CRITICAL\t" + count2);
			} else {
				System.out.println("Task : taskENTER_CRITICAL OK");
			}
		}
		if(0x0020 == (configFlag & 0x00F0)) {
			if (count3 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: taskEXIT_CRITICAL\t" + count2);
			} else {
				System.out.println("Task : taskEXIT_CRITICAL OK");
			}
		}else {
			if (count3 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask : ERROR: taskEXIT_CRITICAL\t" + count2);
			} else {
				System.out.println("Task : taskEXIT_CRITICAL OK");
			}
		}
		if (count3 != count2) {
			System.out.println(taskmod.size() + "\tTask : ERROR: taskEXIT_CRITICAL\t" + count2);
		} else {
			System.out.println("Task : taskEXIT_CRITICAL and taskENTER_CRITICAL OK");
		}
		if (count4 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask : ERROR: taskEXIT_CRITICAL\t" + count4);
		} else {
			System.out.println("Task : Task Count OK");
		}

		fr.close();
	}

	private void fileTestTaskPthread(Amalthea model2, String path1, int configFlag) throws IOException {
		String fname = path1 + File.separator + "taskDef.c";
		EList<Task> taskmod = model2.getSwModel().getTasks();
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
		if (count1 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask : ERROR: Cout count\t" + count1);
		} else {
			System.out.println("Task : Cout count OK");
		}
		if (count2 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask : ERROR: Cin count\t" + count2);
		} else {
			System.out.println("Task : Cin count OK");
		}

		if(0x0020 == (configFlag & 0x00F0)){
			if (count3 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: suspendMe\t" + count3);
			} else {
				System.out.println("Task : suspendMe OK");
			}
		}else {
			if (count3 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask : ERROR: suspendMe\t" + count3);
			} else {
				System.out.println("Task : suspendMe OK");
			}
		}
		if(0x0020 == (configFlag & 0x00F0)) {
			if (count4 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: resumeMe\t" + count4);
			} else {
				System.out.println("Task : resumeMe OK");
			}
		}else {
			if (count4 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask : ERROR: resumeMe\t" + count4);
			} else {
				System.out.println("Task : resumeMe OK");
			}
		}
		if (count4 != count3) {
			System.out.println(count4 + "\tTask : ERROR: suspendMe \t" + count3);
		} else {
			System.out.println("Task : suspendMe and resumeMe OK");
		}
		if (count5 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask : ERROR: taskEXIT_CRITICAL\t" + count5);
		} else {
			System.out.println("Task : Task Count OK");
		}

		fr.close();
	}

	
}
