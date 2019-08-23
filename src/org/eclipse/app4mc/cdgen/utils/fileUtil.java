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
package org.eclipse.app4mc.cdgen.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;
import org.eclipse.app4mc.cdgen.LabelFileCreation;


/**
 * Implementation of Common API for CDGen.
 *
 */

public class fileUtil {

	@SuppressWarnings({ "null", "resource" })
	public static void fileMainHeader(final File f) throws IOException {
		FileWriter fr = null;
		try {
			fr = new FileWriter(f);
			fr.write("/******************************************************************\n");
			fr.write("******************************************************************\n");
			fr.write("**************#####**####*****#####**######*###****##*************\n");
			fr.write("*************##******#***##**##******##*****##*#***##*************\n");
			fr.write("*************#*******#****#**#****##*######*##**#**##*************\n");
			fr.write("*************##******#***##**##***##*##*****##***#*##*************\n");
			fr.write("**************#####**####*****######*######*##****###*************\n");
			fr.write("******************************************************************\n");
			fr.write("******************************************************************\n");
			fr.write("*Author		:	Ram Prasath Govindarajan\n");
			fr.write("*Tool 		:	CDGen_GSoC\n");
			fr.write("*Version 	:	V1.0.0\n");
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

		fr.close();
	}

	public static void FreeRTOSConfigFileHeader(final File f1) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   FreeRTOSConfig\n");
			fw.write("*Description	:	Holds configuration for the FreeRTOS Software\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}


	public static String getFileExtension(final File file) {
		final String fileName = file.getName();
		String fileExtension = null;
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
			// return fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		return fileExtension;
	}

	public static String datatype(final String string) {
		String type = null;
		switch (string) {
			case "8 bit":
				type = "uint8_t";
				break;
			case "16 bit":
				type = "uint16_t";
				break;
			case "32 bit":
				type = "uint32_t";
				break;
			case "64 bit":
				type = "uint64_t";
				break;
			default:
				type = "int";
				break;
		}
		return type;
	}


	public static String datatypeSize(final String string) {
		String type = null;
		switch (string) {
			case "8 bit":
				type = "8";
				break;
			case "16 bit":
				type = "16";
				break;
			case "32 bit":
				type = "32";
				break;
			case "64 bit":
				type = "64";
				break;
			default:
				type = "00";
				break;
		}
		return type;
	}

	/**
	 * Shared Label definition and initialization structure.
	 *
	 * @param tasks
	 *
	 * @param file
	 * @param labellist
	 * @return
	 */
	public static List<Label> CoreSpecificLabel(final Amalthea model, final List<Task> tasks) {
		List<Label> SharedLabelList = new ArrayList<Label>();
		for (final Task ta : tasks) {
			SharedLabelList.addAll(SoftwareUtil.getReadLabelSet(ta, null));
		}
		SharedLabelList = SharedLabelList.stream().distinct().collect(Collectors.toList());
		final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
		if (SharedLabelList.size() == 0) {
			System.out.println("Shared Label size 0");
		}
		else {
			// System.out.println("Shared Label size "+SharedLabelList.size());
			final HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation
					.LabelTaskMap(model, SharedLabelList);
			for (final Label share : SharedLabelList) {
				final HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
				final Set<Task> taskList = TaskMap.keySet();
				final Collection<ProcessingUnit> puList = TaskMap.values();
				final List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
				if (puListUnique.size() == 1 && taskList.size() > 1) {
					SharedLabelListSortCore.add(share);
				}
			}
		}
		return SharedLabelListSortCore;
	}

	/**
	 * Shared Label definition and initialization structure.
	 *
	 * @param tasks
	 *
	 * @param file
	 * @param labellist
	 * @return
	 */
	// public static List<Label> SharedLabelDeclarationHead(Amalthea model,
	// List<Task> tasks) {
	public static List<Label> SharedLabelDeclarationHead(final Amalthea model, final List<Task> tasks) {
		final List<Label> SharedLabelList = LabelFileCreation.SharedLabelFinder(model);
		final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
		final List<Label> SharedLabel = new ArrayList<Label>();
		if (SharedLabelList.size() == 0) {
			// System.out.println("Shared Label size 0");
		}
		else {
			// System.out.println("Shared Label size "+SharedLabelList.size());
			final HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation
					.LabelTaskMap(model, SharedLabelList);
			for (final Label share : SharedLabelList) {
				final HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
				final Collection<ProcessingUnit> puList = TaskMap.values();
				final List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
				if (puListUnique.size() > 1) {
					SharedLabelListSortCore.add(share);
				}
			}
		}

		final HashMap<Label, String> SharedLabelTypeMap = new HashMap<Label, String>();
		for (final Label share : SharedLabelListSortCore) {
			SharedLabelTypeMap.put(share, share.getSize().toString());
		}
		final List<String> SharedTypeMapList = new ArrayList<>(
				SharedLabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
		final List<Label> SharedLabelMapList = new ArrayList<Label>(SharedLabelTypeMap.keySet());
		for (int k = 0; k < SharedTypeMapList.size(); k++) {
			final String sh = SharedTypeMapList.get(k);
			for (final Label s : SharedLabelMapList) {
				final String ShTy = SharedLabelTypeMap.get(s);
				if (sh.equals(ShTy)) {
					SharedLabel.add(s);
				}
			}
		}
		return SharedLabel;
	}


	public static long intialisation(final String string) {
		long init = 0;
		switch (string) {
			case "8 bit":
				init = 255;
				break;
			case "16 bit":
				init = 65535;
				break;
			// case "32 bit": init =4294967295; break;
			// case "64 bit": init =18446744073709551615; break;
			default:
				init = 0000;
				break;
		}
		return init;
	}

	public static void defineMain(final File f1) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true); // the true will
			// append the new
			// data
			fw.write("#define DELAY_MULT 100");
			fw.write("\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	public static Time getRecurrence(final Task t) {
		final List<Stimulus> lStimuli = t.getStimuli();
		for (final Stimulus s : lStimuli) {
			if (s instanceof PeriodicStimulus) {
				return ((PeriodicStimulus) s).getRecurrence();
			}
			System.out.println("ERR: Unsupported Stimulus in Task " + t + " -> " + s);
		}
		return null;
	}

	// This code is form stackoverflow https://stackoverflow.com/a/2581754
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
		final List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());

		final Map<K, V> result = new LinkedHashMap<>();
		for (final Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
