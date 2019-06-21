/**
 ********************************************************************************
 * Copyright (c) 2015-2019 Robert Bosch GmbH and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Robert Bosch GmbH - initial API and implementation
 ********************************************************************************
 */

package org.app4mc.cdgen.tool.java.utils_amalthea;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.AmaltheaIndex;
import org.eclipse.app4mc.amalthea.model.AmaltheaServices;
import org.eclipse.app4mc.amalthea.model.ArrivalCurveStimulus;
import org.eclipse.app4mc.amalthea.model.CallSequence;
import org.eclipse.app4mc.amalthea.model.CustomStimulus;
import org.eclipse.app4mc.amalthea.model.EventStimulus;
import org.eclipse.app4mc.amalthea.model.ExecutionNeed;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.HwFeature;
import org.eclipse.app4mc.amalthea.model.HwFeatureCategory;
import org.eclipse.app4mc.amalthea.model.IDiscreteValueDeviation;
import org.eclipse.app4mc.amalthea.model.ITimeDeviation;
import org.eclipse.app4mc.amalthea.model.InterProcessStimulus;
import org.eclipse.app4mc.amalthea.model.InterProcessTrigger;
import org.eclipse.app4mc.amalthea.model.ModeLabel;
import org.eclipse.app4mc.amalthea.model.ModeLiteral;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.PeriodicSyntheticStimulus;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.RelativePeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.SingleStimulus;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.TaskRunnableCall;
import org.eclipse.app4mc.amalthea.model.Ticks;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.VariableRateStimulus;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

public class RuntimeUtil {

	public static enum TimeType {
		BCET, ACET, WCET
	}

	public static enum PositionType {
		FIRST, LAST
	}

	public static enum AccessDirection {
		READ, WRITE
	}

	/**
	 * Computes the execution time of a Process (Task or ISR) - unique mapping
	 * is required
	 *
	 * @param process
	 *            task or isr
	 * @param modes
	 *            (optional) - null works
	 * @param executionCase
	 *            BCET, ACET, WCET
	 * @return execution time
	 */
	public static Time getExecutionTimeForProcess(final Process process, final EMap<ModeLabel, ModeLiteral> modes,
			final TimeType executionCase) {
		final Time result = FactoryUtil.createTime();

		final Set<ProcessingUnit> processingUnits = DeploymentUtil.getAssignedCoreForProcess(process,
				(Amalthea) process.eContainer().eContainer());
		final List<Runnable> runnables = SoftwareUtil.getRunnableList(process, modes);
		if (processingUnits.size() == 1) {
			final ProcessingUnit processingUnit = processingUnits.iterator().next();
			for (final Runnable runnable : runnables) {
				result.add(getExecutionTimeForRunnable(runnable, processingUnit, modes, executionCase));
			}
		}
		else {
			System.out.println(
					"Mapping unclear. Process mapped to " + processingUnits.size() + " Cores - Use PU specific method");
		}
		return result;
	}

	/**
	 * Computes the execution time of a Process (Task or ISR) on a given
	 * processing unit
	 *
	 * @param process
	 *            task or isr
	 * @param processingUnit
	 *            executing processing unit
	 * @param modes
	 *            (optional) - null works
	 * @param executionCase
	 *            BCET, ACET, WCET
	 * @return execution time
	 */
	public static Time getExecutionTimeForProcess(final Process process, final ProcessingUnit processingUnit,
			final EMap<ModeLabel, ModeLiteral> modes, final TimeType executionCase) {
		Time result = FactoryUtil.createTime();

		final List<Runnable> runnables = SoftwareUtil.getRunnableList(process, modes);
		for (final Runnable runnable : runnables) {
			result = result.add(getExecutionTimeForRunnable(runnable, processingUnit, modes, executionCase));
		}
		return result;
	}

