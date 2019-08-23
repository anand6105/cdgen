# CdGen
> CdGen is a model to text converter tool which is used for real time analysis of the Amalthea System model on the Adapteva Parallella Hardware.

For more details please refer to [documentation][documentation].

## Installation

* [APP4MC version 0.9.5][APP4MC] 
* [Java SE 10][JAVA]
* [Source code for the scheduler][here] 

## User Guide

*	Code Generation
1. Download and Unzip APP4MC
2. Install Java SE 10 
3. Start APP4MC and create a new workspace
4. Import the project from git or any options available.
5. Select checkFileCreateGUI.java from the checks directory andright click and run as Java application
6. Select the scheduler, task preemption, file source of corresponding scheduler and Model of choice.
7. Click start for code generation.
8. Code is generated in the root location with a timestamp.

*	Compilation and Execution
9. Replace the generated code in place of the src directory in below [setup][setup]. 
10. Build, deploy and Run the code on Parallella.

<!-- Markdown link & img dfn's -->
[documentation]:https://cdgendoc.readthedocs.io/en/latest/
[setup]:https://git.eclipse.org/c/app4mc/org.eclipse.app4mc.examples.git/commit/?id=69a0a24f120bb0d79cbd688081ca697368e252f7
[APP4MC]:https://www.eclipse.org/app4mc/downloads/
[JAVA]:https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase10-4425482.html
[here]:https://git.eclipse.org/c/app4mc/org.eclipse.app4mc.examples.git/commit/?id=69a0a24f120bb0d79cbd688081ca697368e252f7
