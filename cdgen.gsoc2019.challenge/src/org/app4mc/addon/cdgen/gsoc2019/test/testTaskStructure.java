package org.app4mc.addon.cdgen.gsoc2019.test;

import java.io.IOException;

import org.eclipse.app4mc.amalthea.model.Amalthea;

/**
 * Implementation of testing Task, Runnable structure and Label types.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class testTaskStructure

{

		final private Amalthea model;
	
		public testTaskStructure(final Amalthea Model, String path1, int configFlag) throws IOException {
			this.model = Model;
			System.out.println("############################################################");
			System.out.println("\t\t\tTest Begins");
			System.out.println("############################################################");
			new testRunnable(model, path1,configFlag);
			new	testTaskDef(model, path1, configFlag);
			new	testMain(model, path1,configFlag);
			new testLabel(model, path1);
			System.out.println("############################################################");
			System.out.println("\t\t\tTest Ends");
			System.out.println("############################################################");
		}
}
	
