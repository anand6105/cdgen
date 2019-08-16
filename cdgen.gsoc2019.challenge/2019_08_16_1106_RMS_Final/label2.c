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
#include "shared_comms.h"
#include "label2.h"






 //local variable for Task_ESSP6

 //Shared label 
		uint8_t	VotedVehicleSpeed_Task_ESSP6;
		uint8_t	BrakeForce_Task_ESSP6;
		uint8_t	MonitoredVehicleState_Task_ESSP6;
		uint8_t	ArbitratedBrakeForce_Task_ESSP6;
		uint8_t	VotedWheelSpeed_Task_ESSP6;
		uint16_t	DecelerationVoltage2_Task_ESSP6;
		uint16_t	DecelerationVoltage1_Task_ESSP6;

	void cIN_Task_ESSP6()
	{
		VotedVehicleSpeed_Task_ESSP6	=	shared_label_8bit_read(0);
		ArbitratedBrakeForce_Task_ESSP6	=	shared_label_8bit_read(5);
		VotedWheelSpeed_Task_ESSP6	=	shared_label_8bit_read(6);
		DecelerationVoltage2_Task_ESSP6	=	shared_label_16bit_read(1);
		DecelerationVoltage1_Task_ESSP6	=	shared_label_16bit_read(9);
	}

	void cOUT_Task_ESSP6()
	{
		shared_label_8bit_write(0,VotedVehicleSpeed_Task_ESSP6 );
		shared_label_8bit_write(5,ArbitratedBrakeForce_Task_ESSP6 );
		shared_label_8bit_write(6,VotedWheelSpeed_Task_ESSP6 );
		shared_label_16bit_write(1,DecelerationVoltage2_Task_ESSP6 );
		shared_label_16bit_write(9,DecelerationVoltage1_Task_ESSP6 );
	}



 //local variable for Task_ESSP4

 //Shared label 
		uint16_t	ThrottlePosition_Task_ESSP4;

	void cIN_Task_ESSP4()
	{
		ThrottlePosition_Task_ESSP4	=	shared_label_16bit_read(0);
	}

	void cOUT_Task_ESSP4()
	{
		shared_label_16bit_write(0,ThrottlePosition_Task_ESSP4 );
	}


