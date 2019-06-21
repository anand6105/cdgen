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
#define DELAY_MULT 100

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

