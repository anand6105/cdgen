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
#include "label0.h"






 //local variable for Task_ESSP0

 //Shared label 
		uint8_t	VotedVehicleSpeed_Task_ESSP0;
		uint16_t	VehicleSpeedVoltage2_Task_ESSP0;
		uint16_t	VehicleSpeedVoltage1_Task_ESSP0;

	void cIN_Task_ESSP0()
	{
		VehicleSpeedVoltage2_Task_ESSP0	=	shared_label_16bit_read(0);
		VehicleSpeedVoltage1_Task_ESSP0	=	shared_label_16bit_read(9);
	}

	void cOUT_Task_ESSP0()
	{
		shared_label_16bit_write(0,VehicleSpeedVoltage2_Task_ESSP0 );
		shared_label_16bit_write(9,VehicleSpeedVoltage1_Task_ESSP0 );
	}



 //local variable for Task_ESSP1

 //Shared label 
		uint8_t	TriggeredCylinderNumber_Task_ESSP1;
		uint16_t	DecelerationVoltage1_Task_ESSP1;
		uint16_t	DecelerationVoltage2_Task_ESSP1;

	void cIN_Task_ESSP1()
	{
		DecelerationVoltage1_Task_ESSP1	=	shared_label_16bit_read(5);
		DecelerationVoltage2_Task_ESSP1	=	shared_label_16bit_read(6);
	}

	void cOUT_Task_ESSP1()
	{
		shared_label_16bit_write(5,DecelerationVoltage1_Task_ESSP1 );
		shared_label_16bit_write(6,DecelerationVoltage2_Task_ESSP1 );
	}


