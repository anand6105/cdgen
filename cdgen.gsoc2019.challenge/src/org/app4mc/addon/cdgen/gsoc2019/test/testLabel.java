package org.app4mc.addon.cdgen.gsoc2019.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class testLabel

{

		public testLabel(final Amalthea Model, String path1, int k, List<Task> taskmod) throws IOException {
			fileTestLabel(Model, path1, k, taskmod);
		}
	
		private void fileTestLabel(Amalthea model2, String path1, int k, List<Task> tasks) throws IOException {
			String fname = path1 + File.separator + "label"+k+".c";
			//EList<Label> label = model2.getSwModel().getLabels();
			List<Label> label = new ArrayList<Label>();
			for(Task ta:tasks) {
				label.addAll(SoftwareUtil.getAccessedLabelSet(ta, null));
			}
			File f1 = new File(fname);
			String[] words = null;
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			String s;
			String input = "\tuint", input1 = "\t=\t";
			int count = 0,count1 = 0;
			int Counter = 0;
			while ((s = br.readLine()) != null) {
			words = s.split(" ");
				for (String word : words) {
					if (word.contains(input)) {
						count++;
					}
					if (word.equals(input1)) {
						count1 ++;
					}
				}
			}
			for (Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				ArrayList<Label> labellist1 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				Counter = Counter + listWithoutDuplicates2.size();
			}
			if (count != label.size()+Counter) {
				System.out.println((label.size()+Counter) + "\tLabel : ERROR: Label2 count\t" + count);
			} else {
				System.out.println("Label : Label2 count OK");
			}
			if (count1 != label.size()) {
				System.out.println(label.size() + "\tLabel : ERROR: Label count\t" + count1);
			} else {
				System.out.println("Label : Label1 count OK");
			}
			fr.close();
		}
	
		}
