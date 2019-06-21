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
import java.util.List;

import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.AmaltheaServices;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;

public class TimeUtil {

	/**
	 * Returns a list with all possible Units
	 * 
	 * @return TimeUnit.VALUES
	 */
	public static List<TimeUnit> getTimeUnitList() {
		return AmaltheaServices.TIME_UNIT_LIST;
	}

	/**
	 * Converts a Time object to the given TimeUnit. Note: when convert from
	 * small unit to bigger unit, the function will round the number e.g 5200Ps
	 * = 5Ns
	 */
	public static Time convertToTimeUnit(final Time time, final TimeUnit unit) {
		if (time.getUnit() == unit) {
			// Units are the same, no conversion
			return time;
		}

		if (time.getUnit() == TimeUnit._UNDEFINED_ || unit == TimeUnit._UNDEFINED_) {
			return null;
		}

		final Time newTime = AmaltheaFactory.eINSTANCE.createTime();

		final List<TimeUnit> units = getTimeUnitList();
		final int power = units.indexOf(time.getUnit()) - units.indexOf(unit);
		// Get the difference from the source TimeUnit to the destination
		// TimeUnit as a factor
		double factor = Math.pow(1000, power);

		// Convert
		if (factor >= 1.0) {
			newTime.setValue(time.getValue().multiply(BigInteger.valueOf((long) factor)));
		}
		else {
			factor = 1 / factor;
			newTime.setValue(time.getValue().divide(BigInteger.valueOf((long) factor)));
		}
		newTime.setUnit(unit);

		return newTime;
	}
	
	public static double getAsTimeUnit(final Time time, final TimeUnit unit) {
		if (time.getUnit() == unit) {
			// Units are the same, no conversion
			return 0;
		}

		if (time.getUnit() == TimeUnit._UNDEFINED_ || unit == TimeUnit._UNDEFINED_) {
			return 0;
		}

		final List<TimeUnit> units = getTimeUnitList();
		final int power = units.indexOf(time.getUnit()) - units.indexOf(unit);
		// Get the difference from the source TimeUnit to the destination
		// TimeUnit as a factor
		double factor = Math.pow(1000, power);
		double timeValue = time.getValue().doubleValue();
		return timeValue * factor;
	}
}
