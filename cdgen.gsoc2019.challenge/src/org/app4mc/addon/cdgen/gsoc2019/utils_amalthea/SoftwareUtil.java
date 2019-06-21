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

package org.app4mc.addon.cdgen.gsoc2019.utils_amalthea;

import static org.eclipse.app4mc.amalthea.model.LabelAccessEnum.READ;
import static org.eclipse.app4mc.amalthea.model.LabelAccessEnum.WRITE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.AmaltheaServices;
import org.eclipse.app4mc.amalthea.model.CallSequence;
import org.eclipse.app4mc.amalthea.model.CallSequenceItem;
import org.eclipse.app4mc.amalthea.model.ClearEvent;
import org.eclipse.app4mc.amalthea.model.ExecutionNeed;
import org.eclipse.app4mc.amalthea.model.GraphEntryBase;
import org.eclipse.app4mc.amalthea.model.Group;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessEnum;
import org.eclipse.app4mc.amalthea.model.LabelAccessStatistic;
import org.eclipse.app4mc.amalthea.model.MinAvgMaxStatistic;
import org.eclipse.app4mc.amalthea.model.ModeLabel;
import org.eclipse.app4mc.amalthea.model.ModeLiteral;
import org.eclipse.app4mc.amalthea.model.ModeSwitch;
import org.eclipse.app4mc.amalthea.model.ModeSwitchEntry;
import org.eclipse.app4mc.amalthea.model.NumericStatistic;
import org.eclipse.app4mc.amalthea.model.ProbabilitySwitch;
import org.eclipse.app4mc.amalthea.model.ProbabilitySwitchEntry;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.RunnableItem;
import org.eclipse.app4mc.amalthea.model.RunnableModeSwitch;
import org.eclipse.app4mc.amalthea.model.RunnableProbabilitySwitch;
import org.eclipse.app4mc.amalthea.model.ServerCall;
import org.eclipse.app4mc.amalthea.model.SetEvent;
import org.eclipse.app4mc.amalthea.model.SingleValueStatistic;
import org.eclipse.app4mc.amalthea.model.TaskRunnableCall;
import org.eclipse.app4mc.amalthea.model.Ticks;
import org.eclipse.app4mc.amalthea.model.WaitEvent;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

public class SoftwareUtil {

	/**
	 * Traverse the call graph of a process and collect all items of the call
	 * sequences.
	 *
	 * @param process
	 *            process (Task or ISR)
	 * @return List of CallSequenceItems
	 */
	public static EList<CallSequenceItem> collectCalls(final Process process) {
		return collectCalls(process, null, CallSequenceItem.class, null);
	}

	/**
	 * Traverse the call graph of a process and collect all items of the call
	 * sequences. Collection can be restricted to specific modes.
	 *
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            list of mode literals that should be considered
	 * @return List of CallSequenceItems
	 */
	public static EList<CallSequenceItem> collectCalls(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectCalls(process, modes, CallSequenceItem.class, null);
	}

	/**
	 * Traverse the call graph of a process and collect all items of the call
	 * sequences. Collection can be restricted to specific modes and filtered by
	 * a lambda expression.
	 *
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            list of mode literals that should be considered
	 * @param filter
	 *            lambda expression (e.g. "a -&gt; a instanceof
	 *            TaskRunnableCall")
	 * @return List of CallSequenceItems
	 */
	public static EList<CallSequenceItem> collectCalls(final Process process, final EMap<ModeLabel, ModeLiteral> modes,
			final Function<CallSequenceItem, Boolean> filter) {
		return collectCalls(process, modes, CallSequenceItem.class, filter);
	}

