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

public class testMain

{

	public testMain(Amalthea model, String path1, int configFlag) throws IOException {
		if ((0x1000 == (configFlag & 0xF000))) {
			fileTestmainTask(model, path1);
		} else if(0x2000 == (configFlag & 0xF000)){
			fileTestmainTaskPthread(model, path1);
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