	/**
	 * Computes the execution time of a Runnable on a given processing unit
	 *
	 * @param runnable
	 *            runnable
	 * @param processingUnit
	 *            executing processing unit
	 * @param modes
	 *            (optional) - null works
	 * @param executionCase
	 *            BCET, ACET, WCET
	 * @return execution time
	 */
	public static Time getExecutionTimeForRunnable(final Runnable runnable, final ProcessingUnit processingUnit,
			final EMap<ModeLabel, ModeLiteral> modes, final TimeType executionCase) {
		Time result = FactoryUtil.createTime();

		final List<Ticks> tickList = SoftwareUtil.getTicks(runnable, modes);
		for (final Ticks tick : tickList) {
			result = result.add(getExecutionTimeForTicks(tick, processingUnit, executionCase));
		}

		final List<ExecutionNeed> executionNeedList = SoftwareUtil.getExecutionNeeds(runnable, modes);
		if (!executionNeedList.isEmpty()) {
			for (final ExecutionNeed need : executionNeedList) {
				result = result.add(getExecutionTimeForExecutionNeeds(need, processingUnit, executionCase));
			}
		}
		return result;
	}

	// Simon 11.01.2019:
	// Idea/Discussion: Why is TimeType needed here?
	// If an extended tick is given, probably also a ITimeDeviation shall be
	// returned?
	// (I guess, this heavily depends on the use case)
	// --> Second step would now then be ITimeDeviation + TimeType -> Time
	//
	/**
	 * Computes time for ticks on a given processing unit
	 */
	public static Time getExecutionTimeForTicks(final Ticks ticks, final ProcessingUnit processingUnit,
			final TimeType executionCase) {
		Time result = FactoryUtil.createTime();

		if (ticks.getExtended().get(processingUnit.getDefinition()) != null) {
			final IDiscreteValueDeviation deviation = ticks.getExtended().get(processingUnit.getDefinition());
			result = getExecutionTimeForTicksDeviation(deviation, processingUnit, executionCase);
		}
		else {
			result = getExecutionTimeForTicksDeviation(ticks.getDefault(), processingUnit, executionCase);
		}
		return result;
	}

	/**
	 * Computes time for ticks on a given processing unit. Returns -1 if
	 * deviation is not available for a core, i.e. the resp process/runnable may
	 * NOT be executed on the given processing unit.
	 *
	 * @param deviation
	 *            ticks deviation
	 * @param processingUnit
	 *            executing processing unit
	 * @param executionCase
	 *            BCET, ACET, WCET
	 * @return execution time
	 */
	public static Time getExecutionTimeForTicksDeviation(final IDiscreteValueDeviation deviation,
			final ProcessingUnit processingUnit, final TimeType executionCase) {
		double ticks = 0d;
		if (null == deviation) {
			return FactoryUtil.createTime(-1, TimeUnit.PS);
		}
		switch (executionCase) {
			case BCET:
				ticks = deviation.getLowerBound().doubleValue();
				break;
			case ACET:
				ticks = deviation.getAverage();
				break;
			case WCET:
				ticks = deviation.getUpperBound().doubleValue();
				break;
		}
		final Time result = getExecutionTimeForCycles(ticks, processingUnit.getFrequencyDomain().getDefaultValue());
		return result;
	}

	/**
	 * Computes time for execution needs on a given processing unit
	 *
	 * @param need
	 *            execution need
	 * @param processingUnit
	 *            executing processing unit
	 * @param executionCase
	 *            BCET, ACET, WCET
	 * @return execution time
	 */
	public static Time getExecutionTimeForExecutionNeeds(final ExecutionNeed need, final ProcessingUnit processingUnit,
			final TimeType executionCase) {
		Time result = FactoryUtil.createTime();

		final HWModel hwModel = (HWModel) processingUnit.getDefinition().eContainer();
		for (final Entry<String, IDiscreteValueDeviation> needEntry : need.getNeeds()) {
			final Set<? extends HwFeatureCategory> hwFeatureCategory = AmaltheaIndex.getElements(hwModel,
					needEntry.getKey(), HwFeatureCategory.class);
			if (hwFeatureCategory.size() == 1) {
				result = getExecutionTimeForExecutionNeedEntry(needEntry.getValue(),
						hwFeatureCategory.iterator().next(), processingUnit, executionCase);
			}
			else {
				// should not happen since name is unique identifier
				System.out.println("Mutliple Categories with the same name: " + needEntry.getKey());
			}
		}
		return result;
	}

