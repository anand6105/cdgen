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
#include "label1.h"

	uint16_t	VehicleSpeedVoltage2 	=	 65535;
	uint16_t	VehicleSpeedVoltage1 	=	 65535;
	uint8_t	BrakeForce 	=	 255;
	uint16_t	DecelerationVoltage1 	=	 65535;
	uint16_t	DecelerationVoltage2 	=	 65535;
	uint8_t	VotedVehicleSpeed 	=	 255;





 //local variable for Task_ESSP7
		uint16_t	VehicleSpeedVoltage2_Task_ESSP7	=	65535;
		uint16_t	VehicleSpeedVoltage1_Task_ESSP7	=	65535;
		uint8_t	BrakeForceFeedback_Task_ESSP7	=	255;
		uint8_t	BrakeForce_Task_ESSP7	=	255;

	void cIN_Task_ESSP7()
	{
		VehicleSpeedVoltage2_Task_ESSP7	=	VehicleSpeedVoltage2;
		VehicleSpeedVoltage1_Task_ESSP7	=	VehicleSpeedVoltage1;
		BrakeForce_Task_ESSP7	=	BrakeForce;
	}

	void cOUT_Task_ESSP7()
	{
	}



 //local variable for Task_ESSP0
		uint16_t	VehicleSpeedVoltage2_Task_ESSP0	=	65535;
		uint16_t	VehicleSpeedVoltage1_Task_ESSP0	=	65535;
		uint8_t	VehicleSpeed1_Task_ESSP0	=	255;
		uint8_t	VehicleSpeed2_Task_ESSP0	=	255;
		uint8_t	VotedVehicleSpeed_Task_ESSP0	=	255;

	void cIN_Task_ESSP0()
	{
		VehicleSpeedVoltage2_Task_ESSP0	=	VehicleSpeedVoltage2;
		VehicleSpeedVoltage1_Task_ESSP0	=	VehicleSpeedVoltage1;
		VotedVehicleSpeed_Task_ESSP0	=	VotedVehicleSpeed;
	}

	void cOUT_Task_ESSP0()
	{
		VehicleSpeedVoltage2	=	VehicleSpeedVoltage2_Task_ESSP0;
		VehicleSpeedVoltage1	=	VehicleSpeedVoltage1_Task_ESSP0;
		VotedVehicleSpeed	=	VotedVehicleSpeed_Task_ESSP0;
	}



 //local variable for Task_ESSP1
		uint8_t	CylinderNumber_Task_ESSP1	=	255;
		uint8_t	TriggeredCylinderNumber_Task_ESSP1	=	255;
		uint16_t	DecelerationVoltage1_Task_ESSP1	=	65535;
		uint16_t	DecelerationVoltage2_Task_ESSP1	=	65535;

	void cIN_Task_ESSP1()
	{
		DecelerationVoltage1_Task_ESSP1	=	DecelerationVoltage1;
		DecelerationVoltage2_Task_ESSP1	=	DecelerationVoltage2;
	}

	void cOUT_Task_ESSP1()
	{
	}



 //local variable for Task_ESSP4
		uint8_t	APedPosition2_Task_ESSP4	=	255;
		uint16_t	APedSensor1Voltage_Task_ESSP4	=	65535;
		uint16_t	APedSensor2Voltage_Task_ESSP4	=	65535;
		uint8_t	APedPosition1_Task_ESSP4	=	255;
		uint8_t	VotedAPedPosition_Task_ESSP4	=	255;
		uint16_t	ThrottlePosition_Task_ESSP4	=	65535;
		uint16_t	DesiredThrottlePosition_Task_ESSP4	=	65535;
		uint8_t	DesiredThrottlePositionVoltage_Task_ESSP4	=	255;

	void cIN_Task_ESSP4()
	{
	}

	void cOUT_Task_ESSP4()
	{
	}



 //local variable for Task_ESSP6
		uint8_t	MonitoredVehicleState_Task_ESSP6	=	255;
		uint16_t	DecelerationVoltage1_Task_ESSP6	=	65535;
		uint16_t	DecelerationVoltage2_Task_ESSP6	=	65535;
		uint8_t	DecelerationRate2_Task_ESSP6	=	255;
		uint8_t	DecelerationRate1_Task_ESSP6	=	255;
		uint8_t	VotedDecelerationRate_Task_ESSP6	=	255;
		uint8_t	ABSActivation_Task_ESSP6	=	255;
		uint8_t	VotedWheelSpeed_Task_ESSP6	=	255;
		uint8_t	ArbitratedBrakeForce_Task_ESSP6	=	255;
		uint8_t	VotedVehicleSpeed_Task_ESSP6	=	255;
		uint8_t	ABSMode_Task_ESSP6	=	255;
		uint8_t	BrakeForceCurrent_Task_ESSP6	=	255;
		uint8_t	BrakeForce_Task_ESSP6	=	255;
		uint8_t	CaliperPosition_Task_ESSP6	=	255;
		uint16_t	BrakeForceVoltage_Task_ESSP6	=	65535;

	void cIN_Task_ESSP6()
	{
		DecelerationVoltage1_Task_ESSP6	=	DecelerationVoltage1;
		DecelerationVoltage2_Task_ESSP6	=	DecelerationVoltage2;
		VotedVehicleSpeed_Task_ESSP6	=	VotedVehicleSpeed;
		BrakeForce_Task_ESSP6	=	BrakeForce;
	}

	void cOUT_Task_ESSP6()
	{
		DecelerationVoltage1	=	DecelerationVoltage1_Task_ESSP6;
		DecelerationVoltage2	=	DecelerationVoltage2_Task_ESSP6;
		BrakeForce	=	BrakeForce_Task_ESSP6;
	}


