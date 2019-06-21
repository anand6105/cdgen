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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaServices;
import org.eclipse.app4mc.amalthea.model.Cache;
import org.eclipse.app4mc.amalthea.model.ConnectionHandler;
import org.eclipse.app4mc.amalthea.model.DataRate;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.HwAccessElement;
import org.eclipse.app4mc.amalthea.model.HwAccessPath;
import org.eclipse.app4mc.amalthea.model.HwConnection;
import org.eclipse.app4mc.amalthea.model.HwDestination;
import org.eclipse.app4mc.amalthea.model.HwModule;
import org.eclipse.app4mc.amalthea.model.HwPathElement;
import org.eclipse.app4mc.amalthea.model.HwStructure;
import org.eclipse.app4mc.amalthea.model.IDiscreteValueDeviation;
import org.eclipse.app4mc.amalthea.model.Memory;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.ProcessingUnitDefinition;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.AccessDirection;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;

public class HardwareUtil {

	public static <T extends HwModule> List<T> getModulesFromHwModel(final Class<T> targetClass, final Amalthea model) {
		final List<T> results = new ArrayList<T>();
		for (final HwStructure structure : model.getHwModel().getStructures()) {
			collectModulesFromHWStructure(structure, targetClass, results);
		}
		return results;
	}

	public static <T extends HwModule> List<T> getModulesFromHWStructure(final Class<T> targetClass,
			final HwStructure structure) {
		final List<T> results = new ArrayList<T>();
		collectModulesFromHWStructure(structure, targetClass, results);

		return results;
	}

	private static <T extends HwModule> void collectModulesFromHWStructure(final HwStructure structure,
			final Class<T> targetClass, final List<T> results) {
		// check all modules
		for (final HwModule module : structure.getModules()) {
			if (targetClass.isInstance(module)) {
				results.add(targetClass.cast(module));
			}

			// special handling of processing unit caches
			if (targetClass.isAssignableFrom(Cache.class) && module instanceof ProcessingUnit) {
				for (final Cache containedCache : ((ProcessingUnit) module).getCaches()) {
					if (targetClass.isInstance(containedCache)) {
						results.add(targetClass.cast(containedCache));
					}
				}
			}
		}

		// call method recursive to also get modules of included structures
		for (final HwStructure hwStruct : structure.getStructures()) {
			collectModulesFromHWStructure(hwStruct, targetClass, results);
		}
	}

	public static List<ProcessingUnit> getAllProcessingUnitsForProcessingUnitDefinition(final Amalthea model,
			final ProcessingUnitDefinition puDef) {
		if (puDef == null) { // null is the key for default values!
			return new ArrayList<ProcessingUnit>();
		}

		final List<ProcessingUnit> result = new ArrayList<ProcessingUnit>();
		for (final ProcessingUnit pu : getModulesFromHwModel(ProcessingUnit.class, model)) {
			if (puDef.equals(pu.getDefinition())) {
				result.add(pu);
			}
		}

		return result;
	}

	public static Map<Memory, Long> getMemoryAccessLatenciesCycles(final Amalthea model, final TimeType timeType) {
		final HashMap<Memory, Long> result = new HashMap<Memory, Long>();
		final List<Memory> mems = getModulesFromHwModel(Memory.class, model);
		for (final Memory mem : mems) {
			result.put(mem, calculateLatency(mem.getDefinition().getAccessLatency(), timeType));
		}
		return result;
	}

	public static Map<Memory, Time> getMemoryAccessLatencyTime(final Amalthea model, final TimeType timeType) {
		final HashMap<Memory, Time> result = new HashMap<Memory, Time>();
		final Map<Memory, Long> memoryMap = getMemoryAccessLatenciesCycles(model, timeType);

		for (final Memory key : memoryMap.keySet()) {
			final Time time = RuntimeUtil.getExecutionTimeForCycles((memoryMap.get(key)),
					key.getFrequencyDomain().getDefaultValue());
			result.put(key, time);
		}

		return result;
	}

	public static List<HwAccessElement> getAccessElementsToDestination(final HwDestination dest, final Amalthea model) {
		final List<HwAccessElement> result = new ArrayList<HwAccessElement>();
		final List<ProcessingUnit> pus = getModulesFromHwModel(ProcessingUnit.class, model);
		for (final ProcessingUnit pu : pus) {
			for (final HwAccessElement element : pu.getAccessElements()) {
				if (element.getDestination().equals(dest)) {
					result.add(element);
				}
			}
		}
		return result;
	}

