/******************************************************************
******************************************************************
**************#####**####*****#####**######*###****##*************
*************##******#***##**##******##*****##*#***##*************
*************#*******#****#**#****##*######*##**#**##*************
*************##******#***##**##***##*##*****##***#*##*************
**************#####**####*****######*######*##****###*************
******************************************************************
******************************************************************
*Author		:	Ram Prasath Govindarajan
*Tool 		:	CDGen_GSoC
*Version 	:	V1.0.0
*Title 		:   Task Definition
*Description	:	Task Definition with Task Structure
******************************************************************
******************************************************************/


#include "taskDef0.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP1	=	0;
int taskCountTask_ESSP0	=	0;



	uint8_t	CylinderNumber;




	void vTask_ESSP1()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			CylNumObserver();
			DecelerationSensorDiagnosis();

			sleepTimerMs(1 , 12);

			taskCountTask_ESSP1++;
			traceTaskPasses(1, taskCountTask_ESSP1);
			traceRunningTask(0);
	}


	uint8_t	VehicleSpeed2;
	uint8_t	VehicleSpeed1;




	void vTask_ESSP0()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			EcuVehicleSpeedSensor();
			VehicleSpeedSensorTranslation();
			VehicleSpeedSensorVoter();

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP0++;
			traceTaskPasses(0, taskCountTask_ESSP0);
			traceRunningTask(0);
	}

