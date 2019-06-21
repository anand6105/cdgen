package org.app4mc.cdgen.tool.java.utils;

import java.util.ArrayList;
import java.util.List;

import org.app4mc.cdgen.tool.java.utils_amalthea.*;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaIndex;
import org.eclipse.app4mc.amalthea.model.AmaltheaPackage;
import org.eclipse.app4mc.amalthea.model.LimitType;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessRequirement;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.PuType;
import org.eclipse.app4mc.amalthea.model.RequirementLimit;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskAllocation;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeMetric;
import org.eclipse.app4mc.amalthea.model.TimeRequirementLimit;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;

/**
 * This class handles access to the Amalthea Model. It provides all
 * functionality that is required to convert AMALTHEA specific information to
 * native data types and to get all the relevant fields for e.g. processing
 * units and tasks.
 */
public class ModelUtils {
	public static final long _PERIODMAX = 999_999_999_999L;

	/**
	 * Returns all tasks mapped to the specified <code>ProcessingUnit</code>.
	 *
	 * @param core
	 *            The Processing Unit
	 * @param model
	 *            The Amalthea Model
	 * @return List of Tasks that are allocated to the resp. processing unit.
	 *         List will be empty if (i) no tasks are allocated or (ii) no
	 *         mapping model has been provided.
	 *
	 */
	public static List<Task> getProcessesMappedToCore(final ProcessingUnit core, final Amalthea model) {
		final List<Task> lTasks = new ArrayList<Task>();
		final MappingModel mappingModel = model.getMappingModel();
		if (mappingModel == null) {
			return lTasks;
		}
		// check for all task allocations if the assigned scheduler is in the
		// list
		for (final TaskAllocation taskAlloc : mappingModel.getTaskAllocation()) {
			if (null == taskAlloc.getAffinity()) {
				System.out.println(
						"ERR: Unsupported Mapping Model. At least one allocation does not specify an affinity.");
			}
			// Affinity overrides anything else
			if (taskAlloc.getAffinity().contains(core)) {
				lTasks.add(taskAlloc.getTask());
			}

		}

		return lTasks;
	}

	/**
	 * Returns the processing units executing the specified <code>Task</code>
	 * based on the mapping models affinity attribute.
	 *
	 * @param task
	 *            The task
	 * @param model
	 *            The Amalthea Model
	 * @return List of Processing Units that execute the resp. task. List will
	 *         be empty if (i) no processing units are allocated or (ii) no
	 *         mapping model has been provided.
	 */
	public static List<ProcessingUnit> getAssignedCoreForProcess(final Task task, final Amalthea model) {
		final MappingModel mappingModel = model.getMappingModel();
		if (mappingModel == null) {
			return new ArrayList<ProcessingUnit>();
		}
		// check for all task allocations if the assigned scheduler is in the
		// list
		for (final TaskAllocation taskAlloc : mappingModel.getTaskAllocation()) {
			if (taskAlloc.getTask().equals(task)) {
				if (null == taskAlloc.getAffinity()) {
					System.out.println(
							"ERR: Unsupported Mapping Model. At least one allocation does not specify an affinity.");
				}
				return taskAlloc.getAffinity();
			}

		}

		return new ArrayList<ProcessingUnit>();
	}

	/**
	 * Returns the intervall between two activations of the specified
	 * <code>Task</code> by evaluating the periodic stimulus. Other stimuli are
	 * not supported.
	 *
	 * @param t
	 *            The Task
	 * @return The Time between two activations of the specific Task, or null if
	 *         no periodic stimulus exists.
	 */
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

	/**
	 * Returns the priority of the specified <code>Task</code>, which equals the
	 * distance between two subsequent arrivals (activation period). Priorities
	 * are considered higher the lower the number is, i.e. a Task with priority
	 * 1 has a higher priority compared to a task with priority 999.
	 *
	 * @param t
	 *            The Task
	 * @return The period of the specific task, 999_999_999_999L if unset.
	 */
	public static long getPriority(final Task t) {
		final Time deadline = getDeadline(t);
		if (null == deadline) {
			return _PERIODMAX;
		}
		return timeToLong(deadline);
	}