	/**
	 * Traverse the call graph of a process and collect all items of the call
	 * sequences. Collection can be restricted to specific modes and filtered by
	 * class.
	 *
	 * @param process
	 *            Process (Task or ISR)
	 * @param modes
	 *            list of mode literals that should be considered
	 * @param targetClass
	 *            subclass of CallSequenceItem that restricts the result
	 * @return List of T extends CallSequenceItems
	 */
	public static <T extends CallSequenceItem> EList<T> collectCalls(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes, final Class<T> targetClass) {
		return collectCalls(process, modes, targetClass, null);
	}

	/**
	 * Traverse the call graph of a process and collect all items of the call
	 * sequences. Collection can be restricted to specific modes and filtered by
	 * class and lambda expression.
	 *
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            list of mode literals that should be considered
	 * @param targetClass
	 *            subclass of CallSequenceItem that restricts the result
	 * @param filter
	 *            lambda expression (e.g. "a -&gt; a instanceof
	 *            TaskRunnableCall")
	 * @return List of T extends CallSequenceItems
	 */
	public static <T extends CallSequenceItem> EList<T> collectCalls(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes, final Class<T> targetClass, final Function<T, Boolean> filter) {
		final EList<T> itemList = new BasicEList<T>();
		if (process.getCallGraph() != null) {
			collectCallSequenceItems(process.getCallGraph().getGraphEntries(), modes, targetClass, filter, itemList);
		}
		return itemList;
	}


	private static <T extends CallSequenceItem> void collectCallSequenceItems(final EList<GraphEntryBase> input,
			final EMap<ModeLabel, ModeLiteral> modes, final Class<T> targetClass, final Function<T, Boolean> filter,
			final List<T> itemList) {
		for (final GraphEntryBase entry : input) {
			if (entry instanceof ProbabilitySwitch) {
				final ProbabilitySwitch propSwitch = (ProbabilitySwitch) entry;
				for (final ProbabilitySwitchEntry<GraphEntryBase> pse : propSwitch.getEntries()) {
					collectCallSequenceItems(pse.getItems(), modes, targetClass, filter, itemList);
				}
			}
			else if (entry instanceof ModeSwitch) {
				final ModeSwitch modeSwitch = (ModeSwitch) entry;
				boolean includeDefault = true;
				for (final ModeSwitchEntry<GraphEntryBase> mse : modeSwitch.getEntries()) {
					if (modes == null) {
						collectCallSequenceItems(mse.getItems(), modes, targetClass, filter, itemList);
					}
					else if (mse.getCondition().isSatisfiedBy(modes)) {
						collectCallSequenceItems(mse.getItems(), modes, targetClass, filter, itemList);
						includeDefault = false;
					}
				}
				if (includeDefault && modeSwitch.getDefaultEntry() != null) {
					collectCallSequenceItems(modeSwitch.getDefaultEntry().getItems(), modes, targetClass, filter,
							itemList);
				}
			}
			else if (entry instanceof CallSequence) {
				for (final CallSequenceItem item : ((CallSequence) entry).getCalls()) {
					if (targetClass.isInstance(item)) {
						final T castedItem = targetClass.cast(item);
						if (filter == null || filter.apply(castedItem)) {
							itemList.add(castedItem);
						}
					}
				}
			}
		}
	}

	/**
	 * Traverse the runnable items graph of a runnable and collect all items.
	 *
	 * @param runnable
	 *            runnable
	 * @return List of RunnableItems
	 */
	public static EList<RunnableItem> collectRunnableItems(final Runnable runnable) {
		return collectRunnableItems(runnable, null, RunnableItem.class, null);
	}

