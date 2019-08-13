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
#include "debugFlags.h"
#include "task.h"
#include "label1.h"
#include "runnable1.h"

/* Static definition of the tasks. */
void vTask_ESSP7( );
void vTask_ESSP0( );
void vTask_ESSP1( );
void vTask_ESSP4( );
void vTask_ESSP6( );
