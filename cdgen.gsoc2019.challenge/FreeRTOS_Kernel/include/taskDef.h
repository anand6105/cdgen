/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Scheduler includes. */
/*
#include "runnableDefinition.h"
#include "labelDeclaration.h"
#include "taskDefinition.h"
*/
#include "FreeRTOS.h"

/* Static definition of the tasks. */
void vTask_10MS( void *pvParameters );
void vTask_20MS( void *pvParameters );
void vTask_5MS( void *pvParameters );
