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

	uint16_t	VehicleSpeedVoltage1 	=	 65535;
	uint16_t	VehicleSpeedVoltage2 	=	 65535;
	uint8_t	BrakeForce 	=	 255;
	uint16_t	DecelerationVoltage2 	=	 65535;
	uint16_t	DecelerationVoltage1 	=	 65535;
	uint8_t	VotedVehicleSpeed 	=	 255;





 //local variable for Task_ESSP7
		uint16_t	VehicleSpeedVoltage1_Task_ESSP7	=	65535;
		uint16_t	VehicleSpeedVoltage2_Task_ESSP7	=	65535;
		uint8_t	BrakeForce_Task_ESSP7	=	255;

 //Shared label 
		uint8_t	BrakeForceFeedback_Task_ESSP7;

	void cIN_Task_ESSP7()
	{
		VehicleSpeedVoltage1_Task_ESSP7	=	VehicleSpeedVoltage1;
		VehicleSpeedVoltage2_Task_ESSP7	=	VehicleSpeedVoltage2;
		BrakeForce_Task_ESSP7	=	BrakeForce;
		BrakeForceFeedback_Task_ESSP7	=	shared_label_8bit_read(1);
	}

	void cOUT_Task_ESSP7()
	{
		shared_label_8bit_write(1,BrakeForceFeedback_Task_ESSP7 );
	}



 //local variable for Task_ESSP1
		uint16_t	DecelerationVoltage2_Task_ESSP1	=	65535;
		uint16_t	DecelerationVoltage1_Task_ESSP1	=	65535;

 //Shared label 
		uint8_t	TriggeredCylinderNumber_Task_ESSP1;

	void cIN_Task_ESSP1()
	{
		DecelerationVoltage2_Task_ESSP1	=	DecelerationVoltage2;
		DecelerationVoltage1_Task_ESSP1	=	DecelerationVoltage1;
	}

	void cOUT_Task_ESSP1()
	{
		shared_label_8bit_write(2,TriggeredCylinderNumber_Task_ESSP1 );
	}



 //local variable for Task_ESSP4

 //Shared label 
		uint16_t	ThrottlePosition_Task_ESSP4;

	void cIN_Task_ESSP4()
	{
		ThrottlePosition_Task_ESSP4	=	shared_label_16bit_read(5);
	}

	void cOUT_Task_ESSP4()
	{
		shared_label_16bit_write(5,ThrottlePosition_Task_ESSP4 );
	}



 //local variable for Task_ESSP6
		uint16_t	DecelerationVoltage2_Task_ESSP6	=	65535;
		uint16_t	DecelerationVoltage1_Task_ESSP6	=	65535;
		uint8_t	VotedVehicleSpeed_Task_ESSP6	=	255;
		uint8_t	BrakeForce_Task_ESSP6	=	255;

 //Shared label 
		uint8_t	VotedWheelSpeed_Task_ESSP6;
		uint8_t	MonitoredVehicleState_Task_ESSP6;
		uint8_t	ArbitratedBrakeForce_Task_ESSP6;

	void cIN_Task_ESSP6()
	{
		DecelerationVoltage2_Task_ESSP6	=	DecelerationVoltage2;
		DecelerationVoltage1_Task_ESSP6	=	DecelerationVoltage1;
		VotedVehicleSpeed_Task_ESSP6	=	VotedVehicleSpeed;
		BrakeForce_Task_ESSP6	=	BrakeForce;
		VotedWheelSpeed_Task_ESSP6	=	shared_label_8bit_read(0);
		ArbitratedBrakeForce_Task_ESSP6	=	shared_label_8bit_read(4);
	}

	void cOUT_Task_ESSP6()
	{
		DecelerationVoltage2	=	DecelerationVoltage2_Task_ESSP6;
		DecelerationVoltage1	=	DecelerationVoltage1_Task_ESSP6;
		BrakeForce	=	BrakeForce_Task_ESSP6;
		shared_label_8bit_write(3,MonitoredVehicleState_Task_ESSP6 );
		shared_label_8bit_write(0,VotedWheelSpeed_Task_ESSP6 );
		shared_label_8bit_write(4,ArbitratedBrakeForce_Task_ESSP6 );
	}



 //local variable for Task_ESSP0
		uint16_t	VehicleSpeedVoltage1_Task_ESSP0	=	65535;
		uint16_t	VehicleSpeedVoltage2_Task_ESSP0	=	65535;
		uint8_t	VotedVehicleSpeed_Task_ESSP0	=	255;

 //Shared label 

	void cIN_Task_ESSP0()
	{
		VehicleSpeedVoltage1_Task_ESSP0	=	VehicleSpeedVoltage1;
		VehicleSpeedVoltage2_Task_ESSP0	=	VehicleSpeedVoltage2;
		VotedVehicleSpeed_Task_ESSP0	=	VotedVehicleSpeed;
	}

	void cOUT_Task_ESSP0()
	{
		VehicleSpeedVoltage1	=	VehicleSpeedVoltage1_Task_ESSP0;
		VehicleSpeedVoltage2	=	VehicleSpeedVoltage2_Task_ESSP0;
		VotedVehicleSpeed	=	VotedVehicleSpeed_Task_ESSP0;
	}


