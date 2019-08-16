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
		uint16_t	WheelSpeedVoltage2_Task_ESSP9;
		uint16_t	WheelSpeedVoltage1_Task_ESSP9;
		uint8_t	VotedWheelSpeed_Task_ESSP9;

	void cIN_Task_ESSP9()
	{
		WheelSpeedVoltage2_Task_ESSP9	=	shared_label_16bit_read(3);
		WheelSpeedVoltage1_Task_ESSP9	=	shared_label_16bit_read(9);
	}

	void cOUT_Task_ESSP9()
	{
		shared_label_16bit_write(3,WheelSpeedVoltage2_Task_ESSP9 );
		shared_label_16bit_write(9,WheelSpeedVoltage1_Task_ESSP9 );
	}



 //local variable for Task_ESSP8

 //Shared label 
		uint16_t	MAFRate_Task_ESSP8;
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP8;
		uint8_t	TriggeredCylinderNumber_Task_ESSP8;

	void cIN_Task_ESSP8()
	{
		MAFRate_Task_ESSP8	=	shared_label_16bit_read(8);
		ArbitratedDiagnosisRequest_Task_ESSP8	=	shared_label_8bit_read(2);
		TriggeredCylinderNumber_Task_ESSP8	=	shared_label_8bit_read(4);
	}

	void cOUT_Task_ESSP8()
	{
		shared_label_16bit_write(8,MAFRate_Task_ESSP8 );
		shared_label_8bit_write(2,ArbitratedDiagnosisRequest_Task_ESSP8 );
		shared_label_8bit_write(4,TriggeredCylinderNumber_Task_ESSP8 );
	}