	/**
	 * Computes time for specific execution need entry on a given processing
	 * unit
	 *
	 * @param deviation
	 *            needs deviation
	 * @param hwFeatureCategory
	 *            corresponding feature category
	 * @param processingUnit
	 *            executing processing unit
	 * @param executionCase
	 *            BCET, ACET, WCET
	 * @return execution time
	 */
	public static Time getExecutionTimeForExecutionNeedEntry(final IDiscreteValueDeviation deviation,
			final HwFeatureCategory hwFeatureCategory, final ProcessingUnit processingUnit,
			final TimeType executionCase) {
		double ticks = 0d;
		switch (executionCase) {
			case BCET:
				ticks = deviation.getLowerBound().doubleValue();
			case ACET:
				ticks = deviation.getAverage();
			case WCET:
				ticks = deviation.getUpperBound().doubleValue();
		}
		double scaleFactor = 0;
		for (final HwFeature feature : processingUnit.getDefinition().getFeatures()) {
			if (feature.getContainingCategory().equals(hwFeatureCategory)) {
				scaleFactor = feature.getValue();
			}
		}
		ticks = ticks * scaleFactor;
		final Time result = getExecutionTimeForCycles(ticks, processingUnit.getFrequencyDomain().getDefaultValue());
		return result;
	}

	/**
	 * Computes time for a number of ticks with a given frequency
	 *
	 * @param ticks
	 *            ticks / cycles
	 * @param frequency
	 *            frequency (of an executing processing unit)
	 * @return execution time
	 */
	public static Time getExecutionTimeForCycles(final double ticks, final Frequency frequency) {
		final double cyclesPerSecond = AmaltheaServices.convertToHertz(frequency).doubleValue();
		final double factor = 1.0d / cyclesPerSecond;
		final Time oneSecond = FactoryUtil.createTime(1, TimeUnit.S);
		final Time t1 = oneSecond.multiply(ticks);
		final Time result = t1.multiply(factor);
		return result.adjustUnit();
	}


	// TODO: Time to ticks?


	/**
	 * Sets the runtime of the given runnable to the given ExecutionNeed
	 */
	public static void setRuntimeOfRunnable(final Runnable runnable, final ExecutionNeed need,
			final EMap<ModeLabel, ModeLiteral> modes) {
		clearRuntimeOfRunnable(runnable, modes);
		addRuntimeToRunnable(runnable, need);
	}

	/**
	 * Sets the runtime of the given runnable to the given Ticks
	 */
	public static void setRuntimeOfRunnable(final Runnable runnable, final Ticks ticks,
			final EMap<ModeLabel, ModeLiteral> modes) {
		clearRuntimeOfRunnable(runnable, modes);
		addRuntimeToRunnable(runnable, ticks);
	}


	/**
	 * Adds Runtime (given as ExecutionNeed) to an existing Runnable currently
	 * without consideration of modes
	 */
	// TODO: Mode-based API
	public static void addRuntimeToRunnable(final Runnable runnable, final ExecutionNeed need) {
		runnable.getRunnableItems().add(need);
	}


	/**
	 * Adds Runtime (given as Ticks) to an existing Runnable currently without
	 * consideration of modes
	 */
	// TODO: Mode-based API
	public static void addRuntimeToRunnable(final Runnable runnable, final Ticks ticks) {
		runnable.getRunnableItems().add(ticks);
	}


	/**
	 * Clears all runtime information (execution needs and ticks)
	 *
	 * @param model
	 *            Amalthea model
	 * @param modes
	 *            (optional) - null works
	 */
	public static void clearRuntimeOfModel(final Amalthea model, final EMap<ModeLabel, ModeLiteral> modes) {
		final List<Process> processes = new ArrayList<Process>();
		processes.addAll(model.getSwModel().getTasks());
		processes.addAll(model.getSwModel().getIsrs());

		for (final Process process : processes) {
			clearRuntimeOfProcess(process, modes);
		}
	}


