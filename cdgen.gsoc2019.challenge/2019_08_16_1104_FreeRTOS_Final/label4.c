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
#include "label4.h"






 //local variable for Task_ESSP9

 //Shared label 
		uint8_t	VotedWheelSpeed_Task_ESSP9;
		uint16_t	WheelSpeedVoltage2_Task_ESSP9;
		uint16_t	WheelSpeedVoltage1_Task_ESSP9;

	void cIN_Task_ESSP9()
	{
		WheelSpeedVoltage2_Task_ESSP9	=	shared_label_16bit_read(1);
		WheelSpeedVoltage1_Task_ESSP9	=	shared_label_16bit_read(4);
	}

	void cOUT_Task_ESSP9()
	{
		shared_label_16bit_write(1,WheelSpeedVoltage2_Task_ESSP9 );
		shared_label_16bit_write(4,WheelSpeedVoltage1_Task_ESSP9 );
	}



 //local variable for Task_ESSP8

 //Shared label 
		uint8_t	TriggeredCylinderNumber_Task_ESSP8;
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP8;
		uint16_t	MAFRate_Task_ESSP8;

	void cIN_Task_ESSP8()
	{
		TriggeredCylinderNumber_Task_ESSP8	=	shared_label_8bit_read(0);
		ArbitratedDiagnosisRequest_Task_ESSP8	=	shared_label_8bit_read(2);
		MAFRate_Task_ESSP8	=	shared_label_16bit_read(3);
	}

	void cOUT_Task_ESSP8()
	{
		shared_label_8bit_write(0,TriggeredCylinderNumber_Task_ESSP8 );
		shared_label_8bit_write(2,ArbitratedDiagnosisRequest_Task_ESSP8 );
		shared_label_16bit_write(3,MAFRate_Task_ESSP8 );
	}


