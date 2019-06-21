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


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <stdint.h>

/* Scheduler includes. */
#include "FreeRTOS.h"
#include "queue.h"
#include "croutine.h"
#include "partest.h"
#include "task.h"
#include "runnable.h"

#define DELAY_MULT 100

		extern	uint16_t	VehicleSpeedVoltage1;
		extern	uint16_t	VehicleSpeedVoltage2;
		extern	uint8_t	VehicleSpeed2;
		extern	uint8_t	VehicleSpeed1;
		extern	uint8_t	VotedVehicleSpeed;



		extern	uint16_t	VehicleSpeedVoltage1_Task_ESSP0;
		extern	uint16_t	VehicleSpeedVoltage2_Task_ESSP0;
		extern	uint8_t	VehicleSpeed2_Task_ESSP0;
		extern	uint8_t	VehicleSpeed1_Task_ESSP0;
		extern	uint8_t	VotedVehicleSpeed_Task_ESSP0;




	void cIN_Task_ESSP0()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP0\n" );
		VehicleSpeedVoltage1_Task_ESSP0	=	VehicleSpeedVoltage1;
		VehicleSpeedVoltage2_Task_ESSP0	=	VehicleSpeedVoltage2;
		VehicleSpeed2_Task_ESSP0	=	VehicleSpeed2;
		VehicleSpeed1_Task_ESSP0	=	VehicleSpeed1;
		VotedVehicleSpeed_Task_ESSP0	=	VotedVehicleSpeed;
	}

	void cOUT_Task_ESSP0()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP0\n\n" );
		VehicleSpeedVoltage1	=	VehicleSpeedVoltage1_Task_ESSP0;
		VehicleSpeedVoltage2	=	VehicleSpeedVoltage2_Task_ESSP0;
		VehicleSpeed2	=	VehicleSpeed2_Task_ESSP0;
		VehicleSpeed1	=	VehicleSpeed1_Task_ESSP0;
		VotedVehicleSpeed	=	VotedVehicleSpeed_Task_ESSP0;
	}

	void vTask_ESSP0( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP0 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP0();

			/*Runnable calls */
			EcuVehicleSpeedSensor();
			VehicleSpeedSensorTranslation();
			VehicleSpeedSensorVoter();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP0();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 5.0E12*DELAY_MULT);
		}
	}

		extern	uint8_t	CylinderNumber;
		extern	uint8_t	TriggeredCylinderNumber;
		extern	uint16_t	DecelerationVoltage2;
		extern	uint16_t	DecelerationVoltage1;



		extern	uint8_t	CylinderNumber_Task_ESSP1;
		extern	uint8_t	TriggeredCylinderNumber_Task_ESSP1;
		extern	uint16_t	DecelerationVoltage2_Task_ESSP1;
		extern	uint16_t	DecelerationVoltage1_Task_ESSP1;




	void cIN_Task_ESSP1()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP1\n" );
		CylinderNumber_Task_ESSP1	=	CylinderNumber;
		TriggeredCylinderNumber_Task_ESSP1	=	TriggeredCylinderNumber;
		DecelerationVoltage2_Task_ESSP1	=	DecelerationVoltage2;
		DecelerationVoltage1_Task_ESSP1	=	DecelerationVoltage1;
	}

	void cOUT_Task_ESSP1()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP1\n\n" );
		TriggeredCylinderNumber	=	TriggeredCylinderNumber_Task_ESSP1;
	}

	void vTask_ESSP1( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP1 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP1();

			/*Runnable calls */
			CylNumObserver();
			DecelerationSensorDiagnosis();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP1();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 1.0E13*DELAY_MULT);
		}
	}

		extern	uint16_t	WheelSpeedVoltage2;
		extern	uint16_t	WheelSpeedVoltage1;
		extern	uint16_t	BrakePedalPositionVoltage1;
		extern	uint16_t	BrakePedalPositionVoltage2;



		extern	uint16_t	WheelSpeedVoltage2_Task_ESSP2;
		extern	uint16_t	WheelSpeedVoltage1_Task_ESSP2;
		extern	uint16_t	BrakePedalPositionVoltage1_Task_ESSP2;
		extern	uint16_t	BrakePedalPositionVoltage2_Task_ESSP2;




	void cIN_Task_ESSP2()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP2\n" );
		WheelSpeedVoltage2_Task_ESSP2	=	WheelSpeedVoltage2;
		WheelSpeedVoltage1_Task_ESSP2	=	WheelSpeedVoltage1;
		BrakePedalPositionVoltage1_Task_ESSP2	=	BrakePedalPositionVoltage1;
		BrakePedalPositionVoltage2_Task_ESSP2	=	BrakePedalPositionVoltage2;
	}

	void cOUT_Task_ESSP2()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP2\n\n" );
	}

	void vTask_ESSP2( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP2 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP2();

			/*Runnable calls */
			WheelSpeedSensorDiagnosis();
			BrakePedalSensorDiagnosis();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP2();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 1.0E13*DELAY_MULT);
		}
	}

		extern	uint8_t	MassAirFlow;
		extern	uint16_t	MAFSensorVoltage;
		extern	uint16_t	MAFRate;
		extern	uint16_t	BaseFuelMassPerStroke;
		extern	uint8_t	TransientFuelMassPerStroke;
		extern	uint8_t	TotalFuelMassPerStroke;
		extern	uint16_t	InjectionTime8;
		extern	uint16_t	InjectionTime4;
		extern	uint16_t	InjectionTime6;
		extern	uint16_t	InjectionTime5;
		extern	uint16_t	InjectionTime7;
		extern	uint16_t	InjectionTime3;
		extern	uint16_t	InjectionTime2;
		extern	uint8_t	TriggeredCylinderNumber;
		extern	uint16_t	InjectionTime1;



		extern	uint8_t	MassAirFlow_Task_ESSP3;
		extern	uint16_t	MAFSensorVoltage_Task_ESSP3;
		extern	uint16_t	MAFRate_Task_ESSP3;
		extern	uint16_t	BaseFuelMassPerStroke_Task_ESSP3;
		extern	uint8_t	TransientFuelMassPerStroke_Task_ESSP3;
		extern	uint8_t	TotalFuelMassPerStroke_Task_ESSP3;
		extern	uint16_t	InjectionTime8_Task_ESSP3;
		extern	uint16_t	InjectionTime4_Task_ESSP3;
		extern	uint16_t	InjectionTime6_Task_ESSP3;
		extern	uint16_t	InjectionTime5_Task_ESSP3;
		extern	uint16_t	InjectionTime7_Task_ESSP3;
		extern	uint16_t	InjectionTime3_Task_ESSP3;
		extern	uint16_t	InjectionTime2_Task_ESSP3;
		extern	uint8_t	TriggeredCylinderNumber_Task_ESSP3;
		extern	uint16_t	InjectionTime1_Task_ESSP3;




	void cIN_Task_ESSP3()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP3\n" );
		MassAirFlow_Task_ESSP3	=	MassAirFlow;
		MAFSensorVoltage_Task_ESSP3	=	MAFSensorVoltage;
		MAFRate_Task_ESSP3	=	MAFRate;
		BaseFuelMassPerStroke_Task_ESSP3	=	BaseFuelMassPerStroke;
		TransientFuelMassPerStroke_Task_ESSP3	=	TransientFuelMassPerStroke;
		TotalFuelMassPerStroke_Task_ESSP3	=	TotalFuelMassPerStroke;
		InjectionTime8_Task_ESSP3	=	InjectionTime8;
		InjectionTime4_Task_ESSP3	=	InjectionTime4;
		InjectionTime6_Task_ESSP3	=	InjectionTime6;
		InjectionTime5_Task_ESSP3	=	InjectionTime5;
		InjectionTime7_Task_ESSP3	=	InjectionTime7;
		InjectionTime3_Task_ESSP3	=	InjectionTime3;
		InjectionTime2_Task_ESSP3	=	InjectionTime2;
		TriggeredCylinderNumber_Task_ESSP3	=	TriggeredCylinderNumber;
		InjectionTime1_Task_ESSP3	=	InjectionTime1;
	}

	void cOUT_Task_ESSP3()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP3\n\n" );
		MassAirFlow	=	MassAirFlow_Task_ESSP3;
		MAFRate	=	MAFRate_Task_ESSP3;
		BaseFuelMassPerStroke	=	BaseFuelMassPerStroke_Task_ESSP3;
		TransientFuelMassPerStroke	=	TransientFuelMassPerStroke_Task_ESSP3;
		TotalFuelMassPerStroke	=	TotalFuelMassPerStroke_Task_ESSP3;
	}

	void vTask_ESSP3( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP3 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP3();

			/*Runnable calls */
			MassAirFlowSensor();
			BaseFuelMass();
			TransientFuelMass();
			TotalFuelMass();
			InjectionTimeActuation();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP3();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 5.0E12*DELAY_MULT);
		}
	}

		extern	uint16_t	APedSensor2Voltage;
		extern	uint16_t	APedSensor1Voltage;
		extern	uint8_t	APedPosition2;
		extern	uint8_t	APedPosition1;
		extern	uint8_t	VotedAPedPosition;
		extern	uint16_t	ThrottlePosition;
		extern	uint16_t	DesiredThrottlePosition;
		extern	uint8_t	DesiredThrottlePositionVoltage;



		extern	uint16_t	APedSensor2Voltage_Task_ESSP4;
		extern	uint16_t	APedSensor1Voltage_Task_ESSP4;
		extern	uint8_t	APedPosition2_Task_ESSP4;
		extern	uint8_t	APedPosition1_Task_ESSP4;
		extern	uint8_t	VotedAPedPosition_Task_ESSP4;
		extern	uint16_t	ThrottlePosition_Task_ESSP4;
		extern	uint16_t	DesiredThrottlePosition_Task_ESSP4;
		extern	uint8_t	DesiredThrottlePositionVoltage_Task_ESSP4;




	void cIN_Task_ESSP4()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP4\n" );
		APedSensor2Voltage_Task_ESSP4	=	APedSensor2Voltage;
		APedSensor1Voltage_Task_ESSP4	=	APedSensor1Voltage;
		APedPosition2_Task_ESSP4	=	APedPosition2;
		APedPosition1_Task_ESSP4	=	APedPosition1;
		VotedAPedPosition_Task_ESSP4	=	VotedAPedPosition;
		ThrottlePosition_Task_ESSP4	=	ThrottlePosition;
		DesiredThrottlePosition_Task_ESSP4	=	DesiredThrottlePosition;
		DesiredThrottlePositionVoltage_Task_ESSP4	=	DesiredThrottlePositionVoltage;
	}

	void cOUT_Task_ESSP4()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP4\n\n" );
		APedPosition2	=	APedPosition2_Task_ESSP4;
		APedPosition1	=	APedPosition1_Task_ESSP4;
		VotedAPedPosition	=	VotedAPedPosition_Task_ESSP4;
		DesiredThrottlePosition	=	DesiredThrottlePosition_Task_ESSP4;
		DesiredThrottlePositionVoltage	=	DesiredThrottlePositionVoltage_Task_ESSP4;
	}

	void vTask_ESSP4( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP4 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP4();

			/*Runnable calls */
			APedSensor();
			APedVoter();
			ThrottleController();
			ThrottleActuator();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP4();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 5.0E12*DELAY_MULT);
		}
	}

		extern	uint16_t	ThrottlePosition;
		extern	uint8_t	ThrottleSensor1Voltage;
		extern	uint8_t	ThrottleSensor2Voltage;
		extern	uint16_t	BrakePedalPositionVoltage1;
		extern	uint16_t	BrakePedalPositionVoltage2;
		extern	uint8_t	BrakePedalPosition2;
		extern	uint8_t	BrakePedalPosition1;
		extern	uint8_t	VotedBrakePedalPosition;
		extern	uint8_t	BrakePedalPosition;
		extern	uint8_t	BrakeSafetyState;
		extern	uint8_t	MonitoredVehicleState;
		extern	uint8_t	BrakeForceFeedback;
		extern	uint8_t	BrakeSafetyLevel;
		extern	uint8_t	ArbitratedDiagnosisRequest;
		extern	uint8_t	BrakeMonitorLevel;
		extern	uint8_t	CalculatedBrakeForce;
		extern	uint8_t	ArbitratedBrakeForce;
		extern	uint8_t	BrakeApplication;



		extern	uint16_t	ThrottlePosition_Task_ESSP5;
		extern	uint8_t	ThrottleSensor1Voltage_Task_ESSP5;
		extern	uint8_t	ThrottleSensor2Voltage_Task_ESSP5;
		extern	uint16_t	BrakePedalPositionVoltage1_Task_ESSP5;
		extern	uint16_t	BrakePedalPositionVoltage2_Task_ESSP5;
		extern	uint8_t	BrakePedalPosition2_Task_ESSP5;
		extern	uint8_t	BrakePedalPosition1_Task_ESSP5;
		extern	uint8_t	VotedBrakePedalPosition_Task_ESSP5;
		extern	uint8_t	BrakePedalPosition_Task_ESSP5;
		extern	uint8_t	BrakeSafetyState_Task_ESSP5;
		extern	uint8_t	MonitoredVehicleState_Task_ESSP5;
		extern	uint8_t	BrakeForceFeedback_Task_ESSP5;
		extern	uint8_t	BrakeSafetyLevel_Task_ESSP5;
		extern	uint8_t	ArbitratedDiagnosisRequest_Task_ESSP5;
		extern	uint8_t	BrakeMonitorLevel_Task_ESSP5;
		extern	uint8_t	CalculatedBrakeForce_Task_ESSP5;
		extern	uint8_t	ArbitratedBrakeForce_Task_ESSP5;
		extern	uint8_t	BrakeApplication_Task_ESSP5;




	void cIN_Task_ESSP5()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP5\n" );
		ThrottlePosition_Task_ESSP5	=	ThrottlePosition;
		ThrottleSensor1Voltage_Task_ESSP5	=	ThrottleSensor1Voltage;
		ThrottleSensor2Voltage_Task_ESSP5	=	ThrottleSensor2Voltage;
		BrakePedalPositionVoltage1_Task_ESSP5	=	BrakePedalPositionVoltage1;
		BrakePedalPositionVoltage2_Task_ESSP5	=	BrakePedalPositionVoltage2;
		BrakePedalPosition2_Task_ESSP5	=	BrakePedalPosition2;
		BrakePedalPosition1_Task_ESSP5	=	BrakePedalPosition1;
		VotedBrakePedalPosition_Task_ESSP5	=	VotedBrakePedalPosition;
		BrakePedalPosition_Task_ESSP5	=	BrakePedalPosition;
		BrakeSafetyState_Task_ESSP5	=	BrakeSafetyState;
		MonitoredVehicleState_Task_ESSP5	=	MonitoredVehicleState;
		BrakeForceFeedback_Task_ESSP5	=	BrakeForceFeedback;
		BrakeSafetyLevel_Task_ESSP5	=	BrakeSafetyLevel;
		ArbitratedDiagnosisRequest_Task_ESSP5	=	ArbitratedDiagnosisRequest;
		BrakeMonitorLevel_Task_ESSP5	=	BrakeMonitorLevel;
		CalculatedBrakeForce_Task_ESSP5	=	CalculatedBrakeForce;
		ArbitratedBrakeForce_Task_ESSP5	=	ArbitratedBrakeForce;
		BrakeApplication_Task_ESSP5	=	BrakeApplication;
	}

	void cOUT_Task_ESSP5()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP5\n\n" );
		ThrottlePosition	=	ThrottlePosition_Task_ESSP5;
		BrakePedalPositionVoltage1	=	BrakePedalPositionVoltage1_Task_ESSP5;
		BrakePedalPositionVoltage2	=	BrakePedalPositionVoltage2_Task_ESSP5;
		BrakePedalPosition2	=	BrakePedalPosition2_Task_ESSP5;
		BrakePedalPosition1	=	BrakePedalPosition1_Task_ESSP5;
		VotedBrakePedalPosition	=	VotedBrakePedalPosition_Task_ESSP5;
		BrakePedalPosition	=	BrakePedalPosition_Task_ESSP5;
		BrakeSafetyState	=	BrakeSafetyState_Task_ESSP5;
		BrakeSafetyLevel	=	BrakeSafetyLevel_Task_ESSP5;
		CalculatedBrakeForce	=	CalculatedBrakeForce_Task_ESSP5;
		BrakeMonitorLevel	=	BrakeMonitorLevel_Task_ESSP5;
		ArbitratedBrakeForce	=	ArbitratedBrakeForce_Task_ESSP5;
		BrakeApplication	=	BrakeApplication_Task_ESSP5;
	}

	void vTask_ESSP5( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP5 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP5();

			/*Runnable calls */
			ThrottleSensor();
			EcuBrakePedalSensor();
			BrakePedalSensorTranslation();
			BrakePedalSensorVoter();
			CheckPlausability();
			BrakeSafetyMonitor();
			BrakeForceCalculation();
			BrakeForceArbiter();
			StopLightActuator();
			EcuStopLightActuator();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP5();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 5.0E12*DELAY_MULT);
		}
	}

		extern	uint8_t	MonitoredVehicleState;
		extern	uint16_t	DecelerationVoltage2;
		extern	uint16_t	DecelerationVoltage1;
		extern	uint8_t	DecelerationRate1;
		extern	uint8_t	DecelerationRate2;
		extern	uint8_t	VotedDecelerationRate;
		extern	uint8_t	VotedVehicleSpeed;
		extern	uint8_t	ABSActivation;
		extern	uint8_t	VotedWheelSpeed;
		extern	uint8_t	ArbitratedBrakeForce;
		extern	uint8_t	ABSMode;
		extern	uint8_t	BrakeForceCurrent;
		extern	uint8_t	BrakeForce;
		extern	uint8_t	CaliperPosition;
		extern	uint16_t	BrakeForceVoltage;



		extern	uint8_t	MonitoredVehicleState_Task_ESSP6;
		extern	uint16_t	DecelerationVoltage2_Task_ESSP6;
		extern	uint16_t	DecelerationVoltage1_Task_ESSP6;
		extern	uint8_t	DecelerationRate1_Task_ESSP6;
		extern	uint8_t	DecelerationRate2_Task_ESSP6;
		extern	uint8_t	VotedDecelerationRate_Task_ESSP6;
		extern	uint8_t	VotedVehicleSpeed_Task_ESSP6;
		extern	uint8_t	ABSActivation_Task_ESSP6;
		extern	uint8_t	VotedWheelSpeed_Task_ESSP6;
		extern	uint8_t	ArbitratedBrakeForce_Task_ESSP6;
		extern	uint8_t	ABSMode_Task_ESSP6;
		extern	uint8_t	BrakeForceCurrent_Task_ESSP6;
		extern	uint8_t	BrakeForce_Task_ESSP6;
		extern	uint8_t	CaliperPosition_Task_ESSP6;
		extern	uint16_t	BrakeForceVoltage_Task_ESSP6;




	void cIN_Task_ESSP6()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP6\n" );
		MonitoredVehicleState_Task_ESSP6	=	MonitoredVehicleState;
		DecelerationVoltage2_Task_ESSP6	=	DecelerationVoltage2;
		DecelerationVoltage1_Task_ESSP6	=	DecelerationVoltage1;
		DecelerationRate1_Task_ESSP6	=	DecelerationRate1;
		DecelerationRate2_Task_ESSP6	=	DecelerationRate2;
		VotedDecelerationRate_Task_ESSP6	=	VotedDecelerationRate;
		VotedVehicleSpeed_Task_ESSP6	=	VotedVehicleSpeed;
		ABSActivation_Task_ESSP6	=	ABSActivation;
		VotedWheelSpeed_Task_ESSP6	=	VotedWheelSpeed;
		ArbitratedBrakeForce_Task_ESSP6	=	ArbitratedBrakeForce;
		ABSMode_Task_ESSP6	=	ABSMode;
		BrakeForceCurrent_Task_ESSP6	=	BrakeForceCurrent;
		BrakeForce_Task_ESSP6	=	BrakeForce;
		CaliperPosition_Task_ESSP6	=	CaliperPosition;
		BrakeForceVoltage_Task_ESSP6	=	BrakeForceVoltage;
	}

	void cOUT_Task_ESSP6()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP6\n\n" );
		MonitoredVehicleState	=	MonitoredVehicleState_Task_ESSP6;
		DecelerationVoltage2	=	DecelerationVoltage2_Task_ESSP6;
		DecelerationVoltage1	=	DecelerationVoltage1_Task_ESSP6;
		DecelerationRate1	=	DecelerationRate1_Task_ESSP6;
		DecelerationRate2	=	DecelerationRate2_Task_ESSP6;
		VotedDecelerationRate	=	VotedDecelerationRate_Task_ESSP6;
		ABSActivation	=	ABSActivation_Task_ESSP6;
		ABSMode	=	ABSMode_Task_ESSP6;
		BrakeForceCurrent	=	BrakeForceCurrent_Task_ESSP6;
		BrakeForce	=	BrakeForce_Task_ESSP6;
		CaliperPosition	=	CaliperPosition_Task_ESSP6;
		BrakeForceVoltage	=	BrakeForceVoltage_Task_ESSP6;
	}

	void vTask_ESSP6( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP6 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP6();

			/*Runnable calls */
			VehicleStateMonitor();
			EcuDecelerationSensor();
			DecelerationSensorTranslation();
			DecelerationSensorVoter();
			ABSCalculation();
			BrakeForceActuation();
			CaliperPositionCalculation();
			BrakeActuator();
			EcuBrakeActuator();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP6();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 1.0E13*DELAY_MULT);
		}
	}

		extern	uint16_t	VehicleSpeedVoltage1;
		extern	uint16_t	VehicleSpeedVoltage2;
		extern	uint8_t	BrakeForce;
		extern	uint8_t	BrakeForceFeedback;



		extern	uint16_t	VehicleSpeedVoltage1_Task_ESSP7;
		extern	uint16_t	VehicleSpeedVoltage2_Task_ESSP7;
		extern	uint8_t	BrakeForce_Task_ESSP7;
		extern	uint8_t	BrakeForceFeedback_Task_ESSP7;




	void cIN_Task_ESSP7()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP7\n" );
		VehicleSpeedVoltage1_Task_ESSP7	=	VehicleSpeedVoltage1;
		VehicleSpeedVoltage2_Task_ESSP7	=	VehicleSpeedVoltage2;
		BrakeForce_Task_ESSP7	=	BrakeForce;
		BrakeForceFeedback_Task_ESSP7	=	BrakeForceFeedback;
	}

	void cOUT_Task_ESSP7()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP7\n\n" );
	}

	void vTask_ESSP7( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP7 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP7();

			/*Runnable calls */
			VehicleSpeedSensorDiagnosis();
			BrakeActuatorMonitor();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP7();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 1.0E13*DELAY_MULT);
		}
	}

		extern	uint8_t	ArbitratedDiagnosisRequest;
		extern	uint8_t	IgnitionTime;
		extern	uint16_t	MAFRate;
		extern	uint16_t	IgnitionTime4;
		extern	uint16_t	IgnitionTime7;
		extern	uint8_t	TriggeredCylinderNumber;
		extern	uint16_t	IgnitionTime3;
		extern	uint16_t	IgnitionTime6;
		extern	uint16_t	IgnitionTime1;
		extern	uint16_t	IgnitionTime2;
		extern	uint16_t	IgnitionTime5;
		extern	uint16_t	IgnitionTime8;



		extern	uint8_t	ArbitratedDiagnosisRequest_Task_ESSP8;
		extern	uint8_t	IgnitionTime_Task_ESSP8;
		extern	uint16_t	MAFRate_Task_ESSP8;
		extern	uint16_t	IgnitionTime4_Task_ESSP8;
		extern	uint16_t	IgnitionTime7_Task_ESSP8;
		extern	uint8_t	TriggeredCylinderNumber_Task_ESSP8;
		extern	uint16_t	IgnitionTime3_Task_ESSP8;
		extern	uint16_t	IgnitionTime6_Task_ESSP8;
		extern	uint16_t	IgnitionTime1_Task_ESSP8;
		extern	uint16_t	IgnitionTime2_Task_ESSP8;
		extern	uint16_t	IgnitionTime5_Task_ESSP8;
		extern	uint16_t	IgnitionTime8_Task_ESSP8;




	void cIN_Task_ESSP8()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP8\n" );
		ArbitratedDiagnosisRequest_Task_ESSP8	=	ArbitratedDiagnosisRequest;
		IgnitionTime_Task_ESSP8	=	IgnitionTime;
		MAFRate_Task_ESSP8	=	MAFRate;
		IgnitionTime4_Task_ESSP8	=	IgnitionTime4;
		IgnitionTime7_Task_ESSP8	=	IgnitionTime7;
		TriggeredCylinderNumber_Task_ESSP8	=	TriggeredCylinderNumber;
		IgnitionTime3_Task_ESSP8	=	IgnitionTime3;
		IgnitionTime6_Task_ESSP8	=	IgnitionTime6;
		IgnitionTime1_Task_ESSP8	=	IgnitionTime1;
		IgnitionTime2_Task_ESSP8	=	IgnitionTime2;
		IgnitionTime5_Task_ESSP8	=	IgnitionTime5;
		IgnitionTime8_Task_ESSP8	=	IgnitionTime8;
	}

	void cOUT_Task_ESSP8()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP8\n\n" );
		IgnitionTime	=	IgnitionTime_Task_ESSP8;
		IgnitionTime7	=	IgnitionTime7_Task_ESSP8;
		IgnitionTime8	=	IgnitionTime8_Task_ESSP8;
	}

	void vTask_ESSP8( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP8 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP8();

			/*Runnable calls */
			DiagnosisArbiter();
			IgnitionTiming();
			IgnitionTimeActuation();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP8();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 1.0E13*DELAY_MULT);
		}
	}

		extern	uint16_t	WheelSpeedVoltage2;
		extern	uint16_t	WheelSpeedVoltage1;
		extern	uint8_t	WheelSpeed2;
		extern	uint8_t	WheelSpeed1;
		extern	uint8_t	VotedWheelSpeed;



		extern	uint16_t	WheelSpeedVoltage2_Task_ESSP9;
		extern	uint16_t	WheelSpeedVoltage1_Task_ESSP9;
		extern	uint8_t	WheelSpeed2_Task_ESSP9;
		extern	uint8_t	WheelSpeed1_Task_ESSP9;
		extern	uint8_t	VotedWheelSpeed_Task_ESSP9;




	void cIN_Task_ESSP9()
	{
		vDisplayMessage( " Cin Execution	Task_ESSP9\n" );
		WheelSpeedVoltage2_Task_ESSP9	=	WheelSpeedVoltage2;
		WheelSpeedVoltage1_Task_ESSP9	=	WheelSpeedVoltage1;
		WheelSpeed2_Task_ESSP9	=	WheelSpeed2;
		WheelSpeed1_Task_ESSP9	=	WheelSpeed1;
		VotedWheelSpeed_Task_ESSP9	=	VotedWheelSpeed;
	}

	void cOUT_Task_ESSP9()
	{
		vDisplayMessage(" Cout Execution	Task_ESSP9\n\n" );
		WheelSpeedVoltage2	=	WheelSpeedVoltage2_Task_ESSP9;
		WheelSpeedVoltage1	=	WheelSpeedVoltage1_Task_ESSP9;
		WheelSpeed2	=	WheelSpeed2_Task_ESSP9;
		WheelSpeed1	=	WheelSpeed1_Task_ESSP9;
		VotedWheelSpeed	=	VotedWheelSpeed_Task_ESSP9;
	}

	void vTask_ESSP9( void *pvParameters )
	{
		const char *pcTaskName = "Task_ESSP9 is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP9();

			/*Runnable calls */
			EcuWheelSpeedSensor();
			WheelSpeedSensorTranslation();
			WheelSpeedSensorVoter();

			/*Cout - Write back the local variables back to the actual variables */
			cOUT_Task_ESSP9();
			taskEXIT_CRITICAL ();
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 5.0E12*DELAY_MULT);
		}
	}