	/**
	 * Clears all runtime information (execution needs and ticks)
	 *
	 * @param process
	 *            task or isr
	 * @param modes
	 *            (optional) - null works
	 */
	public static void clearRuntimeOfProcess(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		final List<Runnable> runnables = SoftwareUtil.getRunnableList(process, modes);
		for (final Runnable runnable : runnables) {
			clearRuntimeOfRunnable(runnable, modes);
		}
	}

	/**
	 * Clears all runtime information (execution needs and ticks)
	 *
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 */
	public static void clearRuntimeOfRunnable(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		final List<ExecutionNeed> executionNeeds = SoftwareUtil.getExecutionNeeds(runnable, modes);
		AmaltheaIndex.deleteAll(executionNeeds);

		final List<Ticks> ticks = SoftwareUtil.getTicks(runnable, modes);
		AmaltheaIndex.deleteAll(ticks);
	}

	/**
	 * Creates a new Runnable with the given runtime and create a CallSequence
	 * at beginning / end of the given process
	 *
	 * @param process
	 *            containing process (task or isr)
	 * @param need
	 *            execution need
	 * @param runnableName
	 *            name of new runnable
	 * @param positon
	 *            FIRST, LAST
	 * @return the new runnable
	 */
	public static Runnable addRuntimeToProcessAsNewRunnable(final Process process, final ExecutionNeed need,
			final String runnableName, final PositionType positon) {
		final Runnable run = AmaltheaFactory.eINSTANCE.createRunnable();
		run.setName(runnableName);
		run.getRunnableItems().add(need);

		final TaskRunnableCall tCall = AmaltheaFactory.eINSTANCE.createTaskRunnableCall();
		tCall.setRunnable(run);

		final CallSequence cs = AmaltheaFactory.eINSTANCE.createCallSequence();
		cs.getCalls().add(tCall);
		cs.setName("Call " + runnableName);

		if (positon.equals(PositionType.FIRST)) {
			process.getCallGraph().getGraphEntries().add(0, cs);
		}
		else {
			process.getCallGraph().getGraphEntries().add(cs);
		}

		return run;
	}

	/**
	 * Creates a new Runnable with the given runtime and create a CallSequence
	 * at beginning / end of the given process
	 */
	/**
	 * @param process
	 *            containing process (task or isr)
	 * @param ticks
	 *            ticks
	 * @param runnableName
	 *            name of new runnable
	 * @param positon
	 *            FIRST, LAST
	 * @return the new runnable
	 */
	public static Runnable addRuntimeToProcessAsNewRunnable(final Process process, final Ticks ticks,
			final String runnableName, final PositionType positon) {
		final Runnable run = AmaltheaFactory.eINSTANCE.createRunnable();
		run.setName(runnableName);
		run.getRunnableItems().add(ticks);

		final TaskRunnableCall tCall = AmaltheaFactory.eINSTANCE.createTaskRunnableCall();
		tCall.setRunnable(run);

		final CallSequence cs = AmaltheaFactory.eINSTANCE.createCallSequence();
		cs.getCalls().add(tCall);
		cs.setName("Call " + runnableName);

		if (positon.equals(PositionType.FIRST)) {
			process.getCallGraph().getGraphEntries().add(0, cs);
		}
		else {
			// process.getCallGraph().getGraphEntries().add(process.getCallGraph().getGraphEntries().size()
			// -1 , cs);
			process.getCallGraph().getGraphEntries().add(cs);
		}

		return run;
	}


	// #######################################################


	/**
	 * Calculates the utilization for a given procUnit
	 */
	public static double getProcUnitUtilization(final ProcessingUnit procUnit, final Amalthea model, final TimeType tt,
			final List<HwFeature> hwFeatures, final EMap<ModeLabel, ModeLiteral> modes) {
		double utilization = 0.0;

		for (final Process proc : DeploymentUtil.getProcessesMappedToCore(procUnit, model)) {
			utilization += getProcessUtilization(proc, procUnit, model, tt, hwFeatures, modes);
		}

		return utilization;
	}

