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
#include "runnable.h"
#include "taskDef.h"
#include "task.h"
/* Static definition of the tasks. */
void vTask_ESSP0( void *pvParameters );
void vTask_ESSP1( void *pvParameters );
void vTask_ESSP2( void *pvParameters );
void vTask_ESSP3( void *pvParameters );
void vTask_ESSP4( void *pvParameters );
void vTask_ESSP5( void *pvParameters );
void vTask_ESSP6( void *pvParameters );
void vTask_ESSP7( void *pvParameters );
void vTask_ESSP8( void *pvParameters );
void vTask_ESSP9( void *pvParameters );

void cOUT_Task_ESSP0();
void cOUT_Task_ESSP1();
void cOUT_Task_ESSP2();
void cOUT_Task_ESSP3();
void cOUT_Task_ESSP4();
void cOUT_Task_ESSP5();
void cOUT_Task_ESSP6();
void cOUT_Task_ESSP7();
void cOUT_Task_ESSP8();
void cOUT_Task_ESSP9();

void cIN_Task_ESSP0();
void cIN_Task_ESSP1();
void cIN_Task_ESSP2();
void cIN_Task_ESSP3();
void cIN_Task_ESSP4();
void cIN_Task_ESSP5();
void cIN_Task_ESSP6();
void cIN_Task_ESSP7();
void cIN_Task_ESSP8();
void cIN_Task_ESSP9();

