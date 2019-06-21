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
void vTask_10MS( void *pvParameters );
void vTask_20MS( void *pvParameters );
void vTask_5MS( void *pvParameters );

