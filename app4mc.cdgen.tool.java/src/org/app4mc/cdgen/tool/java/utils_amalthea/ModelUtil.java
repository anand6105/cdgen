/**
 ********************************************************************************
 * Copyright (c) 2015-2018 Robert Bosch GmbH and others.
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

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.CommonElements;
import org.eclipse.app4mc.amalthea.model.ComponentsModel;
import org.eclipse.app4mc.amalthea.model.ConfigModel;
import org.eclipse.app4mc.amalthea.model.ConstraintsModel;
import org.eclipse.app4mc.amalthea.model.EventModel;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.MeasurementModel;
import org.eclipse.app4mc.amalthea.model.OSModel;
import org.eclipse.app4mc.amalthea.model.PropertyConstraintsModel;
import org.eclipse.app4mc.amalthea.model.SWModel;
import org.eclipse.app4mc.amalthea.model.StimuliModel;

public class ModelUtil {

	public static CommonElements getOrCreateCommonElements(final Amalthea model) {
		if (model.getCommonElements() == null) {
			model.setCommonElements(AmaltheaFactory.eINSTANCE.createCommonElements());
		}
		return model.getCommonElements();
	}

	public static SWModel getOrCreateSwModel(final Amalthea model) {
		if (model.getSwModel() == null) {
			model.setSwModel(AmaltheaFactory.eINSTANCE.createSWModel());
		}
		return model.getSwModel();
	}

	public static HWModel getOrCreateHwModel(final Amalthea model) {
		if (model.getHwModel() == null) {
			model.setHwModel(AmaltheaFactory.eINSTANCE.createHWModel());
		}
		return model.getHwModel();
	}

	public static OSModel getOrCreateOsModel(final Amalthea model) {
		if (model.getOsModel() == null) {
			model.setOsModel(AmaltheaFactory.eINSTANCE.createOSModel());
		}
		return model.getOsModel();
	}

	public static StimuliModel getOrCreateStimuliModel(final Amalthea model) {
		if (model.getStimuliModel() == null) {
			model.setStimuliModel(AmaltheaFactory.eINSTANCE.createStimuliModel());
		}
		return model.getStimuliModel();
	}

	public static EventModel getOrCreateEventModel(final Amalthea model) {
		if (model.getEventModel() == null) {
			model.setEventModel(AmaltheaFactory.eINSTANCE.createEventModel());
		}
		return model.getEventModel();
	}

	public static ConstraintsModel getOrCreateConstraintsModel(final Amalthea model) {
		if (model.getConstraintsModel() == null) {
			model.setConstraintsModel(AmaltheaFactory.eINSTANCE.createConstraintsModel());
		}
		return model.getConstraintsModel();
	}

	public static PropertyConstraintsModel getOrCreatePropertyConstraintsModel(final Amalthea model) {
		if (model.getPropertyConstraintsModel() == null) {
			model.setPropertyConstraintsModel(AmaltheaFactory.eINSTANCE.createPropertyConstraintsModel());
		}
		return model.getPropertyConstraintsModel();
	}

	public static MappingModel getOrCreateMappingModel(final Amalthea model) {
		if (model.getMappingModel() == null) {
			model.setMappingModel(AmaltheaFactory.eINSTANCE.createMappingModel());
		}
		return model.getMappingModel();
	}

	public static ComponentsModel getOrCreateComponentsModel(final Amalthea model) {
		if (model.getComponentsModel() == null) {
			model.setComponentsModel(AmaltheaFactory.eINSTANCE.createComponentsModel());
		}
		return model.getComponentsModel();
	}

	public static ConfigModel getOrCreateConfigModel(final Amalthea model) {
		if (model.getConfigModel() == null) {
			model.setConfigModel(AmaltheaFactory.eINSTANCE.createConfigModel());
		}
		return model.getConfigModel();
	}

	public static MeasurementModel getOrCreateMeasurementModel(final Amalthea model) {
		if (model.getMeasurementModel() == null) {
			model.setMeasurementModel(AmaltheaFactory.eINSTANCE.createMeasurementModel());
		}
		return model.getMeasurementModel();
	}

}