	/**
	 * Calculates the utilization for a given process
	 *
	 * @return Map: procUnit -&gt; utilization
	 */
	public static Map<ProcessingUnit, Double> getProcessUtilization(final Process process, final Amalthea model,
			final TimeType tt, final List<HwFeature> hwFeatures, final EMap<ModeLabel, ModeLiteral> modes) {
		final HashMap<ProcessingUnit, Double> utilizations = new HashMap<>();

		final Set<ProcessingUnit> procUnits = DeploymentUtil.getAssignedCoreForProcess(process, model);

		for (final ProcessingUnit procUnit : procUnits) {
			final double utilization = getProcessUtilization(process, procUnit, model, tt, hwFeatures, modes);
			utilizations.put(procUnit, utilization);
		}

		return utilizations;
	}

	/**
	 * Calculates the utilization for a given process on a given procUnit
	 * <p>
	 * Assumption (wrong): All triggers activate the process on all procUnits
	 * together! (at the same time)
	 *
	 * @return utilization
	 */
	public static double getProcessUtilization(final Process process, final ProcessingUnit procUnit,
			final Amalthea model, final TimeType tt, final List<HwFeature> hwFeatures,
			final EMap<ModeLabel, ModeLiteral> modes) {
		double utilization = 0.0;

		final List<Time> periods = getPeriodsOfProcess(process, tt, modes);
		final Time time = getExecutionTimeForProcess(process, modes, tt);
		if (time.getValue().compareTo(new BigInteger("0")) < 0) {
			System.err.println("execTime " + time);
		}
		for (final Time period : periods) {
			// System.out.println(process.getName() + " Period:
			// "+TimeUtil.timeToString(period) + " ET:
			// "+TimeUtil.timeToString(time));
			if (period != null && period.getValue().intValue() != 0) {
				utilization += time.divide(period);
			}
		}

		return utilization;
	}

	/**
	 * Calculates the process utilization
	 */
	public static double getProcessUtilization(final Process process, final Time period, final TimeType tt,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final Time time = getExecutionTimeForProcess(process, modes, tt);
		return time.divide(period);
	}

	/**
	 * Returns the cumulative process utilization, i.e. runtime on every
	 * procUnit summed up
	 *
	 * @return Map: process -&gt; sum of utilization on all procUnits
	 */
	public static Map<Process, Double> getCumulativeProcessUtilizations(final Amalthea model, final TimeType tt,
			final List<HwFeature> hwFeatures, final EMap<ModeLabel, ModeLiteral> modes) {
		final HashMap<Process, Double> utilizations = new HashMap<>();

		final List<Process> procs = new ArrayList<Process>();
		procs.addAll(model.getSwModel().getTasks());
		procs.addAll(model.getSwModel().getIsrs());

		for (final Process proc : procs) {
			final Map<ProcessingUnit, Double> processUtilization = getProcessUtilization(proc, model, tt, hwFeatures,
					modes);
			double util = 0.0;
			for (final ProcessingUnit c : processUtilization.keySet()) {
				util += processUtilization.get(c);
			}
			utilizations.put(proc, util);
		}

		return utilizations;
	}

	/**
	 * Gets all Period ranges from the model
	 */
	public static Map<Process, List<Time>> getPeriodsOfAllProcesses(final Amalthea model, final TimeType tt,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final Map<Process, List<Time>> result = new HashMap<>();

		final List<Process> processes = new ArrayList<Process>();
		processes.addAll(model.getSwModel().getTasks());
		processes.addAll(model.getSwModel().getIsrs());

		for (final Process process : processes) {
			final List<Time> periods = getPeriodsOfProcess(process, tt, modes);
			result.put(process, periods);
		}

		return result;
	}

