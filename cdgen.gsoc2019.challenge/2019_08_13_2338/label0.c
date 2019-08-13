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
*Title 		:   Label Declaration
*Description	:	Declaration and Initialisation of Label
******************************************************************
******************************************************************/


/* Standard includes. */
#include <stdio.h>
#include <stdint.h>
#include <string.h>

/* Scheduler includes. */
#include "label0.h"

	uint8_t	ArbitratedDiagnosisRequest 	=	 255;
	uint16_t	MAFRate 	=	 65535;
	uint16_t	WheelSpeedVoltage2 	=	 65535;
	uint16_t	WheelSpeedVoltage1 	=	 65535;
	uint16_t	BrakePedalPositionVoltage2 	=	 65535;
	uint16_t	BrakePedalPositionVoltage1 	=	 65535;





 //local variable for Task_ESSP8
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP8	=	255;
		uint16_t	MAFRate_Task_ESSP8	=	65535;
		uint8_t	IgnitionTime_Task_ESSP8	=	255;
		uint16_t	IgnitionTime1_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime8_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime5_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime4_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime6_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime2_Task_ESSP8	=	65535;
		uint8_t	TriggeredCylinderNumber_Task_ESSP8	=	255;
		uint16_t	IgnitionTime7_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime3_Task_ESSP8	=	65535;

	void cIN_Task_ESSP8()
	{
		ArbitratedDiagnosisRequest_Task_ESSP8	=	ArbitratedDiagnosisRequest;
		MAFRate_Task_ESSP8	=	MAFRate;
	}

	void cOUT_Task_ESSP8()
	{
	}



 //local variable for Task_ESSP3
		uint16_t	MAFSensorVoltage_Task_ESSP3	=	65535;
		uint8_t	MassAirFlow_Task_ESSP3	=	255;
		uint16_t	MAFRate_Task_ESSP3	=	65535;
		uint16_t	BaseFuelMassPerStroke_Task_ESSP3	=	65535;
		uint8_t	TransientFuelMassPerStroke_Task_ESSP3	=	255;
		uint8_t	TotalFuelMassPerStroke_Task_ESSP3	=	255;
		uint16_t	InjectionTime8_Task_ESSP3	=	65535;
		uint16_t	InjectionTime1_Task_ESSP3	=	65535;
		uint16_t	InjectionTime4_Task_ESSP3	=	65535;
		uint16_t	InjectionTime6_Task_ESSP3	=	65535;
		uint16_t	InjectionTime7_Task_ESSP3	=	65535;
		uint16_t	InjectionTime3_Task_ESSP3	=	65535;
		uint8_t	TriggeredCylinderNumber_Task_ESSP3	=	255;
		uint16_t	InjectionTime2_Task_ESSP3	=	65535;
		uint16_t	InjectionTime5_Task_ESSP3	=	65535;

	void cIN_Task_ESSP3()
	{
		MAFRate_Task_ESSP3	=	MAFRate;
	}

	void cOUT_Task_ESSP3()
	{
		MAFRate	=	MAFRate_Task_ESSP3;
	}



 //local variable for Task_ESSP2
		uint16_t	WheelSpeedVoltage2_Task_ESSP2	=	65535;
		uint16_t	WheelSpeedVoltage1_Task_ESSP2	=	65535;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP2	=	65535;
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP2	=	65535;

	void cIN_Task_ESSP2()
	{
		WheelSpeedVoltage2_Task_ESSP2	=	WheelSpeedVoltage2;
		WheelSpeedVoltage1_Task_ESSP2	=	WheelSpeedVoltage1;
		BrakePedalPositionVoltage2_Task_ESSP2	=	BrakePedalPositionVoltage2;
		BrakePedalPositionVoltage1_Task_ESSP2	=	BrakePedalPositionVoltage1;
	}

	void cOUT_Task_ESSP2()
	{
	}



 //local variable for Task_ESSP5
		uint16_t	ThrottlePosition_Task_ESSP5	=	65535;
		uint8_t	ThrottleSensor1Voltage_Task_ESSP5	=	255;
		uint8_t	ThrottleSensor2Voltage_Task_ESSP5	=	255;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP5	=	65535;
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP5	=	65535;
		uint8_t	BrakePedalPosition1_Task_ESSP5	=	255;
		uint8_t	BrakePedalPosition2_Task_ESSP5	=	255;
		uint8_t	VotedBrakePedalPosition_Task_ESSP5	=	255;
		uint8_t	BrakePedalPosition_Task_ESSP5	=	255;
		uint8_t	BrakeSafetyLevel_Task_ESSP5	=	255;
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP5	=	255;
		uint8_t	BrakeSafetyState_Task_ESSP5	=	255;
		uint8_t	MonitoredVehicleState_Task_ESSP5	=	255;
		uint8_t	BrakeForceFeedback_Task_ESSP5	=	255;
		uint8_t	BrakeMonitorLevel_Task_ESSP5	=	255;
		uint8_t	CalculatedBrakeForce_Task_ESSP5	=	255;
		uint8_t	ArbitratedBrakeForce_Task_ESSP5	=	255;
		uint8_t	BrakeApplication_Task_ESSP5	=	255;

	void cIN_Task_ESSP5()
	{
		BrakePedalPositionVoltage2_Task_ESSP5	=	BrakePedalPositionVoltage2;
		BrakePedalPositionVoltage1_Task_ESSP5	=	BrakePedalPositionVoltage1;
		ArbitratedDiagnosisRequest_Task_ESSP5	=	ArbitratedDiagnosisRequest;
	}

	void cOUT_Task_ESSP5()
	{
		BrakePedalPositionVoltage2	=	BrakePedalPositionVoltage2_Task_ESSP5;
		BrakePedalPositionVoltage1	=	BrakePedalPositionVoltage1_Task_ESSP5;
	}



 //local variable for Task_ESSP9
		uint16_t	WheelSpeedVoltage2_Task_ESSP9	=	65535;
		uint16_t	WheelSpeedVoltage1_Task_ESSP9	=	65535;
		uint8_t	WheelSpeed1_Task_ESSP9	=	255;
		uint8_t	WheelSpeed2_Task_ESSP9	=	255;
		uint8_t	VotedWheelSpeed_Task_ESSP9	=	255;

	void cIN_Task_ESSP9()
	{
		WheelSpeedVoltage2_Task_ESSP9	=	WheelSpeedVoltage2;
		WheelSpeedVoltage1_Task_ESSP9	=	WheelSpeedVoltage1;
	}

	void cOUT_Task_ESSP9()
	{
		WheelSpeedVoltage2	=	WheelSpeedVoltage2_Task_ESSP9;
		WheelSpeedVoltage1	=	WheelSpeedVoltage1_Task_ESSP9;
	}


