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

public class testTaskStructure

{

	final private Amalthea model;

	public testTaskStructure(final Amalthea Model, String path1, boolean pthreadFlag, boolean preemptionFlag) throws IOException {
		this.model = Model;
		if (pthreadFlag == false) {
			System.out.println("############################################################");
			System.out.println("Test Begins");
			fileTestRunnable(model, path1);
			fileTestTask(model, path1, preemptionFlag);
			fileTestmainTask(model, path1);
			System.out.println("Test Sucessful");
			System.out.println("############################################################");
		} else if (pthreadFlag != false) {
			System.out.println("Test Begins");
			fileTestRunnablePthread(model, path1);
			fileTestTaskPthread(model, path1, preemptionFlag);
			fileTestmainTaskPthread(model, path1);
			System.out.println("Test Sucessful");
		}
	}

	private void fileTestmainTaskPthread(Amalthea model2, String path1) throws IOException {
		String fname = path1 + File.separator + "main.c";
		EList<Task> taskmod = model2.getSwModel().getTasks();
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
			System.out.println(taskmod.size() + "\tMain : ERROR: Task create count\t" + count);
		} else {
			System.out.println("Main : Task create count OK");
		}
		fr.close();
	}

	private void fileTestRunnable(Amalthea model2, String path1) throws IOException {
		String fname = path1 + File.separator + "runnable.c";
		EList<Runnable> runmod = model2.getSwModel().getRunnables();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		String input = "\tRunnable", input1 = "void";
		int count = 0, count1 = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(input)) {
					count++;
				}
				if (word.equals(input1)) {
					count1++;
				}
			}
		}
		if (count != runmod.size()) {
			System.out.println(runmod.size() + "\tRunnable : ERROR: Runnable size Not matching count\t" + count);
		} else {
			System.out.println("Runnable : Number of Runnables checked");
		}
		if (count1 != runmod.size()) {
			System.out.println(runmod.size() + "\tRunnable : ERROR: Runnable VOID Not matching count\t" + count1);
		} else {
			System.out.println("Runnable : Number of Runnables  void checked");
		}

		fr.close();
	}

	private void fileTestRunnablePthread(Amalthea model2, String path1) throws IOException {
		String fname = path1 + File.separator + "runnable.c";
		EList<Runnable> runmod = model2.getSwModel().getRunnables();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		String input = "Runnable", input1 = "void";
		int count = 0, count1 = 0;
		while ((s = br.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(input)) {
					count++;
				}
				if (word.equals(input1)) {
					count1++;
				}
			}
		}
		if (count != runmod.size() + 3) {
			System.out.println(runmod.size() + "\tRunnable : ERROR: Runnable size Not matching count\t" + count);
		} else {
			System.out.println("Runnable : Number of Runnables checked");
		}
		if (count1 != runmod.size()) {
			System.out.println(runmod.size() + "\tRunnable : ERROR: Runnable VOID Not matching count\t" + count1);
		} else {
			System.out.println("Runnable : Number of Runnables  void checked");
		}

		fr.close();
	}

	private void fileTestTask(Amalthea model2, String path1, boolean preemptionFlag) throws IOException {
		String fname = path1 + File.separator + "taskDef.c";
		EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
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
		if(preemptionFlag == true) {
			if (count2 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: taskENTER_CRITICAL\t" + count2);
			} else {
				System.out.println("Task : taskENTER_CRITICAL OK");
			}
		}else if(preemptionFlag == false) {
			if (count2 != taskmod.size()) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: taskENTER_CRITICAL\t" + count2);
			} else {
				System.out.println("Task : taskENTER_CRITICAL OK");
			}
		}
		if(preemptionFlag == true) {
			if (count3 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: taskEXIT_CRITICAL\t" + count2);
			} else {
				System.out.println("Task : taskEXIT_CRITICAL OK");
			}
		}else if(preemptionFlag == false) {
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

	private void fileTestTaskPthread(Amalthea model2, String path1, boolean preemptionFlag) throws IOException {
		String fname = path1 + File.separator + "taskDef.c";
		EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		String input = "Cout", input2 = "Cin", input3 = "\t\t\tsuspendMe", input4 = "\t\t\tresumeMe";
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

		if(preemptionFlag == true) {
			if (count3 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: suspendMe\t" + count2);
			} else {
				System.out.println("Task : suspendMe OK");
			}
		}else if(preemptionFlag == false) {
			if (count3 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask : ERROR: suspendMe\t" + count2);
			} else {
				System.out.println("Task : suspendMe OK");
			}
		}
		if(preemptionFlag == true) {
			if (count3 != taskmod.size() * 2) {
				System.out.println(taskmod.size() * 2 + "\tTask : ERROR: resumeMe\t" + count2);
			} else {
				System.out.println("Task : resumeMe OK");
			}
		}else if(preemptionFlag == false) {
			if (count3 != taskmod.size()) {
				System.out.println(taskmod.size()+ "\tTask : ERROR: resumeMe\t" + count2);
			} else {
				System.out.println("Task : resumeMe OK");
			}
		}
		if (count3 != count2) {
			System.out.println(taskmod.size() + "\tTask : ERROR: suspendMe \t" + count2);
		} else {
			System.out.println("Task : suspendMe and resumeMe OK");
		}
		if (count4 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tTask : ERROR: taskEXIT_CRITICAL\t" + count4);
		} else {
			System.out.println("Task : Task Count OK");
		}

		fr.close();
	}

	private void fileTestmainTask(Amalthea model2, String path1) throws IOException {
		String fname = path1 + File.separator + "main.c";
		EList<Task> taskmod = model2.getSwModel().getTasks();
		File f1 = new File(fname);
		String[] words = null;
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		String s;
		String input = "\txTaskCreate(";
		String input2 = "tskIDLE_PRIORITY";
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
			System.out.println(taskmod.size() + "\tMain : ERROR: Task create count\t" + count);
		} else {
			System.out.println("Main : Task create count OK");
		}
		if (count1 != taskmod.size()) {
			System.out.println(taskmod.size() + "\tMain : ERROR: Task Priorities count\t" + count1);
		} else {
			System.out.println("Main : Task Priorities count OK");
		}
		fr.close();
	}
}