	/**
	 * Returns a list of all triggering periods. Sorted by shortest period
	 * first!
	 */
	public static List<Time> getPeriodsOfProcess(final Process process, final TimeType tt,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final List<Time> result = new ArrayList<Time>();
		// System.out.println(process.getName());
		for (final Stimulus stimulus : process.getStimuli()) {
			// System.out.println(" Stimulus "+stimulus);
			if (stimulus instanceof PeriodicStimulus) {
				final PeriodicStimulus p = ((PeriodicStimulus) stimulus);
				result.add(p.getRecurrence());
			}
			else if (stimulus instanceof ArrivalCurveStimulus) { // Simon
																	// 11.01.2019:
																	// TODO
				switch (tt) {
					case ACET:
						break;
					case BCET:
						break;
					case WCET:
						break;
					default:
						break;
				}
			}
			else if (stimulus instanceof CustomStimulus) {
				switch (tt) {
					case ACET:
						break;
					case BCET:
						break;
					case WCET:
						break;
					default:
						break;
				}
			}
			else if (stimulus instanceof EventStimulus) {
				switch (tt) {
					case ACET:
						break;
					case BCET:
						break;
					case WCET:
						break;
					default:
						break;
				}
			}
			else if (stimulus instanceof InterProcessStimulus) {
				final InterProcessStimulus ip = ((InterProcessStimulus) stimulus);
				long ipPrescaler = 1L;
				if (ip.getCounter() != null) {
					ipPrescaler = ip.getCounter().getPrescaler();
				}
				// System.out.println(" IP found");
				final Map<Process, Long> triggeringProcesses = getTriggeringProcesses(ip, modes);
				if (triggeringProcesses.containsKey(process)) {
					// TODO decided what to do with self triggering processes
					triggeringProcesses.remove(process);
				}

				for (final Process triggeringProcess : triggeringProcesses.keySet()) {
					// System.out.println(" --triggered by
					// "+triggeringProcess.getName());
					// InterProcessActivation ipa = getIpaForStimulus(stimulus,
					// triggeringProcess,
					// modes);
					final List<Time> periods = getPeriodsOfProcess(triggeringProcess, tt, modes);
					for (final Time t : periods) {
						final long ipaPrescaler = triggeringProcesses.get(triggeringProcess);
						// if(ipa.getCounter() != null) {
						// ipaPrescaler = ipa.getCounter().getPrescaler();
						// }
						result.add(t.multiply(ipaPrescaler * ipPrescaler));
					}
				}

				// System.out.println("InterProcess eContainer:
				// "+ip.eContainer());
				// System.out.println(ip);
				// System.out.println(getTriggeringProcess(model, ip));
				// result.addAll(getPeriods(model, getTriggeringProcess(model,
				// ip), tt));

			}
			else if (stimulus instanceof VariableRateStimulus) { // Simon
																	// 11.01.2019:
																	// TODO
				final VariableRateStimulus vr = ((VariableRateStimulus) stimulus);
				// System.out.println(s);
				final Time time = null;
				switch (tt) {
					case ACET:
						// TODO fix it
						// if(vr.getStimulusDeviation() != null) {
						// time = getMean(vr.getStimulusDeviation());
						// }
						// break;
						// case BCET:
						// if(vr.getStimulusDeviation() != null) {
						// time = vr.getStimulusDeviation().getUpperBound();
						// }
						// break;
						// case WCET:
						// if(vr.getStimulusDeviation() != null) {
						// time = vr.getStimulusDeviation().getLowerBound();
						// }
						break;
					default:
						break;
				}

				if (time == null) {
					/*
					 * time = AmaltheaFactory.eINSTANCE.createTime();
					 * time.setValue(0); time.setUnit(TimeUnit.MS);
					 */
				}
				else {
					result.add(time);
				}
			}
			else if (stimulus instanceof SingleStimulus) {
				switch (tt) {
					case ACET:
						break;
					case BCET:
						break;
					case WCET:
						break;
					default:
						break;
				}
			}
			else if (stimulus instanceof RelativePeriodicStimulus) {
				final RelativePeriodicStimulus rp = ((RelativePeriodicStimulus) stimulus);
				Time time = null;
				switch (tt) {
					case ACET:
						// TODO check changes
						if (rp.getNextOccurrence() != null) {
							time = rp.getNextOccurrence().getAverage();
						}
						break;
					case BCET:
						if (rp.getNextOccurrence() != null) {
							time = (rp.getNextOccurrence().getUpperBound());
						}
						break;
					case WCET:
						if (rp.getNextOccurrence() != null) {
							time = (rp.getNextOccurrence().getLowerBound());
						}
						break;
					default:
						break;
				}

				if (time == null) {
					/*
					 * time = AmaltheaFactory.eINSTANCE.createTime();
					 * time.setValue(0); time.setUnit(TimeUnit.MS);
					 */
				}
				else {
					result.add(time);
				}
			}
			else if (stimulus instanceof PeriodicSyntheticStimulus) { // Simon
																		// 11.01.2019:
																		// TODO
				switch (tt) {
					case ACET:
						break;
					case BCET:
						break;
					case WCET:
						break;
					default:
						break;
				}
			}
		}

		Collections.sort(result);

		return result;
	}

