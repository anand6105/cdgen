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
#include "task.h"
#include "queue.h"
#include "croutine.h"
#include "partest.h"
#include "runnable.h"
#include "taskDef.h"


#define DELAY_MULT 100



		extern	uint8_t	VotedBrakePedalPosition;
		extern	uint8_t	BrakePedalPosition;
		extern	uint8_t	BrakeForce;
		extern	uint8_t	BrakeForceFeedback;
		extern	uint8_t	ArbitratedDiagnosisRequest;
		extern	uint8_t	MonitoredVehicleState;
		extern	uint8_t	BrakeMonitorLevel;
		extern	uint8_t	BrakeSafetyLevel;
		extern	uint8_t	CalculatedBrakeForce;
		extern	uint8_t	BrakeSafetyState;
		extern	uint8_t	VotedVehicleSpeed;
		extern	uint8_t	ArbitratedBrakeForce;
		extern	uint8_t	ABSActivation;
		extern	uint8_t	VotedDecelerationRate;
		extern	uint8_t	VotedWheelSpeed;
		extern	uint8_t	ABSMode;
		extern	uint8_t	BrakeForceCurrent;
		extern	uint8_t	CaliperPosition;
		extern	uint16_t	BrakeForceVoltage;
		extern	uint8_t	BrakeApplication;
		extern	uint16_t	BrakePedalPositionVoltage2;
		extern	uint16_t	BrakePedalPositionVoltage1;
		extern	uint8_t	BrakePedalPosition2;
		extern	uint8_t	BrakePedalPosition1;
		extern	uint16_t	DecelerationVoltage1;
		extern	uint16_t	DecelerationVoltage2;
		extern	uint8_t	DecelerationRate1;
		extern	uint8_t	DecelerationRate2;
		extern	uint16_t	VehicleSpeedVoltage2;
		extern	uint16_t	VehicleSpeedVoltage1;
		extern	uint8_t	VehicleSpeed1;
		extern	uint8_t	VehicleSpeed2;
		extern	uint8_t	WheelSpeed2;
		extern	uint16_t	WheelSpeedVoltage2;
		extern	uint8_t	WheelSpeed1;
		extern	uint16_t	WheelSpeedVoltage1;
		extern	uint8_t	CylinderNumber;
		extern	uint8_t	TriggeredCylinderNumber;
		extern	uint8_t	VotedAPedPosition;
		extern	uint16_t	DesiredThrottlePosition;
		extern	uint16_t	ThrottlePosition;
		extern	uint8_t	APedPosition2;
		extern	uint8_t	APedPosition1;
		extern	uint16_t	MAFRate;
		extern	uint16_t	BaseFuelMassPerStroke;
		extern	uint8_t	MassAirFlow;
		extern	uint8_t	DesiredThrottlePositionVoltage;
		extern	uint8_t	TransientFuelMassPerStroke;
		extern	uint8_t	TotalFuelMassPerStroke;
		extern	uint16_t	InjectionTime2;
		extern	uint16_t	InjectionTime8;
		extern	uint16_t	InjectionTime1;
		extern	uint16_t	InjectionTime4;
		extern	uint16_t	InjectionTime5;
		extern	uint16_t	InjectionTime6;
		extern	uint16_t	InjectionTime7;
		extern	uint16_t	InjectionTime3;
		extern	uint8_t	IgnitionTime;
		extern	uint16_t	IgnitionTime4;
		extern	uint16_t	IgnitionTime8;
		extern	uint16_t	IgnitionTime6;
		extern	uint16_t	IgnitionTime2;
		extern	uint16_t	IgnitionTime1;
		extern	uint16_t	IgnitionTime5;
		extern	uint16_t	IgnitionTime3;
		extern	uint16_t	IgnitionTime7;

		extern	uint8_t	VotedBrakePedalPosition_Task_10MS;
		extern	uint8_t	BrakePedalPosition_Task_10MS;
		extern	uint8_t	BrakeForce_Task_10MS;
		extern	uint8_t	BrakeForceFeedback_Task_10MS;
		extern	uint8_t	ArbitratedDiagnosisRequest_Task_10MS;
		extern	uint8_t	MonitoredVehicleState_Task_10MS;
		extern	uint8_t	BrakeMonitorLevel_Task_10MS;
		extern	uint8_t	BrakeSafetyLevel_Task_10MS;
		extern	uint8_t	CalculatedBrakeForce_Task_10MS;
		extern	uint8_t	BrakeSafetyState_Task_10MS;
		extern	uint8_t	VotedVehicleSpeed_Task_10MS;
		extern	uint8_t	ArbitratedBrakeForce_Task_10MS;
		extern	uint8_t	ABSActivation_Task_10MS;
		extern	uint8_t	VotedDecelerationRate_Task_10MS;
		extern	uint8_t	VotedWheelSpeed_Task_10MS;
		extern	uint8_t	ABSMode_Task_10MS;
		extern	uint8_t	BrakeForceCurrent_Task_10MS;
		extern	uint8_t	CaliperPosition_Task_10MS;
		extern	uint16_t	BrakeForceVoltage_Task_10MS;
		extern	uint8_t	BrakeApplication_Task_10MS;
		extern	uint16_t	BrakePedalPositionVoltage2_Task_10MS;
		extern	uint16_t	BrakePedalPositionVoltage1_Task_10MS;
		extern	uint8_t	BrakePedalPosition2_Task_10MS;
		extern	uint8_t	BrakePedalPosition1_Task_10MS;
		extern	uint16_t	DecelerationVoltage1_Task_10MS;
		extern	uint16_t	DecelerationVoltage2_Task_10MS;
		extern	uint8_t	DecelerationRate1_Task_10MS;
		extern	uint8_t	DecelerationRate2_Task_10MS;
		extern	uint16_t	VehicleSpeedVoltage2_Task_10MS;
		extern	uint16_t	VehicleSpeedVoltage1_Task_10MS;
		extern	uint8_t	VehicleSpeed1_Task_10MS;
		extern	uint8_t	VehicleSpeed2_Task_10MS;
		extern	uint8_t	WheelSpeed2_Task_10MS;
		extern	uint16_t	WheelSpeedVoltage2_Task_10MS;
		extern	uint8_t	WheelSpeed1_Task_10MS;
		extern	uint16_t	WheelSpeedVoltage1_Task_10MS;
		extern	uint8_t	CylinderNumber_Task_10MS;
		extern	uint8_t	TriggeredCylinderNumber_Task_10MS;
		extern	uint8_t	VotedAPedPosition_Task_10MS;
		extern	uint16_t	DesiredThrottlePosition_Task_10MS;
		extern	uint16_t	ThrottlePosition_Task_10MS;
		extern	uint8_t	APedPosition2_Task_10MS;
		extern	uint8_t	APedPosition1_Task_10MS;
		extern	uint16_t	MAFRate_Task_10MS;
		extern	uint16_t	BaseFuelMassPerStroke_Task_10MS;
		extern	uint8_t	MassAirFlow_Task_10MS;
		extern	uint8_t	DesiredThrottlePositionVoltage_Task_10MS;
		extern	uint8_t	TransientFuelMassPerStroke_Task_10MS;
		extern	uint8_t	TotalFuelMassPerStroke_Task_10MS;
		extern	uint16_t	InjectionTime2_Task_10MS;
		extern	uint16_t	InjectionTime8_Task_10MS;
		extern	uint16_t	InjectionTime1_Task_10MS;
		extern	uint16_t	InjectionTime4_Task_10MS;
		extern	uint16_t	InjectionTime5_Task_10MS;
		extern	uint16_t	InjectionTime6_Task_10MS;
		extern	uint16_t	InjectionTime7_Task_10MS;
		extern	uint16_t	InjectionTime3_Task_10MS;
		extern	uint8_t	IgnitionTime_Task_10MS;
		extern	uint16_t	IgnitionTime4_Task_10MS;
		extern	uint16_t	IgnitionTime8_Task_10MS;
		extern	uint16_t	IgnitionTime6_Task_10MS;
		extern	uint16_t	IgnitionTime2_Task_10MS;
		extern	uint16_t	IgnitionTime1_Task_10MS;
		extern	uint16_t	IgnitionTime5_Task_10MS;
		extern	uint16_t	IgnitionTime3_Task_10MS;
		extern	uint16_t	IgnitionTime7_Task_10MS;







	void cIN_Task_10MS()
	{
		vDisplayMessage( "Cin Execution	Task_10MS\n" );
		VotedBrakePedalPosition_Task_10MS	=	VotedBrakePedalPosition;
		BrakePedalPosition_Task_10MS	=	BrakePedalPosition;
		BrakeForce_Task_10MS	=	BrakeForce;
		BrakeForceFeedback_Task_10MS	=	BrakeForceFeedback;
		ArbitratedDiagnosisRequest_Task_10MS	=	ArbitratedDiagnosisRequest;
		MonitoredVehicleState_Task_10MS	=	MonitoredVehicleState;
		BrakeMonitorLevel_Task_10MS	=	BrakeMonitorLevel;
		BrakeSafetyLevel_Task_10MS	=	BrakeSafetyLevel;
		CalculatedBrakeForce_Task_10MS	=	CalculatedBrakeForce;
		BrakeSafetyState_Task_10MS	=	BrakeSafetyState;
		VotedVehicleSpeed_Task_10MS	=	VotedVehicleSpeed;
		ArbitratedBrakeForce_Task_10MS	=	ArbitratedBrakeForce;
		ABSActivation_Task_10MS	=	ABSActivation;
		VotedDecelerationRate_Task_10MS	=	VotedDecelerationRate;
		VotedWheelSpeed_Task_10MS	=	VotedWheelSpeed;
		ABSMode_Task_10MS	=	ABSMode;
		BrakeForceCurrent_Task_10MS	=	BrakeForceCurrent;
		CaliperPosition_Task_10MS	=	CaliperPosition;
		BrakeForceVoltage_Task_10MS	=	BrakeForceVoltage;
		BrakeApplication_Task_10MS	=	BrakeApplication;
		BrakePedalPositionVoltage2_Task_10MS	=	BrakePedalPositionVoltage2;
		BrakePedalPositionVoltage1_Task_10MS	=	BrakePedalPositionVoltage1;
		BrakePedalPosition2_Task_10MS	=	BrakePedalPosition2;
		BrakePedalPosition1_Task_10MS	=	BrakePedalPosition1;
		DecelerationVoltage1_Task_10MS	=	DecelerationVoltage1;
		DecelerationVoltage2_Task_10MS	=	DecelerationVoltage2;
		DecelerationRate1_Task_10MS	=	DecelerationRate1;
		DecelerationRate2_Task_10MS	=	DecelerationRate2;
		VehicleSpeedVoltage2_Task_10MS	=	VehicleSpeedVoltage2;
		VehicleSpeedVoltage1_Task_10MS	=	VehicleSpeedVoltage1;
		VehicleSpeed1_Task_10MS	=	VehicleSpeed1;
		VehicleSpeed2_Task_10MS	=	VehicleSpeed2;
		WheelSpeed2_Task_10MS	=	WheelSpeed2;
		WheelSpeedVoltage2_Task_10MS	=	WheelSpeedVoltage2;
		WheelSpeed1_Task_10MS	=	WheelSpeed1;
		WheelSpeedVoltage1_Task_10MS	=	WheelSpeedVoltage1;
		CylinderNumber_Task_10MS	=	CylinderNumber;
		TriggeredCylinderNumber_Task_10MS	=	TriggeredCylinderNumber;
		VotedAPedPosition_Task_10MS	=	VotedAPedPosition;
		DesiredThrottlePosition_Task_10MS	=	DesiredThrottlePosition;
		ThrottlePosition_Task_10MS	=	ThrottlePosition;
		APedPosition2_Task_10MS	=	APedPosition2;
		APedPosition1_Task_10MS	=	APedPosition1;
		MAFRate_Task_10MS	=	MAFRate;
		BaseFuelMassPerStroke_Task_10MS	=	BaseFuelMassPerStroke;
		MassAirFlow_Task_10MS	=	MassAirFlow;
		DesiredThrottlePositionVoltage_Task_10MS	=	DesiredThrottlePositionVoltage;
		TransientFuelMassPerStroke_Task_10MS	=	TransientFuelMassPerStroke;
		TotalFuelMassPerStroke_Task_10MS	=	TotalFuelMassPerStroke;
		InjectionTime2_Task_10MS	=	InjectionTime2;
		InjectionTime8_Task_10MS	=	InjectionTime8;
		InjectionTime1_Task_10MS	=	InjectionTime1;
		InjectionTime4_Task_10MS	=	InjectionTime4;
		InjectionTime5_Task_10MS	=	InjectionTime5;
		InjectionTime6_Task_10MS	=	InjectionTime6;
		InjectionTime7_Task_10MS	=	InjectionTime7;
		InjectionTime3_Task_10MS	=	InjectionTime3;
		IgnitionTime_Task_10MS	=	IgnitionTime;
		IgnitionTime4_Task_10MS	=	IgnitionTime4;
		IgnitionTime8_Task_10MS	=	IgnitionTime8;
		IgnitionTime6_Task_10MS	=	IgnitionTime6;
		IgnitionTime2_Task_10MS	=	IgnitionTime2;
		IgnitionTime1_Task_10MS	=	IgnitionTime1;
		IgnitionTime5_Task_10MS	=	IgnitionTime5;
		IgnitionTime3_Task_10MS	=	IgnitionTime3;
		IgnitionTime7_Task_10MS	=	IgnitionTime7;
	}

	void cOUT_Task_10MS()
	{
		vDisplayMessage(" Cout Execution	Task_10MS\n\n" );
		BrakePedalPosition	=	BrakePedalPosition_Task_10MS;
		MonitoredVehicleState	=	MonitoredVehicleState_Task_10MS;
		BrakeMonitorLevel	=	BrakeMonitorLevel_Task_10MS;
		CalculatedBrakeForce	=	CalculatedBrakeForce_Task_10MS;
		BrakeSafetyState	=	BrakeSafetyState_Task_10MS;
		BrakeSafetyLevel	=	BrakeSafetyLevel_Task_10MS;
		ABSActivation	=	ABSActivation_Task_10MS;
		ABSMode	=	ABSMode_Task_10MS;
		BrakeForce	=	BrakeForce_Task_10MS;
		BrakeForceCurrent	=	BrakeForceCurrent_Task_10MS;
		CaliperPosition	=	CaliperPosition_Task_10MS;
		BrakeForceVoltage	=	BrakeForceVoltage_Task_10MS;
		BrakeApplication	=	BrakeApplication_Task_10MS;
		BrakePedalPosition2	=	BrakePedalPosition2_Task_10MS;
		BrakePedalPosition1	=	BrakePedalPosition1_Task_10MS;
		VotedBrakePedalPosition	=	VotedBrakePedalPosition_Task_10MS;
		DecelerationRate1	=	DecelerationRate1_Task_10MS;
		DecelerationRate2	=	DecelerationRate2_Task_10MS;
		VotedDecelerationRate	=	VotedDecelerationRate_Task_10MS;
		VotedVehicleSpeed	=	VotedVehicleSpeed_Task_10MS;
		VehicleSpeed1	=	VehicleSpeed1_Task_10MS;
		VehicleSpeed2	=	VehicleSpeed2_Task_10MS;
		WheelSpeed2	=	WheelSpeed2_Task_10MS;
		WheelSpeed1	=	WheelSpeed1_Task_10MS;
		TriggeredCylinderNumber	=	TriggeredCylinderNumber_Task_10MS;
		VotedWheelSpeed	=	VotedWheelSpeed_Task_10MS;
		DesiredThrottlePosition	=	DesiredThrottlePosition_Task_10MS;
		VotedAPedPosition	=	VotedAPedPosition_Task_10MS;
		MAFRate	=	MAFRate_Task_10MS;
		BaseFuelMassPerStroke	=	BaseFuelMassPerStroke_Task_10MS;
		DesiredThrottlePositionVoltage	=	DesiredThrottlePositionVoltage_Task_10MS;
		TotalFuelMassPerStroke	=	TotalFuelMassPerStroke_Task_10MS;
		TransientFuelMassPerStroke	=	TransientFuelMassPerStroke_Task_10MS;
		IgnitionTime	=	IgnitionTime_Task_10MS;
		IgnitionTime8	=	IgnitionTime8_Task_10MS;
		IgnitionTime7	=	IgnitionTime7_Task_10MS;
	}

	void vTask_10MS( void *pvParameters )
	{
		const char *pcTaskName = "Task_10MS is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL( );
			cIN_Task_10MS();
			taskEXIT_CRITICAL( );

			/*Runnable calls */
			CheckPlausability();
			BrakeActuatorMonitor();
			DiagnosisArbiter();
			VehicleStateMonitor();
			BrakeForceCalculation();
			BrakeSafetyMonitor();
			ABSCalculation();
			BrakeForceActuation();
			CaliperPositionCalculation();
			BrakeActuator();
			StopLightActuator();
			BrakePedalSensorDiagnosis();
			BrakePedalSensorTranslation();
			BrakePedalSensorVoter();
			DecelerationSensorDiagnosis();
			DecelerationSensorTranslation();
			VehicleSpeedSensorDiagnosis();
			DecelerationSensorVoter();
			VehicleSpeedSensorVoter();
			VehicleSpeedSensorTranslation();
			WheelSpeedSensorTranslation();
			WheelSpeedSensorDiagnosis();
			CylNumObserver();
			WheelSpeedSensorVoter();
			ThrottleController();
			APedVoter();
			BaseFuelMass();
			ThrottleActuator();
			TotalFuelMass();
			TransientFuelMass();
			InjectionTimeActuation();
			IgnitionTiming();
			IgnitionTimeActuation();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL( );
			cOUT_Task_10MS();
			taskEXIT_CRITICAL( );
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 10*DELAY_MULT);
		}
	}

		extern	uint8_t	ArbitratedBrakeForce_Task_20MS;
		extern	uint8_t	BrakeSafetyState_Task_20MS;
		extern	uint8_t	CalculatedBrakeForce_Task_20MS;



		extern	uint8_t	ArbitratedBrakeForce;
		extern	uint8_t	BrakeSafetyState;
		extern	uint8_t	CalculatedBrakeForce;




	void cIN_Task_20MS()
	{
		vDisplayMessage( "Cin Execution	Task_20MS\n" );
		ArbitratedBrakeForce_Task_20MS	=	ArbitratedBrakeForce;
		BrakeSafetyState_Task_20MS	=	BrakeSafetyState;
		CalculatedBrakeForce_Task_20MS	=	CalculatedBrakeForce;
	}

	void cOUT_Task_20MS()
	{
		vDisplayMessage(" Cout Execution	Task_20MS\n\n" );
		ArbitratedBrakeForce	=	ArbitratedBrakeForce_Task_20MS;
	}

	void vTask_20MS( void *pvParameters )
	{
		const char *pcTaskName = "Task_20MS is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL( );
			cIN_Task_20MS();
			taskEXIT_CRITICAL( );

			/*Runnable calls */
			BrakeForceArbiter();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL( );
			cOUT_Task_20MS();
			taskEXIT_CRITICAL( );
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 20*DELAY_MULT);
		}
	}

		extern	uint16_t	BrakeForceVoltage_Task_5MS;
		extern	uint8_t	BrakeApplication_Task_5MS;
		extern	uint16_t	BrakePedalPositionVoltage2_Task_5MS;
		extern	uint16_t	BrakePedalPositionVoltage1_Task_5MS;
		extern	uint16_t	DecelerationVoltage1_Task_5MS;
		extern	uint16_t	DecelerationVoltage2_Task_5MS;
		extern	uint16_t	VehicleSpeedVoltage2_Task_5MS;
		extern	uint16_t	VehicleSpeedVoltage1_Task_5MS;
		extern	uint16_t	WheelSpeedVoltage2_Task_5MS;
		extern	uint16_t	WheelSpeedVoltage1_Task_5MS;
		extern	uint8_t	APedPosition2_Task_5MS;
		extern	uint8_t	APedPosition1_Task_5MS;
		extern	uint16_t	APedSensor1Voltage_Task_5MS;
		extern	uint16_t	APedSensor2Voltage_Task_5MS;
		extern	uint8_t	ThrottleSensor2Voltage_Task_5MS;
		extern	uint8_t	ThrottleSensor1Voltage_Task_5MS;
		extern	uint16_t	ThrottlePosition_Task_5MS;
		extern	uint16_t	MAFSensorVoltage_Task_5MS;
		extern	uint8_t	MassAirFlow_Task_5MS;



		extern	uint16_t	BrakeForceVoltage;
		extern	uint8_t	BrakeApplication;
		extern	uint16_t	BrakePedalPositionVoltage2;
		extern	uint16_t	BrakePedalPositionVoltage1;
		extern	uint16_t	DecelerationVoltage1;
		extern	uint16_t	DecelerationVoltage2;
		extern	uint16_t	VehicleSpeedVoltage2;
		extern	uint16_t	VehicleSpeedVoltage1;
		extern	uint16_t	WheelSpeedVoltage2;
		extern	uint16_t	WheelSpeedVoltage1;
		extern	uint8_t	APedPosition2;
		extern	uint8_t	APedPosition1;
		extern	uint16_t	APedSensor1Voltage;
		extern	uint16_t	APedSensor2Voltage;
		extern	uint8_t	ThrottleSensor2Voltage;
		extern	uint8_t	ThrottleSensor1Voltage;
		extern	uint16_t	ThrottlePosition;
		extern	uint16_t	MAFSensorVoltage;
		extern	uint8_t	MassAirFlow;




	void cIN_Task_5MS()
	{
		vDisplayMessage( "Cin Execution	Task_5MS\n" );
		BrakeForceVoltage_Task_5MS	=	BrakeForceVoltage;
		BrakeApplication_Task_5MS	=	BrakeApplication;
		BrakePedalPositionVoltage2_Task_5MS	=	BrakePedalPositionVoltage2;
		BrakePedalPositionVoltage1_Task_5MS	=	BrakePedalPositionVoltage1;
		DecelerationVoltage1_Task_5MS	=	DecelerationVoltage1;
		DecelerationVoltage2_Task_5MS	=	DecelerationVoltage2;
		VehicleSpeedVoltage2_Task_5MS	=	VehicleSpeedVoltage2;
		VehicleSpeedVoltage1_Task_5MS	=	VehicleSpeedVoltage1;
		WheelSpeedVoltage2_Task_5MS	=	WheelSpeedVoltage2;
		WheelSpeedVoltage1_Task_5MS	=	WheelSpeedVoltage1;
		APedPosition2_Task_5MS	=	APedPosition2;
		APedPosition1_Task_5MS	=	APedPosition1;
		APedSensor1Voltage_Task_5MS	=	APedSensor1Voltage;
		APedSensor2Voltage_Task_5MS	=	APedSensor2Voltage;
		ThrottleSensor2Voltage_Task_5MS	=	ThrottleSensor2Voltage;
		ThrottleSensor1Voltage_Task_5MS	=	ThrottleSensor1Voltage;
		ThrottlePosition_Task_5MS	=	ThrottlePosition;
		MAFSensorVoltage_Task_5MS	=	MAFSensorVoltage;
		MassAirFlow_Task_5MS	=	MassAirFlow;
	}

	void cOUT_Task_5MS()
	{
		vDisplayMessage(" Cout Execution	Task_5MS\n\n" );
		BrakePedalPositionVoltage2	=	BrakePedalPositionVoltage2_Task_5MS;
		BrakePedalPositionVoltage1	=	BrakePedalPositionVoltage1_Task_5MS;
		DecelerationVoltage1	=	DecelerationVoltage1_Task_5MS;
		DecelerationVoltage2	=	DecelerationVoltage2_Task_5MS;
		VehicleSpeedVoltage2	=	VehicleSpeedVoltage2_Task_5MS;
		VehicleSpeedVoltage1	=	VehicleSpeedVoltage1_Task_5MS;
		WheelSpeedVoltage2	=	WheelSpeedVoltage2_Task_5MS;
		WheelSpeedVoltage1	=	WheelSpeedVoltage1_Task_5MS;
		APedPosition2	=	APedPosition2_Task_5MS;
		APedPosition1	=	APedPosition1_Task_5MS;
		ThrottlePosition	=	ThrottlePosition_Task_5MS;
		MassAirFlow	=	MassAirFlow_Task_5MS;
	}

	void vTask_5MS( void *pvParameters )
	{
		const char *pcTaskName = "Task_5MS is running\r\n";
		portTickType xLastWakeTime;

		for( ;; )
		{
			vDisplayMessage( pcTaskName );
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL( );
			cIN_Task_5MS();
			taskEXIT_CRITICAL( );

			/*Runnable calls */
			EcuBrakeActuator();
			EcuStopLightActuator();
			EcuBrakePedalSensor();
			EcuDecelerationSensor();
			EcuVehicleSpeedSensor();
			EcuWheelSpeedSensor();
			APedSensor();
			ThrottleSensor();
			MassAirFlowSensor();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL( );
			cOUT_Task_5MS();
			taskEXIT_CRITICAL( );
			taskYIELD();
			vTaskDelayUntil(&xLastWakeTime, 5*DELAY_MULT);
		}
	}
