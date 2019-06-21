/**
 ********************************************************************************
 * Copyright (c) 2019 Robert Bosch GmbH and others.
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaServices;
import org.eclipse.app4mc.amalthea.model.ConstraintsModel;
import org.eclipse.app4mc.amalthea.model.LimitType;
import org.eclipse.app4mc.amalthea.model.ProcessRequirement;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.RunnableRequirement;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeMetric;
import org.eclipse.app4mc.amalthea.model.TimeRequirementLimit;
import org.eclipse.emf.ecore.EObject;


public class ConstraintsUtil {

	public static Time getDeadline(final Task task) {
		return getDeadline(task, getConstraintsModel(task));
	}

	public static Time getDeadline(final Task task, final ConstraintsModel constModel) {
		if (task == null || constModel == null) {
			return null;
		}

		final List<Time> list = constModel.getRequirements().stream()
				.filter(req -> req.getLimit().getLimitType() == LimitType.UPPER_LIMIT
						&& req instanceof ProcessRequirement && ((ProcessRequirement) req).getProcess().equals(task)
						&& req.getLimit() instanceof TimeRequirementLimit
						&& ((TimeRequirementLimit) req.getLimit()).getMetric() == TimeMetric.RESPONSE_TIME)
				.map(req -> ((TimeRequirementLimit) req.getLimit()).getLimitValue()).filter(Objects::nonNull)
				.sorted(Time::compareTo).collect(Collectors.toList());
		if (list.isEmpty()) {
			return null;
		}

		return list.get(0);
	}

	public static Time getDeadline(final Runnable runnable) {
		return getDeadline(runnable, getConstraintsModel(runnable));
	}

	public static Time getDeadline(final Runnable runnable, final ConstraintsModel constModel) {
		if (runnable == null || constModel == null) {
			return null;
		}

		final List<Time> list = constModel.getRequirements().stream()
				.filter(req -> req.getLimit().getLimitType() == LimitType.UPPER_LIMIT
						&& req instanceof RunnableRequirement && ((RunnableRequirement) req).getRunnable() == runnable
						&& req.getLimit() instanceof TimeRequirementLimit
						&& ((TimeRequirementLimit) req.getLimit()).getMetric() == TimeMetric.RESPONSE_TIME)
				.map(req -> ((TimeRequirementLimit) req.getLimit()).getLimitValue()).filter(Objects::nonNull)
				.sorted(Time::compareTo).collect(Collectors.toList());
		if (list.isEmpty()) {
			return null;
		}

		return list.get(0);
	}

	public static void updateDeadlineRequirement(final Task task, final Time deadline) {
		updateDeadlineRequirement(task, deadline, getConstraintsModel(task));
	}

	public static void updateDeadlineRequirement(final Task task, final Time deadline,
			final ConstraintsModel constModel) {

		// TODO
	}


	public static void updateDeadlineRequirement(final Runnable runnable, final Time deadline) {
		updateDeadlineRequirement(runnable, deadline, getConstraintsModel(runnable));
	}

	public static void updateDeadlineRequirement(final Runnable runnable, final Time deadline,
			final ConstraintsModel constModel) {

		// TODO
	}

	private static ConstraintsModel getConstraintsModel(final EObject object) {
		if (object == null) {
			return null;
		}

		final Amalthea modelRoot = AmaltheaServices.getContainerOfType(object, Amalthea.class);
		if (modelRoot == null) {
			return null;
		}

		return ModelUtil.getOrCreateConstraintsModel(modelRoot);
	}

}