	/**
	 * Returns all Processes that trigger the given InterProcessStimulus
	 *
	 * @return Map: process -&gt; prescaler value
	 */
	public static Map<Process, Long> getTriggeringProcesses(final InterProcessStimulus ip,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final Map<Process, Long> result = new HashMap<>();

		for (final InterProcessTrigger interProcessTrigger : ip.getExplicitTriggers()) {
			final Process parentProcess = AmaltheaServices.getContainerOfType(interProcessTrigger, Process.class);
			if (interProcessTrigger.getCounter() != null) {
				result.put(parentProcess, interProcessTrigger.getCounter().getPrescaler());
			}
			else {
				result.put(parentProcess, 1L);
			}
		}

		return result;
	}

	/**
	 * Returns a map of all stimuli, triggered by this process, associated with
	 * its prescaler.
	 */
	public static HashMap<Stimulus, Long> getTriggeredStimuli(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final HashMap<Stimulus, Long> stimuliMap = new HashMap<Stimulus, Long>();
		final List<InterProcessTrigger> interProcessTriggers = SoftwareUtil
				.collectCalls(process, modes, s -> s instanceof InterProcessTrigger).stream()
				.map(s -> (InterProcessTrigger) s).collect(Collectors.toList());

		interProcessTriggers.forEach(ipa -> {
			if (ipa.getCounter() != null) {
				stimuliMap.put(ipa.getStimulus(), ipa.getCounter().getPrescaler());
			}
			else {
				stimuliMap.put(ipa.getStimulus(), 1L);
			}
		});

		return stimuliMap;

	}

	/**
	 * Returns a Map of all sporadically triggered processes with their
	 * prescaler
	 *
	 * @return Map of processes with a sporadic activation and depending on tt
	 *         the time between activations
	 */
	public static Map<Process, List<Time>> getProcessesWithRelativePeriodicStimulus(final Amalthea model,
			final TimeType tt) {
		final Set<Process> processes = new HashSet<>();
		processes.addAll(model.getSwModel().getTasks());
		processes.addAll(model.getSwModel().getIsrs());

		// TODO check
		final Map<Process, List<Time>> result = processes.stream().collect(Collectors.toMap(p -> p,
				p -> p.getStimuli().stream().filter(s -> (s instanceof RelativePeriodicStimulus)).map(
						s -> getActivationTimeFromDeviation(((RelativePeriodicStimulus) s).getNextOccurrence(), tt))
						.collect(Collectors.toList())));

		return result;
	}

	private static Time getActivationTimeFromDeviation(final ITimeDeviation deviation, final TimeType tt) {
		switch (tt) {
			case ACET:
				return deviation.getAverage();
			case BCET:
				return deviation.getUpperBound();
			case WCET:
				return deviation.getLowerBound();
		}
		return null;
	}

	/**
	 * Returns a Map of all sporadically triggered processes with their
	 * prescaler
	 *
	 * @return Map of processes with a sporadic activation and the deviation of
	 *         the activations
	 */
	public static Map<Process, List<ITimeDeviation>> getProcessesWithRelativePeriodicStimulus(final Amalthea model) {
		final List<Process> processes = new ArrayList<>();
		processes.addAll(model.getSwModel().getTasks());
		processes.addAll(model.getSwModel().getIsrs());
		processes.stream().filter(p -> p.getStimuli().stream().filter(s -> (s instanceof RelativePeriodicStimulus))
				.collect(Collectors.toList()).size() > 0);

		// TODO check
		final Map<Process, List<ITimeDeviation>> result = processes.stream()
				.collect(Collectors.toMap(p -> p,
						p -> p.getStimuli().stream().filter(s -> (s instanceof RelativePeriodicStimulus))
								.map(s -> ((RelativePeriodicStimulus) s).getNextOccurrence())
								.collect(Collectors.toList())));

		return result;
	}