	public static Map<ProcessingUnit, HashMap<HwDestination, Time>> getAccessTimes(final Amalthea model,
			final TimeType timeType, final AccessDirection direction) {
		final Map<ProcessingUnit, HashMap<HwDestination, Time>> coreMemoryLatency = new HashMap<ProcessingUnit, HashMap<HwDestination, Time>>();
		final List<ProcessingUnit> puList = getModulesFromHwModel(ProcessingUnit.class, model);
		for (final ProcessingUnit pu : puList) {
			final HashMap<HwDestination, Time> memoryAccessMap = new HashMap<HwDestination, Time>();
			for (final HwAccessElement accessElement : pu.getAccessElements()) {
				final HwDestination destination = accessElement.getDestination();
				Time latency = null;
				if (accessElement.getAccessPath() != null) {
					latency = calculateHwAccessPathTime(accessElement, timeType, direction);
				}
				else {
					latency = calculateLatencyPathTime(accessElement, timeType, direction);
				}

				final Time previousLatency = memoryAccessMap.get(destination);
				if (previousLatency == null
						|| (latency != null && AmaltheaServices.compareTimes(previousLatency, latency) < 0)) {
					memoryAccessMap.put(destination, latency);
				}
			}
			coreMemoryLatency.put(pu, memoryAccessMap);
		}
		return coreMemoryLatency;
	}

	public static Time calculateLatencyPathTime(final HwAccessElement accessElement, final TimeType timeType,
			final AccessDirection direction) {
		IDiscreteValueDeviation latency = null;
		switch (direction) {
			case READ:
				if (accessElement.getReadLatency() != null) {
					latency = accessElement.getReadLatency();
				}
				break;
			case WRITE:
				if (accessElement.getWriteLatency() != null) {
					latency = accessElement.getWriteLatency();
				}
				break;
			default:
				break;
		}

		return RuntimeUtil.getExecutionTimeForCycles(calculateLatency(latency, timeType),
				accessElement.getSource().getFrequencyDomain().getDefaultValue());
	}

	public static Time calculateHwAccessPathTime(final HwAccessElement accessElement, final TimeType timeType,
			final AccessDirection direction) {
		Time result = FactoryUtil.createTime();
		Frequency frequency = null;
		IDiscreteValueDeviation latency = null;
		if (accessElement.getAccessPath() != null) {
			for (final HwPathElement element : accessElement.getAccessPath().getPathElements()) {
				if (element instanceof ConnectionHandler) {
					if (direction.equals(AccessDirection.READ)) {
						latency = ((ConnectionHandler) element).getDefinition().getReadLatency();
					}
					else {
						latency = ((ConnectionHandler) element).getDefinition().getWriteLatency();
					}
					frequency = getFrequencyOfModule((ConnectionHandler) element);
				}
				else if (element instanceof Cache) {
					latency = ((Cache) element).getDefinition().getAccessLatency();
					frequency = getFrequencyOfModule((Cache) element);
				}
				else if (element instanceof HwConnection) {
					if (direction.equals(AccessDirection.READ)) {
						latency = ((HwConnection) element).getReadLatency();
					}
					else {
						latency = ((HwConnection) element).getWriteLatency();
					}
					// Assumption is that if the frequencyOfComponent is null
					// the HwConnection is
					// the first element in the path, in this case the frequency
					// of the source
					// (ProcessingUnit)
					// is the driver for the frequency. In any other case the
					// element in front of
					// the connection is the driver (The HwPath is an ordered
					// list of path elements)
					if (frequency == null) {
						frequency = getFrequencyOfModule(accessElement.getSource());
					}
				}
				// It is not possible to specify a Read or Write Latencies for
				// ProcessingUnits!
				// In the case this causes some issues in future the interface
				// HwPathElement
				// could be removed from ProcessingUnit
				else if (element instanceof ProcessingUnit) {
					frequency = getFrequencyOfModule((ProcessingUnit) element);
				}

				final Long tmpLatency = calculateLatency(latency, timeType);
				final Time executionTimeForCycles = RuntimeUtil.getExecutionTimeForCycles(tmpLatency, frequency);
				result = result.add(executionTimeForCycles);
			}
		}
		return result.adjustUnit();
	}

	public static Long calculateLatency(final IDiscreteValueDeviation latency, final TimeType timeType) {
		if (latency == null) {
			return 0L;
		}

		switch (timeType) {
			case BCET:
				return latency.getLowerBound();
			case ACET:
				return (long) latency.getAverage();
			case WCET:
				return latency.getUpperBound();
		}

		return (long) latency.getAverage();
	}

	/**
	 * Computes the minimum data rate of an access path
	 */
	public static DataRate getMinDataRateOfHwAccessPath(final HwAccessPath path) {
		DataRate minimum = null;
		if (path != null) {
			for (final HwPathElement element : path.getPathElements()) {
				DataRate temp = null;
				if (element instanceof ConnectionHandler) {
					temp = ((ConnectionHandler) element).getDefinition().getDataRate();
				}
				else if (element instanceof HwConnection) {
					temp = ((HwConnection) element).getDataRate();
				}
				if (temp != null) {
					if (minimum == null || AmaltheaServices.compareDataRates(temp, minimum) < 0) {
						minimum = temp;
					}
				}
			}
		}
		return minimum;
	}

	/**
	 * Returns the frequency of a specific module
	 */
	public static Frequency getFrequencyOfModule(final HwModule module) {
		return module.getFrequencyDomain().getDefaultValue();
	}

	/**
	 * Returns the frequency of a specific module in Hertz
	 */
	public static long getFrequencyOfModuleInHz(final HwModule module) {
		return AmaltheaServices.convertToHertz(getFrequencyOfModule(module)).longValue();
	}
}
