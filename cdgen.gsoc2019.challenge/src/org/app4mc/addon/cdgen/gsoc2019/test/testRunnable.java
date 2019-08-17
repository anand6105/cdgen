package org.app4mc.addon.cdgen.gsoc2019.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
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


	public class testRunnable
	{
		public testRunnable(final Amalthea Model, String path1, int configFlag, int k, List<Task> tasks) throws IOException {
			this.model = Model;
			boolean pthreadFlag = false;
			if (0x2000 != (configFlag & 0xF000)) {
				pthreadFlag = false;
				fileTestRunnable(model, path1, k, tasks);
			} else{
				pthreadFlag = true;
				fileTestRunnablePthread(model, path1, k, tasks);
			}
		}
	
	
		private void fileTestRunnable(Amalthea model2, String path1, int k, List<Task> tasks) throws IOException {
			String fname = path1 + File.separator + "runnable"+k+".c";
			List<Runnable> runmod = new ArrayList<Runnable>();
			for(Task ta:tasks) {
				runmod.addAll(SoftwareUtil.getRunnableList(ta, null));
			}
			
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
					/*if (word.equals(input)) {
						count++;
					}*/
					if (word.equals(input1)) {
						count1++;
					}
				}
			}
			/*if (count != runmod.size()) {
				System.out.println(runmod.size() + "\tRunnable"+k+" : ERROR: Runnable size Not matching count\t" + count);
			} else {
				System.out.println("Runnable : Number of Runnables OK");
			}*/
			if (count1 == runmod.size()) {
				System.out.println("Runnable"+k+" : Number of Runnables  void OK");
			} else {
				
				System.out.println(runmod.size() + "\tRunnable"+k+" : ERROR: Runnable VOID Not matching count\t" + count1);
			}
			fr.close();
		}
	
		private void fileTestRunnablePthread(Amalthea model2, String path1, int k, List<Task> tasks) throws IOException {
			String fname = path1 + File.separator + "runnable"+k+".c";
			List<Runnable> runmod = new ArrayList<Runnable>();
			for(Task ta:tasks) {
				runmod.addAll(SoftwareUtil.getRunnableList(ta, null));
			}
			
			File f1 = new File(fname);
			String[] words = null;
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			String s;		
			boolean flagRun = false;
			String input = "\tRunnable", input1 = "void";
			int count = 0, count1 = 0;
			while ((s = br.readLine()) != null) {
				words = s.split(" ");
				for (String word : words) {
					if(flagRun) {
						//System.out.println("\nRunnable "+word);
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
				System.out.println(runmod.size() + "\tRunnable"+k+" : ERROR: Runnable size Not matching count\t" + count);
			} else {
				System.out.println("Runnable"+k+" : Number of Runnables OK");
			}
			if (count1 != runmod.size()) {
				System.out.println(runmod.size() + "\tRunnable"+k+" : ERROR: Runnable VOID Not matching count\t" + count1);
			} else {
				System.out.println("Runnable"+k+" : Number of Runnables  void OK");
			}
			fr.close();
		}
	

	final private Amalthea model;

	
}
