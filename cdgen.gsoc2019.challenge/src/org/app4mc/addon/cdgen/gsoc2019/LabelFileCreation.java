package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.app4mc.addon.cdgen.gsoc2019.utils_amalthea.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.emf.common.util.EList;

/**
 * Declaration of Labels with initial values .
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class LabelFileCreation {
	final private Amalthea model;

	/**
	 * Constructor LabelFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param path1
	 * @throws IOException
	 */
	public LabelFileCreation(final Amalthea Model, String path1) throws IOException {
		this.model = Model;
		System.out.println("Label File Creation Begins");
		fileCreate(model, path1);
		System.out.println("Label File Creation Ends");
	}

	private static void fileCreate(Amalthea model, String path1) throws IOException {
		EList<Task> tasks = model.getSwModel().getTasks();
		EList<Label> labellist = model.getSwModel().getLabels();
		String fname = path1 + File.separator + "label.c";
		File f2 = new File(path1);
		File f1 = new File(fname);
		f2.mkdirs();
		try {
			f1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fn = f1;
		FileWriter fw = new FileWriter(fn, true);
		try {
			fileUtil.fileMainHeader(f1);
			labelFileHeader(f1);
			headerIncludesLabel(f1);
			LabelDeclaration(f1, labellist);
			LabelDeclarationLocal(f1, tasks);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void labelFileHeader(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Label Declaration\n");
			fw.write("*Description	:	Declaration and Initialisation of Label\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void headerIncludesLabel(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdint.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void LabelDeclaration(File f1, EList<Label> labellist) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			labellist.stream().distinct().collect(Collectors.toList());
			for (Label label : labellist) {
				String type = fileUtil.datatype(label.getSize().toString());
				long init = fileUtil.intialisation(label.getSize().toString());
				fw.write("\t" + type + "	" + label.getName() + " \t=\t " + init + ";\n");
			}

			fw.write("\n\n\n\n");
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void LabelDeclarationLocal(File f1, EList<Task> tasks) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			for (Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				ArrayList<Label> labellist1 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (Runnable run : runnablesOfTask) {
					Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				fw.write("\n //local variable for " + task.getName() + "\n");
				for (Label lab : listWithoutDuplicates2) {
					String type = fileUtil.datatype(lab.getSize().toString());
					long init = fileUtil.intialisation(lab.getSize().toString());
					fw.write("\t\t" + type + "\t" + lab.getName() + "_" + task.getName() + "\t=\t" + init + ";\n");
				}
				fw.write("\n\n");

			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * helper function to get the Amalthea Model
	 * 
	 */
	public Amalthea getModel() {
		return this.model;
	}

}
