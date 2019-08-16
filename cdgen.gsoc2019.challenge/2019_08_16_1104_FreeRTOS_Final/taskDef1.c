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


#include "taskDef1.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP2	=	0;
int taskCountTask_ESSP3	=	0;





	void vTask_ESSP2()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP2();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			WheelSpeedSensorDiagnosis();
			BrakePedalSensorDiagnosis();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP2();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 13);

			taskCountTask_ESSP2++;
			traceTaskPasses(3, taskCountTask_ESSP2);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}


		uint8_t	TransientFuelMassPerStroke;
		uint16_t	InjectionTime7;
		uint16_t	BaseFuelMassPerStroke;
		uint16_t	InjectionTime1;
		uint16_t	InjectionTime2;
		uint8_t	TotalFuelMassPerStroke;
		uint8_t	MassAirFlow;
		uint16_t	MAFSensorVoltage;
		uint16_t	InjectionTime5;
		uint16_t	InjectionTime6;
		uint16_t	InjectionTime4;
		uint16_t	InjectionTime8;
		uint16_t	InjectionTime3;


	void vTask_ESSP3()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP3();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			MassAirFlowSensor();
			BaseFuelMass();
			TransientFuelMass();
			TotalFuelMass();
			InjectionTimeActuation();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP3();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 14);

			taskCountTask_ESSP3++;
			traceTaskPasses(4, taskCountTask_ESSP3);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}