	/**
	 * Traverse the runnable items graph of a runnable and collect all items.
	 * Collection can be restricted to specific modes.
	 *
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            list of mode literals that should be considered
	 * @return List of RunnableItems
	 */
	public static EList<RunnableItem> collectRunnableItems(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, RunnableItem.class, null);
	}

	/**
	 * Traverse the runnable items graph of a runnable and collect all items.
	 * Collection can be restricted to specific modes and filtered by a lambda
	 * expression.
	 *
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            list of mode literals that should be considered
	 * @param filter
	 *            lambda expression (e.g. "a -&gt; a instanceof LabelAccess")
	 * @return List of RunnableItems
	 */
	public static EList<RunnableItem> collectRunnableItems(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes, final Function<RunnableItem, Boolean> filter) {
		return collectRunnableItems(runnable, modes, RunnableItem.class, filter);
	}

	/**
	 * Traverse the runnable items graph of a runnable and collect all items.
	 * Collection can be restricted to specific modes and filtered by class.
	 *
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            list of mode literals that should be considered
	 * @param targetClass
	 *            subclass of RunnableItem that restricts the result
	 * @return List of T extends RunnableItems
	 */
	public static <T extends RunnableItem> EList<T> collectRunnableItems(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes, final Class<T> targetClass) {
		return collectRunnableItems(runnable, modes, targetClass, null);
	}

	/**
	 * Traverse the runnable items graph of a runnable and collect all items.
	 * Collection can be restricted to specific modes and filtered by class and
	 * lambda expression.
	 *
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            list of mode literals that should be considered
	 * @param targetClass
	 *            subclass of RunnableItem that restricts the result
	 * @param filter
	 *            lambda expression (e.g. "a -&gt; a instanceof LabelAccess")
	 * @return List of T extends RunnableItems
	 */
	public static <T extends RunnableItem> EList<T> collectRunnableItems(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes, final Class<T> targetClass, final Function<T, Boolean> filter) {
		final EList<T> itemList = new BasicEList<T>();
		collectRunnableItems(runnable.getRunnableItems(), modes, targetClass, filter, itemList);
		return itemList;
	}


	private static <T extends RunnableItem> void collectRunnableItems(final EList<RunnableItem> input,
			final EMap<ModeLabel, ModeLiteral> modes, final Class<T> targetClass, final Function<T, Boolean> filter,
			final List<T> itemList) {
		for (final RunnableItem item : input) {
			if (item instanceof Group) {
				collectRunnableItems(((Group) item).getItems(), modes, targetClass, filter, itemList);
			}
			else if (item instanceof RunnableProbabilitySwitch) {
				final RunnableProbabilitySwitch propSwitch = (RunnableProbabilitySwitch) item;
				for (final ProbabilitySwitchEntry<RunnableItem> pse : propSwitch.getEntries()) {
					collectRunnableItems(pse.getItems(), modes, targetClass, filter, itemList);
				}
			}
			else if (item instanceof RunnableModeSwitch) {
				final RunnableModeSwitch modeSwitch = (RunnableModeSwitch) item;
				boolean includeDefault = true;
				for (final ModeSwitchEntry<RunnableItem> mse : modeSwitch.getEntries()) {
					if (modes == null) {
						collectRunnableItems(mse.getItems(), modes, targetClass, filter, itemList);
					}
					else if (mse.getCondition().isSatisfiedBy(modes)) {
						collectRunnableItems(mse.getItems(), modes, targetClass, filter, itemList);
						includeDefault = false;
					}
				}
				if (includeDefault && modeSwitch.getDefaultEntry() != null) {
					collectRunnableItems(modeSwitch.getDefaultEntry().getItems(), modes, targetClass, filter, itemList);
				}
			}
			else if (targetClass.isInstance(item)) {
				final T castedItem = targetClass.cast(item);
				if (filter == null || filter.apply(castedItem)) {
					itemList.add(castedItem);
				}
			}
		}
	}


	/**
	 * Returns a set of labels accessed from the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Labels
	 */
	public static Set<Label> getAccessedLabelSet(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class).stream().map(la -> la.getData())
				.filter(Objects::nonNull).collect(Collectors.toSet());
	}

	/**
	 * Returns a set of labels read by the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Labels
	 */
	public static Set<Label> getReadLabelSet(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class, (la -> la.getAccess() == READ)).stream()
				.map(la -> la.getData()).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	/**
	 * Returns a set of labels written by the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Labels
	 */
	public static Set<Label> getWriteLabelSet(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class, (la -> la.getAccess() == WRITE)).stream()
				.map(la -> la.getData()).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	/**
	 * Returns a list of all label accesses of the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of LabelAccesses
	 */
	public static List<LabelAccess> getLabelAccessList(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class);
	}

	/**
	 * Returns a list of read label accesses of the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of LabelAccesses
	 */
	public static List<LabelAccess> getReadLabelAccessList(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class, (la -> la.getAccess() == READ));
	}

	/**
	 * Returns a list of write label accesses of the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of LabelAccesses
	 */
	public static List<LabelAccess> getWriteLabelAccessList(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class, (la -> la.getAccess() == WRITE));
	}

	/**
	 * Returns a map: labels accessed from runnable -&gt; the corresponding
	 * label accesses
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccesses
	 */
	public static Map<Label, List<LabelAccess>> getLabelToLabelAccessMap(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class, (la -> la.getData() != null)).stream()
				.collect(Collectors.groupingBy(LabelAccess::getData));
	}

	/**
	 * Returns a map: labels accessed by the runnable -&gt; the corresponding
	 * LabelAccessStatistics
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccessStatistics
	 */
	public static Map<Label, List<LabelAccessStatistic>> getLabelAccessStatisticsMap(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class,
				(la -> la.getData() != null && la.getStatistic() != null)).stream()
						.collect(Collectors.groupingBy(LabelAccess::getData,
								Collectors.mapping(LabelAccess::getStatistic, Collectors.toList())));
	}

	/**
	 * Returns a map: labels read by the runnable -&gt; the corresponding
	 * LabelAccessStatistics
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccessStatistics
	 */
	public static Map<Label, List<LabelAccessStatistic>> getReadLabelAccessStatisticsMap(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class,
				(la -> la.getData() != null && la.getStatistic() != null && la.getAccess() == READ)).stream()
						.collect(Collectors.groupingBy(LabelAccess::getData,
								Collectors.mapping(LabelAccess::getStatistic, Collectors.toList())));
	}

	/**
	 * Returns a map: labels written by the runnable -&gt; the corresponding
	 * LabelAccessStatistics
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccessStatistics
	 */
	public static Map<Label, List<LabelAccessStatistic>> getWriteLabelAccessStatisticsMap(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, LabelAccess.class,
				(la -> la.getData() != null && la.getStatistic() != null && la.getAccess() == WRITE)).stream()
						.collect(Collectors.groupingBy(LabelAccess::getData,
								Collectors.mapping(LabelAccess::getStatistic, Collectors.toList())));
	}

	/**
	 * Returns a set of labels accessed by the process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Labels
	 */
	public static Set<Label> getAccessedLabelSet(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		final HashSet<Label> result = new HashSet<>();
		for (final Runnable r : getRunnableList(process, modes)) {
			result.addAll(getAccessedLabelSet(r, modes));
		}
		return result;
	}

	/**
	 * Returns a set of labels read by the process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Labels
	 */
	public static Set<Label> getReadLabelSet(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		final HashSet<Label> result = new HashSet<>();
		for (final Runnable r : getRunnableList(process, modes)) {
			result.addAll(getReadLabelSet(r, modes));
		}
		return result;
	}

	/**
	 * Returns a set of labels written by the process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Labels
	 */
	public static Set<Label> getWriteLabelSet(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		final HashSet<Label> result = new HashSet<>();
		for (final Runnable r : getRunnableList(process, modes)) {
			result.addAll(getWriteLabelSet(r, modes));
		}
		return result;
	}

	/**
	 * Returns a list of all label accesses of the process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of LabelAccesses
	 */
	public static List<LabelAccess> getLabelAccessList(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final ArrayList<LabelAccess> result = new ArrayList<>();
		for (final Runnable r : getRunnableList(process, modes)) {
			result.addAll(getLabelAccessList(r, modes));
		}
		return result;
	}

	/**
	 * Returns a map: accessed labels of the process -&gt; the corresponding
	 * label accesses
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccess
	 */
	public static Map<Label, List<LabelAccess>> getLabelToLabelAccessMap(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final HashMap<Label, List<LabelAccess>> result = new HashMap<>();
		for (final Runnable r : getRunnableList(process, modes)) {
			final Map<Label, List<LabelAccess>> labelToLabelAccessMap = SoftwareUtil.getLabelToLabelAccessMap(r, modes);
			for (final Label key : labelToLabelAccessMap.keySet()) {
				if (!result.containsKey(key)) {
					result.put(key, new ArrayList<>());
				}
				result.get(key).addAll(labelToLabelAccessMap.get(key));
			}
		}
		return result;
	}

	/**
	 * Returns a map: labels accessed by the process -&gt; the corresponding
	 * label access statistics
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccessStatistic
	 */
	public static Map<Label, List<LabelAccessStatistic>> getLabelAccessStatisticsMap(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final HashMap<Label, List<LabelAccessStatistic>> result = new HashMap<Label, List<LabelAccessStatistic>>();
		for (final Runnable r : getRunnableList(process, modes)) {
			final Map<Label, List<LabelAccessStatistic>> labelToLabelAccessMap = SoftwareUtil
					.getLabelAccessStatisticsMap(r, modes);
			for (final Label key : labelToLabelAccessMap.keySet()) {
				if (!result.containsKey(key)) {
					result.put(key, new ArrayList<>());
				}
				result.get(key).addAll(labelToLabelAccessMap.get(key));
			}
		}
		return result;
	}

	/**
	 * Returns a map: labels read the process -&gt; the corresponding label
	 * access statistics
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccessStatistic
	 */
	public static Map<Label, List<LabelAccessStatistic>> getReadLabelAccessStatisticsMap(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final HashMap<Label, List<LabelAccessStatistic>> result = new HashMap<Label, List<LabelAccessStatistic>>();
		for (final Runnable r : getRunnableList(process, modes)) {
			final Map<Label, List<LabelAccessStatistic>> readLabelAccessStatisticsMap = SoftwareUtil
					.getReadLabelAccessStatisticsMap(r, modes);
			for (final Label key : readLabelAccessStatisticsMap.keySet()) {
				if (!result.containsKey(key)) {
					result.put(key, new ArrayList<>());
				}
				result.get(key).addAll(readLabelAccessStatisticsMap.get(key));
			}
		}
		return result;
	}

	/**
	 * Returns a map: labels written by the process -&gt; the corresponding
	 * label access statistics
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Map: Label -&gt; List of LabelAccessStatistic
	 */
	public static Map<Label, List<LabelAccessStatistic>> getWriteLabelAccessStatisticsMap(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final HashMap<Label, List<LabelAccessStatistic>> result = new HashMap<Label, List<LabelAccessStatistic>>();
		for (final Runnable r : getRunnableList(process, modes)) {
			final Map<Label, List<LabelAccessStatistic>> writeLabelAcessStatisticsMap = SoftwareUtil
					.getWriteLabelAccessStatisticsMap(r, modes);
			for (final Label l : writeLabelAcessStatisticsMap.keySet()) {
				if (!result.containsKey(l)) {
					result.put(l, new ArrayList<>());
				}
				result.get(l).addAll(writeLabelAcessStatisticsMap.get(l));
			}
		}
		return result;
	}

	/**
	 * Returns the reads from a process to a certain label. Evaluates the
	 * LabelAccessStatistic. Definition of TimeType is possible. Null value
	 * returns the average case.
	 */
	public static float getLabelReadCount(final Label label, final Process process,
			final EMap<ModeLabel, ModeLiteral> modes, TimeType timeType) {
		float reads = 0f;
		if (timeType == null) {
			timeType = TimeType.ACET;
		}

		for (final Runnable r : getRunnableList(process, modes)) {
			final List<LabelAccess> readLabelAccessesOfRunnable = getReadLabelAccessList(r, modes);
			for (final LabelAccess la : readLabelAccessesOfRunnable) {
				final NumericStatistic statistic = la.getStatistic().getValue();
				if (statistic instanceof SingleValueStatistic) {
					final SingleValueStatistic svs = (SingleValueStatistic) statistic;
					reads += svs.getValue();
				}
				else if (statistic instanceof MinAvgMaxStatistic) {
					final MinAvgMaxStatistic stat = (MinAvgMaxStatistic) statistic;

					switch (timeType) {
						case ACET:
							reads += stat.getAvg();
							break;
						case BCET:
							reads += stat.getMin();
							break;
						case WCET:
							reads += stat.getMax();
							break;
					}
				}
			}
		}
		return reads;
	}

	/**
	 * Returns the writes from a process to a certain label. Evaluates the
	 * LabelAccessStatistic. Definition of TimeType is possible. Null value
	 * returns the average case.
	 */
	public static float getLabelWriteCount(final Label label, final Process process,
			final EMap<ModeLabel, ModeLiteral> modes, TimeType timeType) {
		float writes = 0f;
		if (timeType == null) {
			timeType = TimeType.ACET;
		}
		for (final Runnable r : getRunnableList(process, modes)) {
			final List<LabelAccess> writeLabelAccessesOfRunnable = getWriteLabelAccessList(r, modes);
			for (final LabelAccess la : writeLabelAccessesOfRunnable) {
				final NumericStatistic statistic = la.getStatistic().getValue();
				if (statistic instanceof SingleValueStatistic) {
					final SingleValueStatistic svs = (SingleValueStatistic) statistic;
					writes += svs.getValue();
				}
				else if (statistic instanceof MinAvgMaxStatistic) {
					final MinAvgMaxStatistic stat = (MinAvgMaxStatistic) statistic;
					switch (timeType) {
						case ACET:
							writes += stat.getAvg();
							break;
						case BCET:
							writes += stat.getMin();
							break;
						case WCET:
							writes += stat.getMax();
							break;
					}
				}
			}
		}
		return writes;
	}

	/**
	 * Returns a list of runnables called by the process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of Runnables
	 */
	public static List<Runnable> getRunnableList(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		return SoftwareUtil.collectCalls(process, modes, TaskRunnableCall.class).stream()
				.map(call -> call.getRunnable()).filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * Returns a set of runnables called by the process - no duplicates
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Set of runnables called by the process
	 */
	public static Set<Runnable> getRunnableSet(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		return new HashSet<>(getRunnableList(process, modes));
	}

	/**
	 * Returns the number of label accesses from a statistic. The accType
	 * defines if the minimum, maximum or average accesses are returned.
	 */
	public static float getLabelAccessCountFromStatistics(final LabelAccess labelAcc, final TimeType accType) {
		float accesses = 1;
		if (labelAcc.getStatistic() == null) {
			return 1;
		}
		if (labelAcc.getStatistic().getValue() instanceof SingleValueStatistic) {
			accesses = ((SingleValueStatistic) labelAcc.getStatistic().getValue()).getValue();
		}
		else if (labelAcc.getStatistic().getValue() instanceof MinAvgMaxStatistic) {
			switch (accType) {
				case ACET:
					accesses = ((MinAvgMaxStatistic) labelAcc.getStatistic().getValue()).getAvg();
					break;
				case BCET:
					accesses = ((MinAvgMaxStatistic) labelAcc.getStatistic().getValue()).getMin();
					break;
				case WCET:
					accesses = ((MinAvgMaxStatistic) labelAcc.getStatistic().getValue()).getMax();
					break;
				default:
					accesses = ((MinAvgMaxStatistic) labelAcc.getStatistic().getValue()).getAvg();
			}
		}
		return accesses;
	}

	/**
	 * Returns a list of runnables reading the label
	 * 
	 * @param label
	 *            label
	 * @param modes
	 *            (optional) - null works
	 * @return List of Runnables
	 */
	public static List<Runnable> getReaderListOfLabel(final Label label, final EMap<ModeLabel, ModeLiteral> modes) {
		final ArrayList<Runnable> result = new ArrayList<>();
		for (final LabelAccess la : label.getLabelAccesses()) {
			if (la.getAccess() == LabelAccessEnum.READ) {
				final Runnable run = AmaltheaServices.getContainerOfType(la, Runnable.class);
				if (modes == null || modes.isEmpty() || (collectRunnableItems(run, modes)).contains(la)) {
					result.add(AmaltheaServices.getContainerOfType(la, Runnable.class));
				}
			}
		}
		return result;
	}

	/**
	 * Returns a set of runnables reading the label
	 * 
	 * @param label
	 *            label
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Labels
	 */
	public static Set<Runnable> getReadersSetOfLabel(final Label label, final EMap<ModeLabel, ModeLiteral> modes) {
		return new HashSet<>(getReaderListOfLabel(label, modes));
	}

	/**
	 * Returns a list of runnables writing the label
	 * 
	 * @param label
	 *            label
	 * @param modes
	 *            (optional) - null works
	 * @return List of Runnables
	 */
	public static List<Runnable> getWriterListOfLabel(final Label label, final EMap<ModeLabel, ModeLiteral> modes) {
		final ArrayList<Runnable> result = new ArrayList<>();
		for (final LabelAccess la : label.getLabelAccesses()) {
			if (la.getAccess().equals(LabelAccessEnum.WRITE)) {
				final Runnable run = AmaltheaServices.getContainerOfType(la, Runnable.class);
				if (modes == null || modes.isEmpty() || (collectRunnableItems(run, modes)).contains(la)) {
					result.add(AmaltheaServices.getContainerOfType(la, Runnable.class));
				}
			}
		}
		return result;
	}

	/**
	 * Returns a set of runnables writing the label
	 * 
	 * @param label
	 *            label
	 * @param modes
	 *            (optional) - null works
	 * @return Set of Runnables
	 */
	public static Set<Runnable> getWriterSetOfLabel(final Label label, final EMap<ModeLabel, ModeLiteral> modes) {
		return new HashSet<>(getWriterListOfLabel(label, modes));
	}

	/**
	 * Collects a list of set events calls for a process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of SetEvents
	 */
	public static List<SetEvent> collectSetEvents(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		return SoftwareUtil.collectCalls(process, modes, SetEvent.class);
	}

	/**
	 * Collects a list of clear event calls for a process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of ClearEvents
	 */
	public static List<ClearEvent> collectClearEvents(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		return SoftwareUtil.collectCalls(process, modes, ClearEvent.class);
	}

	/**
	 * Collects a list of wait event calls for a process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of WaitEvents
	 */
	public static List<WaitEvent> collectWaitEvents(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		return SoftwareUtil.collectCalls(process, modes, WaitEvent.class);
	}

	/**
	 * Collects a list of event calls (clear, set or wait) of a process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of CallSequenceItems
	 */
	public static List<CallSequenceItem> collectEventsOfProcess(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return SoftwareUtil.collectCalls(process, modes,
				(call -> call instanceof ClearEvent || call instanceof SetEvent || call instanceof WaitEvent));
	}

	/**
	 * Returns a list of all exchanged labels that are written by the sender
	 * process and read by the receiver process
	 * 
	 * @param sender
	 *            process (Task or ISR)
	 * @param receiver
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of Labels
	 */
	public static List<Label> getInterTaskCommunication(final Process sender, final Process receiver,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final ArrayList<Label> result = new ArrayList<Label>();
		result.addAll(getWriteLabelSet(sender, modes));
		result.retainAll(getReadLabelSet(receiver, modes));
		return result;
	}

	/**
	 * Returns a list of processes calling the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of Processes
	 */
	public static List<Process> getProcesses(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		final ArrayList<Process> result = new ArrayList<>();
		for (final TaskRunnableCall trc : runnable.getTaskRunnableCalls()) {
			final Process proc = trc.getContainingProcess(); // null if
																// container is
																// not of type
																// Process
			if (proc == null) {
				continue;
			}

			if (modes != null && !modes.isEmpty()) {
				if (getRunnableList(proc, modes).contains(runnable)) {
					result.add(proc);
				}
			}
			else {
				result.add(proc);
			}
		}
		return result;
	}

	/**
	 * Returns a list of runnables directly calling the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of Runnables
	 */
	public static List<Runnable> getRunnableCallParents(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final ArrayList<Runnable> result = new ArrayList<>();
		for (final RunnableCall rc : runnable.getRunnableCalls()) {
			final Runnable run = rc.getContainingRunnable();
			if (run == null) {
				continue;
			}

			if (modes != null && !modes.isEmpty()) {
				final EList<RunnableItem> runItems = collectRunnableItems(run, modes);
				if (runItems != null && runItems.isEmpty()) {
					for (final RunnableItem runItem : runItems) {
						if (runItem instanceof RunnableCall) {
							if (((RunnableCall) runItem).getRunnable().equals(runnable)) {
								result.add(run);
							}
						}
					}
				}
			}
			else {
				result.add(run);
			}
		}
		return result;
	}

	/**
	 * Returns a list of runnables directly called by the runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of Runnables
	 */
	public static List<Runnable> getCalledRunnables(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, RunnableCall.class).stream().map(rc -> rc.getRunnable())
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * Returns a list of all execution needs for a given runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of ExecutionNeeds
	 */
	public static List<ExecutionNeed> getExecutionNeeds(final Runnable runnable,
			final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, ExecutionNeed.class);
	}

	/**
	 * Returns a list of all execution needs for a given process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of ExecutionNeeds
	 */
	public static List<ExecutionNeed> getExecutionNeeds(final Process process,
			final EMap<ModeLabel, ModeLiteral> modes) {
		final List<ExecutionNeed> result = new ArrayList<>();
		final List<Runnable> runnableList = getRunnableList(process, modes);
		for (final Runnable run : runnableList) {
			result.addAll(getExecutionNeeds(run, modes));
		}
		return result;
	}

	/**
	 * Returns a list of all ticks for a given runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return List of Ticks
	 */
	public static List<Ticks> getTicks(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, Ticks.class);
	}

	/**
	 * Returns a list of all ticks for a given process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return List of Ticks
	 */
	public static List<Ticks> getTicks(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		final List<Ticks> result = new ArrayList<>();
		for (final Runnable run : getRunnableList(process, modes)) {
			result.addAll(getTicks(run, modes));
		}
		return result;
	}

	/**
	 * Returns a set of server calls for a runnable
	 * 
	 * @param runnable
	 *            runnable
	 * @param modes
	 *            (optional) - null works
	 * @return Set of ServerCall
	 */
	public static Set<ServerCall> getServerCallSet(final Runnable runnable, final EMap<ModeLabel, ModeLiteral> modes) {
		return collectRunnableItems(runnable, modes, ServerCall.class).stream().collect(Collectors.toSet());
	}

	/**
	 * Returns a set of server calls for a process
	 * 
	 * @param process
	 *            process (Task or ISR)
	 * @param modes
	 *            (optional) - null works
	 * @return Set of ServerCall
	 */
	public static Set<ServerCall> getServerCallSet(final Process process, final EMap<ModeLabel, ModeLiteral> modes) {
		final HashSet<ServerCall> result = new HashSet<>();
		for (final Runnable run : getRunnableList(process, modes)) {
			result.addAll(getServerCallSet(run, modes));
		}
		return result;
	}
}