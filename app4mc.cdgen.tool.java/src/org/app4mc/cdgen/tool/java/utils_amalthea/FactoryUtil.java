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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DataRate;
import org.eclipse.app4mc.amalthea.model.DataRateUnit;
import org.eclipse.app4mc.amalthea.model.DataSize;
import org.eclipse.app4mc.amalthea.model.DataSizeUnit;
import org.eclipse.app4mc.amalthea.model.DiscreteValueBoundaries;
import org.eclipse.app4mc.amalthea.model.DiscreteValueConstant;
import org.eclipse.app4mc.amalthea.model.DiscreteValueGaussDistribution;
import org.eclipse.app4mc.amalthea.model.DiscreteValueWeibullEstimatorsDistribution;
import org.eclipse.app4mc.amalthea.model.ExecutionNeed;
import org.eclipse.app4mc.amalthea.model.Frequency;
import org.eclipse.app4mc.amalthea.model.FrequencyUnit;
import org.eclipse.app4mc.amalthea.model.HwFeature;
import org.eclipse.app4mc.amalthea.model.IDiscreteValueDeviation;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.TypeDefinition;
import org.eclipse.app4mc.amalthea.model.TypeRef;
import org.eclipse.app4mc.amalthea.model.Voltage;
import org.eclipse.app4mc.amalthea.model.VoltageUnit;

public class FactoryUtil {

	/**
	 * Creates a data size (initialized with zero value and base unit)
	 */
	public static DataSize createDataSize() {
		final DataSize size = AmaltheaFactory.eINSTANCE.createDataSize();
		size.setValue(BigInteger.ZERO);
		size.setUnit(DataSizeUnit.BIT);
		return size;
	}

	/**
	 * Creates a data size out of a value and a unit
	 */
	public static DataSize createDataSize(final long value, final DataSizeUnit unit) {
		final DataSize size = AmaltheaFactory.eINSTANCE.createDataSize();
		size.setValue(BigInteger.valueOf(value));
		size.setUnit(unit);
		return size;
	}

	/**
	 * Creates a data rate (initialized with zero value and base unit)
	 */
	public static DataRate createDataRate() {
		final DataRate rate = AmaltheaFactory.eINSTANCE.createDataRate();
		rate.setValue(BigInteger.ZERO);
		rate.setUnit(DataRateUnit.BIT_PER_SECOND);
		return rate;
	}

	/**
	 * Creates a data rate out of a value and a unit
	 */
	public static DataRate createDataRate(final long value, final DataRateUnit unit) {
		final DataRate rate = AmaltheaFactory.eINSTANCE.createDataRate();
		rate.setValue(BigInteger.valueOf(value));
		rate.setUnit(unit);
		return rate;
	}

	/**
	 * Creates a frequency (initialized with zero value and base unit)
	 */
	public static Frequency createFrequency() {
		final Frequency frequency = AmaltheaFactory.eINSTANCE.createFrequency();
		frequency.setUnit(FrequencyUnit.HZ);
		return frequency;
	}

	/**
	 * Creates a frequency out of a value and a unit
	 */
	public static Frequency createFrequency(final double value, final FrequencyUnit unit) {
		final Frequency frequency = AmaltheaFactory.eINSTANCE.createFrequency();
		frequency.setValue(value);
		frequency.setUnit(unit);
		return frequency;
	}

	/**
	 * Creates a voltage (initialized with zero value and base unit)
	 */
	public static Voltage createVoltage() {
		final Voltage voltage = AmaltheaFactory.eINSTANCE.createVoltage();
		voltage.setUnit(VoltageUnit.UV);
		return voltage;
	}

	/**
	 * Creates a voltage out of a value and a unit
	 */
	public static Voltage createVoltage(final double value, final VoltageUnit unit) {
		final Voltage voltage = AmaltheaFactory.eINSTANCE.createVoltage();
		voltage.setValue(value);
		voltage.setUnit(unit);
		return voltage;
	}

	/**
	 * Creates a time (initialized with zero value and base unit)
	 */
	public static Time createTime() {
		final Time time = AmaltheaFactory.eINSTANCE.createTime();
		time.setValue(BigInteger.ZERO);
		time.setUnit(TimeUnit.PS);
		return time;
	}

	/**
	 * Creates a new time based on a time
	 * 
	 * @param inputTime
	 * @return New time object
	 */
	public static Time createTime(final Time inputTime) {
		final Time time = AmaltheaFactory.eINSTANCE.createTime();
		time.setValue(inputTime.getValue());
		time.setUnit(inputTime.getUnit());
		return time;
	}

	/**
	 * Creates a time out of a value and a unit
	 */
	public static Time createTime(final long value, final TimeUnit unit) {
		final Time time = AmaltheaFactory.eINSTANCE.createTime();
		time.setValue(BigInteger.valueOf(value));
		time.setUnit(unit);
		return time;
	}

