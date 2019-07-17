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
*Title 		:   FreeRTOSConfig
*Description	:	Holds configuration for the FreeRTOS Software
******************************************************************
******************************************************************/


	#ifndef FREERTOS_CONFIG_H
	#define FREERTOS_CONFIG_H
//-----------------------------------------------------------
	#define configUSE_PREEMPTION			0
	#define configUSE_TICK_HOOK				1
/*	#define configCORE1_START_IN_FREERTOS	0
	#define configUSE_IDLE_SLEEP			1
	#define configUSE_IDLE_HOOK				0
	#define configUSE_TICK_HOOK				0*/
	#define configTICK_RATE_HZ				( ( portTickType ) 1000 )
	#define configMINIMAL_STACK_SIZE		( ( unsigned portSHORT ) 4 )
	#define configTOTAL_HEAP_SIZE			( ( size_t ) ( 32 * 1024 ) )
	#define configMAX_TASK_NAME_LEN			( 16 )
	#define configUSE_TRACE_FACILITY    	1
	#define configUSE_16_BIT_TICKS      	0
	#define configIDLE_SHOULD_YIELD			1
	#define configUSE_CO_ROUTINES 			1
	#define configUSE_MUTEXES				1
	#define configUSE_COUNTING_SEMAPHORES	1
	#define configUSE_ALTERNATIVE_API		0
	#define configUSE_RECURSIVE_MUTEXES		1
	#define configCHECK_FOR_STACK_OVERFLOW	0
	#define configUSE_APPLICATION_TASK_TAG	1
	#define configQUEUE_REGISTRY_SIZE		0
	#define configUSE_IDLE_HOOK		0
	#define configMAX_SYSCALL_INTERRUPT_PRIORITY	1
	#define configMAX_PRIORITIES		( ( unsigned portBASE_TYPE ) 11 )
	#define configMAX_CO_ROUTINE_PRIORITIES ( 2 )
//-----------------------------------------------------------
	#define INCLUDE_vTaskPrioritySet        	1
	#define INCLUDE_uxTaskPriorityGet       	1
	#define INCLUDE_vTaskDelete             	1
	#define INCLUDE_vTaskCleanUpResources   	1
	#define INCLUDE_vTaskSuspend            	1
	#define INCLUDE_vTaskDelayUntil				1
	#define INCLUDE_vTaskDelay					1
	#define INCLUDE_uxTaskGetStackHighWaterMark 0
	#define INCLUDE_xTaskGetSchedulerState		1
	extern void vMainQueueSendPassed( void );
	#define traceQUEUE_SEND( pxQueue ) vMainQueueSendPassed()
	#define configGENERATE_RUN_TIME_STATS		1
	#endif
