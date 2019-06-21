package org.app4mc.cdgen.tool.java.utils;

import java.util.List;

import org.app4mc.cdgen.tool.java.utils_amalthea.*;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskAllocation;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;

public class RTAUtils {
	public static long getPriority(final Task t) {
		final Time deadline = getDeadline(t);
		if (null == deadline) {
			return 999_999_999_999L;
		}
		return getRawTime(deadline);
		// return deadline.getValue().longValue();
	}

	public static Time getRecurrence(final Task t) {
		final List<Stimulus> lStimuli = t.getStimuli();
		for (final Stimulus s : lStimuli) {
			if (s instanceof PeriodicStimulus) {
				return ((PeriodicStimulus) s).getRecurrence();
			}
			System.out.println("ERR: Unsupported Stimulus in Task " + t + " -> " + s);
		}
		return null;
	}
	
	public static Time getPeriodGPU (Task TGPU, Amalthea model) {
		Task cpuTask = ModelUtils.getCPUTaskFromGPUTask(TGPU, model);
		Time periodFromCPUTask = RTAUtils.getRecurrence(cpuTask);
		
		return periodFromCPUTask;
	}
	
	public static Time getTaskTimeSliceGPU(Task TGPU, Amalthea model) {
		// get task allocation for time slices
		final List<TaskAllocation> taskAllocationList = model.getMappingModel().getTaskAllocation();
		Time TaskOwnTimeSlice = null;
		for (final TaskAllocation taskAlloc : taskAllocationList) {
			if (taskAlloc.getTask().equals(TGPU)) {
				final String x = taskAlloc.getParameterExtensions().get(0).getValue();
				TaskOwnTimeSlice = FactoryUtil.createTime(x);
			}
		}
		if (TaskOwnTimeSlice == null) {
			System.out.println("err: task -> "+TGPU+ " time slcie not found");
		}
		return TaskOwnTimeSlice;
	}
	
	

	public static Time getDeadline(final Task t) {
		if (null != ConstraintsUtil.getDeadline(t)) {
			return ConstraintsUtil.getDeadline(t);
		}
		// If deadline is unavailable, use the recurrence instead.
		final List<Stimulus> lStimuli = t.getStimuli();
		for (final Stimulus s : lStimuli) {
			if (s instanceof PeriodicStimulus) {
				return ((PeriodicStimulus) s).getRecurrence();
			}
		}
		return null;
	}

	/**
	 * Adjusts the time to Nanoseconds (NS) and returns its raw long value
	 *
	 * @param t
	 * @return
	 */
	public static long getRawTime(final Time t) {
		if (null == t) {
			System.out.println("ERR: Time is null");
			return 0;
		}

		final Time adjustedTime = TimeUtil.convertToTimeUnit(t, TimeUnit.PS);
		return adjustedTime.getValue().longValue();
	}
	



}