	/**
	 * Creates a time out of a value and a unit
	 */
	public static Time createTime(final BigInteger value, final TimeUnit unit) {
		final Time time = AmaltheaFactory.eINSTANCE.createTime();
		time.setValue(value);
		time.setUnit(unit);
		return time;
	}

	private static TimeUnit parseTimeUnit(final String input) {
		if (input == null) {
			return TimeUnit.MS; // default millisecond
		}

		final String unit = input.trim().toLowerCase();
		if (unit.equals("ps")) {
			return TimeUnit.PS;
		}
		if (unit.equals("ns")) {
			return TimeUnit.NS;
		}
		if (unit.equals("us")) {
			return TimeUnit.US;
		}
		if (unit.equals("ms")) {
			return TimeUnit.MS;
		}
		if (unit.equals("s")) {
			return TimeUnit.S;
		}

		return null;
	}

	/**
	 * Creates a time out of a value and a unit given as String.
	 */
	public static Time createTime(final long value, final String unit) {
		return createTime(value, parseTimeUnit(unit));
	}

	/**
	 * Creates a time out of a value and a unit given as String.
	 */
	public static Time createTime(final BigInteger value, final String unit) {
		return createTime(value, parseTimeUnit(unit));
	}

	/**
	 * Creates a Time object parsed from a text representation.
	 * 
	 * @param timeString
	 *            string representation of a time (number followed by time unit
	 *            s, ms, us, ns or ps)
	 * @return The new Time object
	 *
	 */
	public static Time createTime(final String timeString) {
		final Pattern p = Pattern.compile("(\\d+)\\s?(s|ms|us|ns|ps)");
		final Matcher m = p.matcher(timeString);
		if (m.matches()) {
			final String value = m.group(1);
			final String unit = m.group(2);
			return createTime(Long.parseLong(value), parseTimeUnit(unit));
		}
		return null;
	}

	/**
	 * Creates a TypeRef object that refers to a type definition
	 */
	public static TypeRef createTypeRef(final TypeDefinition typeDefinition) {
		final TypeRef refObj = AmaltheaFactory.eINSTANCE.createTypeRef();
		refObj.setTypeDef(typeDefinition);
		;
		return refObj;
	}

	public static DiscreteValueConstant createDiscreteValueConstant(final long value) {
		final DiscreteValueConstant result = AmaltheaFactory.eINSTANCE.createDiscreteValueConstant();
		result.setValue(value);
		;
		return result;
	}

	public static DiscreteValueBoundaries createDiscreteValueBoundaries(final long min, final long max) {
		final DiscreteValueBoundaries boundaries = AmaltheaFactory.eINSTANCE.createDiscreteValueBoundaries();
		boundaries.setLowerBound(min);
		boundaries.setUpperBound(max);
		return boundaries;
	}

	public static DiscreteValueGaussDistribution createDiscreteValueGaussDistribution(final double mean,
			final double sd) {
		final DiscreteValueGaussDistribution result = AmaltheaFactory.eINSTANCE.createDiscreteValueGaussDistribution();
		result.setMean(mean);
		result.setSd(sd);
		return result;
	}

	public static DiscreteValueGaussDistribution createDiscreteValueGaussDistribution(final double mean,
			final double sd, final Long min, final Long max) {
		final DiscreteValueGaussDistribution result = createDiscreteValueGaussDistribution(mean, sd);
		result.setLowerBound(min);
		result.setUpperBound(max);
		return result;
	}

	public static DiscreteValueWeibullEstimatorsDistribution createWeibullDistribution(final long min, final double avg,
			final long max, final double promille) {
		final DiscreteValueWeibullEstimatorsDistribution result = AmaltheaFactory.eINSTANCE
				.createDiscreteValueWeibullEstimatorsDistribution();
		result.setLowerBound(min);
		result.setAverage(avg);
		result.setUpperBound(max);
		result.setPRemainPromille(promille);
		return result;
	}

	/**
	 * Creates ExecutionNeed for a featureCategoryName, need set.
	 */
	public static ExecutionNeed createExecutionNeed(final String featureCategory,
			final IDiscreteValueDeviation usages) {
		final ExecutionNeed exeNeed = AmaltheaFactory.eINSTANCE.createExecutionNeed();
		exeNeed.getNeeds().put(featureCategory, usages);
		return exeNeed;
	}

	/**
	 * Creates ExecutionNeed for a feature, need set.
	 */
	public static ExecutionNeed createExecutionNeed(final HwFeature feature, final IDiscreteValueDeviation usages) {
		final ExecutionNeed exeNeed = AmaltheaFactory.eINSTANCE.createExecutionNeed();
		exeNeed.getNeeds().put(feature.getContainingCategory().getName(), usages);
		return exeNeed;
	}

	public static DiscreteValueConstant createLatency(final long value) {
		return createDiscreteValueConstant(value);
	}

}
