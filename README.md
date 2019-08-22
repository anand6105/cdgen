# CdGen
==============
 INTRODUCTION
==============

CdGen is a model to text converter tool which is used for real time analysis of the Amalthea System model on the Adapteva Parallella Hardware.

The code is available currently on the below link,

https://github.com/rprasathg/cdgen

The documentation is available in 

https://github.com/rprasathg/cdgendoc

==============
 REQUIREMENTS
==============

APP4MC version 0.9.5  - https://www.eclipse.org/app4mc/downloads/
Java SE 10 - https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase10-4425482.html
Source code for the scheduler  - https://git.eclipse.org/c/app4mc/org.eclipse.app4mc.examples.git/commit/?id=69a0a24f120bb0d79cbd688081ca697368e252f7


============
 File System
============
$root
|--org.eclipse.app4mc.cdgen(template for files)
	|--ArmCodeFileCreation
	|--FreeRTOSConfigFileCreation
	|--LabelFileCreation
	|--MainFileCreation
	|--MainRMSFileCreation
	|--MakeFileCreation
	|--RunFileCreation
	|--SharedLabelsFileCreation
	|--TaskFileCreation
|--org.eclipse.app4mc.cdgen.checks(App GUI and File selection based on Scheduler selection)
	|--checkFileCreateGUI
	|--checkFreeRTOSConfiguration
	|--checkPOSIXConfiguration
	|--checkRMSConfiguration
|--org.eclipse.app4mc.cdgen.test(Unit Test and Integration)
	|--testLabel
	|--testMain
	|--testRunnable
	|--testTaskDef
	|--testTaskStructure
|--org.eclipse.app4mc.cdgen.utils
	|--fileUtil

=======
 USAGE
=======

1. Select checkFileCreateGUI.java and Run as Java application
2. Select the scheduler, task preemption, file source of corresponding scheduler and Model of choice.
3. Click start for code generation.
4. Use the code in place of the src directory in below setup https://git.eclipse.org/c/app4mc/org.eclipse.app4mc.examples.git/commit/?id=69a0a24f120bb0d79cbd688081ca697368e252f7
5. Build, deploy and Run the code on Parallella.


