package org.app4mc.addon.cdgen.gsoc2019.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;


/**
 * Implementation of Common API for CDGen.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class fileUtil {

	public static void fileMainHeader(File f) throws IOException {
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		fr.close();
	}

	public static void FreeRTOSConfigFileHeader(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   FreeRTOSConfig\n");
			fw.write("*Description	:	Holds configuration for the FreeRTOS Software\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	public static String datatype(String string) {
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

	public static long intialisation(String string) {
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

	public static void defineMain(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true); // the true will append the new data
			fw.write("#define DELAY_MULT 100");
			fw.write("\n");
			fw.close();
		} catch (IOException ioe) {
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
	
	//This code is form stackoverflow https://stackoverflow.com/a/2581754
		public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
	        list.sort(Entry.comparingByValue());

	        Map<K, V> result = new LinkedHashMap<>();
	        for (Entry<K, V> entry : list) {
	            result.put(entry.getKey(), entry.getValue());
	        }

	        return result;
	    }

}