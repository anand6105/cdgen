package org.app4mc.cdgen.tool.java.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.app4mc.cdgen.tool.java.utils_amalthea.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.emf.common.util.EList;

public class fileUtil {

public static void fileMainHeader(File f){
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
    fr.write("Author		:	Ram Prasath Govindarajan\n");
    fr.write("Tool 		:	CDGen_GSoC\n");
    fr.write("Version 	:	V1.0.0\n");
    }catch (IOException e) {
        e.printStackTrace();
    }finally{
        //close resources
        try {
        	//quit ;
           fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public static void mainFileHeader(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    //fw.write("add a line\n");//appends the string to the file
	    fw.write("Title 		:   C File for Tasks Call\n");
	    fw.write("Description	:	Main file in which scheduling is done \n");
	    fw.write("******************************************************************\n");
	    fw.write("******************************************************************\n");
	    fw.write("******************************************************************/\n\n\n");
	    
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void runFileHeader(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    //fw.write("add a line\n");//appends the string to the file
	    fw.write("Title 		:   Runnable Definition\n");
	    fw.write("Description	:	Runnable Definition with delay time\n");
	    fw.write("******************************************************************\n");
	    fw.write("******************************************************************/\n\n\n");
	    
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void taskFileHeader(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    //fw.write("add a line\n");//appends the string to the file
	    fw.write("Title 		:   Task Definition\n");
	    fw.write("Description	:	Task Definition with Task Structure\n");
	    fw.write("******************************************************************\n");
	    fw.write("******************************************************************/\n\n\n");
	    
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void labelFileHeader(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    //fw.write("add a line\n");//appends the string to the file
	    fw.write("Title 		:   Label Declaration\n");
	    fw.write("Description	:	Declaration and Initialisation of Label\n");
	    fw.write("******************************************************************\n");
	    fw.write("******************************************************************/\n\n\n");
	    
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void headerIncludesMain(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    //fw.write("add a line\n");//appends the string to the file
	    fw.write("/* Standard includes. */\n");
	    fw.write("#include <stdio.h>\n");
    	fw.write("#include <stdlib.h>\n");
    	fw.write("#include <string.h>\n\n");
    	fw.write("/* Scheduler includes. */\n");
    	fw.write("#include <runnableDefinition.h>\n");
    	fw.write("#include <taskDefinition.h>\n");
    	fw.write("#include <labelDeclaration.h>\n");
    	fw.write("#include "+"FreeRTOS.h"+"\n\n");
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void headerIncludesRun(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); 
	    fw.write("/* Standard includes. */\n");
	    fw.write("#include <stdio.h>\n");
    	fw.write("#include <stdlib.h>\n");
    	fw.write("#include <string.h>\n\n");
    	fw.write("/* Scheduler includes. */\n");
    	fw.write("#include <runnableDefinition.h>\n");
    	fw.write("#include <taskDefinition.h>\n");
    	fw.write("#include <labelDeclaration.h>\n");
    	fw.write("#include "+"FreeRTOS.h"+"\n\n");
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void headerIncludesLabel(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); 
	    fw.write("/* Standard includes. */\n");
	    fw.write("#include <stdio.h>\n");
    	fw.write("#include <stdlib.h>\n");
    	fw.write("#include <string.h>\n\n");
    	fw.write("/* Scheduler includes. */\n");
    	fw.write("#include <labelDeclaration.h>\n");
    	fw.write("#include "+"FreeRTOS.h"+"\n\n");
    	fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void headerIncludesTask(File f1) {
	// TODO Auto-generated method stub
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    fw.write("/* Standard includes. */\n");
	    fw.write("#include <stdio.h>\n");
    	fw.write("#include <stdlib.h>\n");
    	fw.write("#include <string.h>\n\n");
    	fw.write("/* Scheduler includes. */\n");
    	fw.write("#include <runnableDefinition.h>\n");
    	fw.write("#include <labelDeclaration.h>\n");
    	fw.write("#include "+"FreeRTOS.h"+"\n\n");
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void runnableDefinition(File f1, EList<Runnable> runnables) {
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    for (Runnable Run:runnables) {
	    	fw.write("void "+Run.getName()+"\t{\n");
        	fw.write("\tprintf(\"Runnable Execution "+Run.getName()+"\\n\""+");\n");
        	fw.write("\tdelay();\n");
        	fw.write("}\n");
        	}
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}    

}

public static void TaskDefinition(File f1, EList<Task> tasks) {
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    for (Task task:tasks) {
        	fw.write("\tvoid v"+task.getName()+"( void *pvParameters )"+"\n\t{\n");
        	fw.write("\tconst char *pcTaskName = \""+task.getName()+" is running\\r\\n\";\n");
        	fw.write("\tvolatile uint32_t ul;\n\n");
        	fw.write("\t\tfor( ;; )\n\t\t{\n");
        	List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
        	fw.write("\t\t\tvPrintString( pcTaskName );\n");
        	for (Runnable run:runnablesOfTask) {
				fw.write("\t\t\t"+run.getName()+"();\n");
        	}
			fw.write("\t\t\tfor( ul = 0; ul < mainDELAY_LOOP_COUNT; ul++ )\n");
        	fw.write("\t\t\t{\n");
        	fw.write("\t\t\t}\n");
			fw.write("\t\t}\n");
			fw.write("\t\tvTaskDelayUntil(TaskDelay ms);\n");
			fw.write("\t}\n\n");
        }
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}
}

public static void mainStaticTaskDef(File f1, EList<Task> tasks) {
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    fw.write("/* Static definition of the tasks. */\n");
	    for (Task task:tasks) {
	    fw.write("extern static void v"+task.getName() +"( void *pvParameters );\n");
	    }
	    fw.write("\n");
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}
}

public static void mainFucntion(File f1, EList<Task> tasks) {
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    fw.write("int main(void) \n{\n");
	    for (Task task:tasks) {
	    	fw.write("\txTaskCreate( v"+task.getName()+", "+task.getName().toUpperCase()+", configMINIMAL_STACK_SIZE, NULL, main"+task.getName()+", NULL );\n");
	    }
	    fw.write("\tvTaskStartScheduler();\n");
	    fw.write("\t"+"return 0;\n");
	    fw.write("}\n");
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}
}

public static void mainTaskPriority(File f1, EList<Task> tasks) {
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    fw.write("/* Task priorities. */\n");
	    for (Task task:tasks) {
	    	fw.write("\t#define main"+task.getName()+"\t( tskIDLE_PRIORITY + "+task.getSize()+" )\n");
	    }
	    fw.write("\n");
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}
}

public static void LabelDeclaration(File f1, EList<Label> labellist) {
	try
	{
	    File fn= f1;
	    FileWriter fw = new FileWriter(fn,true); //the true will append the new data
	    for (Label label:labellist) {
	    //	if()
	    	fw.write("\t"+label.getName()+"\t"+label.getSize()+";\n");
	    }
	    fw.close();
	}
	catch(IOException ioe)
	{
	    System.err.println("IOException: " + ioe.getMessage());
	}

	// TODO Auto-generated method stub
	
}}