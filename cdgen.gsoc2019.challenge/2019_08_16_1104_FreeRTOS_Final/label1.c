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
#include "label1.h"






 //local variable for Task_ESSP2

 //Shared label 
		uint16_t	WheelSpeedVoltage2_Task_ESSP2;
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP2;
		uint16_t	WheelSpeedVoltage1_Task_ESSP2;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP2;

	void cIN_Task_ESSP2()
	{
		WheelSpeedVoltage2_Task_ESSP2	=	shared_label_16bit_read(1);
		BrakePedalPositionVoltage1_Task_ESSP2	=	shared_label_16bit_read(2);
		WheelSpeedVoltage1_Task_ESSP2	=	shared_label_16bit_read(4);
		BrakePedalPositionVoltage2_Task_ESSP2	=	shared_label_16bit_read(8);
	}

	void cOUT_Task_ESSP2()
	{
		shared_label_16bit_write(1,WheelSpeedVoltage2_Task_ESSP2 );
		shared_label_16bit_write(2,BrakePedalPositionVoltage1_Task_ESSP2 );
		shared_label_16bit_write(4,WheelSpeedVoltage1_Task_ESSP2 );
		shared_label_16bit_write(8,BrakePedalPositionVoltage2_Task_ESSP2 );
	}



 //local variable for Task_ESSP3

 //Shared label 
		uint8_t	TriggeredCylinderNumber_Task_ESSP3;
		uint16_t	MAFRate_Task_ESSP3;

	void cIN_Task_ESSP3()
	{
		TriggeredCylinderNumber_Task_ESSP3	=	shared_label_8bit_read(0);
	}

	void cOUT_Task_ESSP3()
	{
		shared_label_8bit_write(0,TriggeredCylinderNumber_Task_ESSP3 );
	}