	/**
	 * Returns the deadline of the specified <code>Process</code> that is
	 * fetched from the <code>ConstraintsModel</code>s
	 * <code>ProcessRequirement</code>s.
	 *
	 * @param amaltheaProcess
	 *            The amalthea process
	 * @return The deadline of the specified process or null if no deadline is
	 *         set.
	 */
	public static Time getDeadline(final Process amaltheaProcess) {
		final EList<ProcessRequirement> requirements = getProcessRequirements(amaltheaProcess);
		for (final ProcessRequirement req : requirements) {
			final RequirementLimit limit = req.getLimit();
			if (limit instanceof TimeRequirementLimit) {
				final TimeRequirementLimit trLimit = (TimeRequirementLimit) limit;
				final TimeMetric metric = trLimit.getMetric();
				final LimitType limitType = trLimit.getLimitType();
				final Time limitValue = trLimit.getLimitValue();
				if (metric == TimeMetric.RESPONSE_TIME && limitType == LimitType.UPPER_LIMIT) {
					// Deadline
					return limitValue;
				}
			}
		}

		return null;
	}

	public static Task getGPUTaskFromCPUTask(final Task TCPU, final Amalthea model) {
		final String taskName = TCPU.getName();
		final String gpuTaskName = taskName.replace("PRE_", "").replace("_gpu_POST", "");
		final EList<Task> allTasks = model.getSwModel().getTasks();
		Task GPUTask = null;
		for (final Task searchIndexTask : allTasks) {
			if (!searchIndexTask.equals(TCPU) && searchIndexTask.getName().equals(gpuTaskName)) {
				GPUTask = searchIndexTask;
				break;
			}
		}
		if (GPUTask == null) {
			System.out.println("error: GPU task not found");
		}

		return GPUTask;
	}

	public static Task getCPUTaskFromGPUTask(final Task TGPU, final Amalthea model) {
		final EList<Task> taskList = model.getSwModel().getTasks();
		Task CPUTask = null;
		for (final Task task : taskList) {
			if (task.getName().contains(TGPU.getName())
					&& (task.getName().contains("PRE") || task.getName().contains("POST"))) {
				CPUTask = task;
			}
		}
		return CPUTask;
	}

	public static boolean isGpuTask(final Task t, final Amalthea model) {
		final List<ProcessingUnit> lProcessingUnits = getAssignedCoreForProcess(t, model);
		for (final ProcessingUnit pu : lProcessingUnits) {
			if (pu.getDefinition().getPuType().equals(PuType.GPU)) {
				return true;
			}
		}
		return false;
	}

	public static Time getTaskTimeSliceGPU(final Task t, final Amalthea model) {
		// get task allocation for time slices
		final List<TaskAllocation> taskAllocationList = model.getMappingModel().getTaskAllocation();
		for (final TaskAllocation taskAlloc : taskAllocationList) {
			if (taskAlloc.getTask().equals(t)) {
				final String slice = taskAlloc.getParameterExtensions().get(0).getValue();
				return FactoryUtil.createTime(slice);
			}
		}
		System.out.println("err: task -> " + t + " time slcie not found");
		return null;
	}


	/**
	 * Converts the specified <code>Time</code> to a long with a scale of
	 * Nanoseconds.
	 *
	 * @param time
	 *            The time
	 * @return The time with a scale of nano seconds.
	 */
	public static long timeToLong(final Time time) {
		return (long) TimeUtil.getAsTimeUnit(time, TimeUnit.NS);
	}

	/**
	 * Returns a list of <code>ProcessRequirement</code>s for the given
	 * <code>Process</code>.
	 *
	 * @param amaltheaProcess
	 * @return
	 */
	private static EList<ProcessRequirement> getProcessRequirements(final Process amaltheaProcess) {
		final EReference _processRequirement_Process = AmaltheaPackage.eINSTANCE.getProcessRequirement_Process();
		return AmaltheaIndex.<ProcessRequirement> getInverseReferences(amaltheaProcess,
				AmaltheaPackage.eINSTANCE.getAbstractProcess_ReferringComponents(),
				java.util.Collections.<EReference> unmodifiableSet(org.eclipse.xtext.xbase.lib.CollectionLiterals
						.<EReference> newHashSet(_processRequirement_Process)));
	}
}
