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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.DiscreteValueConstant;
import org.eclipse.app4mc.amalthea.model.ExecutionNeed;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.HwFeature;
import org.eclipse.app4mc.amalthea.model.HwFeatureCategory;
import org.eclipse.app4mc.amalthea.model.IDiscreteValueDeviation;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.ProcessingUnitDefinition;
import org.eclipse.app4mc.amalthea.model.Ticks;

/**
 * Creates Need entries for Instructions
 *
 * @deprecated (0.9.3) use {@link Ticks} instead.
 */
@Deprecated
public class InstructionsUtil {

	public static final String INSTRUCTIONS_CATEGORY_NAME = "Instructions";

	public static HwFeatureCategory getOrCreateInstructionsCategory(final Amalthea model) {
		final HWModel hwModel = ModelUtil.getOrCreateHwModel(model);
		for (final HwFeatureCategory category : hwModel.getFeatureCategories()) {
			if (null != category.getName() && category.getName().equals(INSTRUCTIONS_CATEGORY_NAME)) {
				return category;
			}
		}

		// create missing category
		final HwFeatureCategory newCategory = AmaltheaFactory.eINSTANCE.createHwFeatureCategory();
		newCategory.setName(INSTRUCTIONS_CATEGORY_NAME);
		hwModel.getFeatureCategories().add(newCategory);

		return newCategory;
	}

	public static ExecutionNeed createExecutionNeedConstant(final long instructions) {
		return createExecutionNeed(INSTRUCTIONS_CATEGORY_NAME, instructions);
	}

	public static IDiscreteValueDeviation getNeed(final ExecutionNeed execNeed) {
		return execNeed.getNeeds().get(INSTRUCTIONS_CATEGORY_NAME);
	}

	public static long getNeedConstant(final ExecutionNeed execNeed) {
		return getNeedConstant(execNeed, INSTRUCTIONS_CATEGORY_NAME);
	}

	public static IDiscreteValueDeviation getNeedDeviation(final ExecutionNeed execNeed) {
		return getNeedDeviation(execNeed, INSTRUCTIONS_CATEGORY_NAME);
	}


	public static double getIPC(final Amalthea model, final ProcessingUnit pu) {
		final List<Double> values = getFeatureValuesOfCategory(pu.getDefinition(),
				getOrCreateInstructionsCategory(model));
		if (!values.isEmpty()) {
			return values.get(0); // take first value
		}
		return 1.0; // default
	}


	// ----------------------- General -----------------------


	private static ExecutionNeed createExecutionNeed(final String category, final long value) {
		final DiscreteValueConstant dev = AmaltheaFactory.eINSTANCE.createDiscreteValueConstant();
		dev.setValue(value);

		final ExecutionNeed execNeed = AmaltheaFactory.eINSTANCE.createExecutionNeed();
		execNeed.getNeeds().put(category, dev);

		return execNeed;
	}

	private static long getNeedConstant(final ExecutionNeed execNeed, final String category) {
		if (execNeed == null || category == null) {
			return 0;
		}

		final IDiscreteValueDeviation dev = execNeed.getNeeds().get(category);
		if (dev instanceof DiscreteValueConstant) {
			return ((DiscreteValueConstant) dev).getValue();
		}
		return 0;
	}

	private static IDiscreteValueDeviation getNeedDeviation(final ExecutionNeed execNeed, final String category) {
		if (execNeed == null || category == null) {
			return null;
		}

		return execNeed.getNeeds().get(category);
	}

	public static List<HwFeature> getFeaturesOfCategory(final ProcessingUnitDefinition puDefinition,
			final HwFeatureCategory category) {
		if (puDefinition == null || category == null) {
			return Collections.<HwFeature> emptyList();
		}

		return puDefinition.getFeatures().stream().filter(i -> i.getContainingCategory() == category)
				.collect(Collectors.toList());
	}

	public static List<Double> getFeatureValuesOfCategory(final ProcessingUnitDefinition puDefinition,
			final HwFeatureCategory category) {
		if (puDefinition == null || category == null) {
			return Collections.<Double> emptyList();
		}

		return puDefinition.getFeatures().stream().filter(i -> i.getContainingCategory() == category)
				.map(i -> i.getValue()).collect(Collectors.toList());
	}


}