	/**
	 * Returns a map from process to all stimuli (matching filter) that lead to
	 * that process with the correct prescaler
	 */
	public static <T> Map<Process, Map<Stimulus, Long>> getPlainTriggersForModel(final Amalthea model,
			final Function<Stimulus, Boolean> filter) {
		final HashMap<Process, Map<Stimulus, Long>> map = new HashMap<>();
		final List<Process> processes = new ArrayList<>();
		final SWModel swModel = ModelUtil.getOrCreateSwModel(model);
		processes.addAll(swModel.getTasks());
		processes.addAll(swModel.getIsrs());

		for (final Process p : processes) {
			final Map<Stimulus, Long> plainTriggerForProcess = getPlainTriggerForProcess(p, 1, filter);
			if (plainTriggerForProcess != null && !plainTriggerForProcess.isEmpty()) {
				map.put(p, plainTriggerForProcess);
			}
		}
		return map;
	}

	/**
	 * Returns all stimuli (matching filter) that trigger the given process
	 */
	public static <T> Map<Stimulus, Long> getPlainTriggerForProcess(final Process process, final long depthCounter,
			final Function<Stimulus, Boolean> filter) {
		final HashMap<Stimulus, Long> map = new HashMap<>();

		for (final Stimulus stimulus : process.getStimuli()) {

			if ((filter == null) || (filter.apply(stimulus))) {

				if (stimulus instanceof PeriodicStimulus) {
					final PeriodicStimulus p = ((PeriodicStimulus) stimulus);
					map.put(p, depthCounter);
				}
				else if (stimulus instanceof ArrivalCurveStimulus) {
					final ArrivalCurveStimulus p = ((ArrivalCurveStimulus) stimulus);
					map.put(p, depthCounter);
				}
				else if (stimulus instanceof CustomStimulus) {
					final CustomStimulus p = ((CustomStimulus) stimulus);
					map.put(p, depthCounter);
				}
				else if (stimulus instanceof EventStimulus) {
					final EventStimulus p = ((EventStimulus) stimulus);
					map.put(p, depthCounter);
				}
				else if (stimulus instanceof InterProcessStimulus) {
					final InterProcessStimulus ip = ((InterProcessStimulus) stimulus);
					long ipPrescaler = 1L;
					if (ip.getCounter() != null) {
						ipPrescaler = ip.getCounter().getPrescaler();
					}
					final Map<Process, Long> triggeringProcesses = getTriggeringProcesses(ip, null);
					for (final Process triggeringProcess : triggeringProcesses.keySet()) {
						if (triggeringProcess.equals(process)) {
							continue; // don't follow loops
						}
						final long ipaPrescaler = triggeringProcesses.get(triggeringProcess);
						final Map<Stimulus, Long> plainTriggerForProcess = getPlainTriggerForProcess(triggeringProcess,
								depthCounter * ipaPrescaler * ipPrescaler, filter);
						map.putAll(plainTriggerForProcess);
					}

				}
				else if (stimulus instanceof VariableRateStimulus) {
					final VariableRateStimulus p = ((VariableRateStimulus) stimulus);
					map.put(p, depthCounter);
				}
				else if (stimulus instanceof SingleStimulus) {
					final SingleStimulus p = ((SingleStimulus) stimulus);
					map.put(p, depthCounter);
				}
				else if (stimulus instanceof RelativePeriodicStimulus) {
					final RelativePeriodicStimulus s = ((RelativePeriodicStimulus) stimulus);
					map.put(s, depthCounter);
				}
				else if (stimulus instanceof PeriodicSyntheticStimulus) {
					final PeriodicSyntheticStimulus p = ((PeriodicSyntheticStimulus) stimulus);
					map.put(p, depthCounter);
				}
			}
			else {
				// not in filter
			}
		}
		return map;
	}

	// idea:
	// remove the (additional) Amalthea parameter in the method signatures
	// and use the following method if necessary
	private static Amalthea getRoot(final @NonNull EObject obj) {
		return AmaltheaServices.getContainerOfType(obj, Amalthea.class);
	}
}

