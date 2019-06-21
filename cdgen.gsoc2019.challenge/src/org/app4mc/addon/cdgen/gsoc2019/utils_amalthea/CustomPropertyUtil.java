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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.IAnnotatable;
import org.eclipse.app4mc.amalthea.model.IReferable;
import org.eclipse.app4mc.amalthea.model.IntegerObject;
import org.eclipse.app4mc.amalthea.model.ReferenceObject;
import org.eclipse.app4mc.amalthea.model.StringObject;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.Value;
import org.eclipse.jdt.annotation.NonNull;


public final class CustomPropertyUtil {

	private static final String ARG1_MESSAGE = "First argument is null, expected instance of IAnnotatable";
	private static final String ARG2_MESSAGE = "Key is null or empty, expected non empty String";

	public static Value customPut(@NonNull final IAnnotatable object, @NonNull final String key, final int num) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		IntegerObject valueObject;
		valueObject = AmaltheaFactory.eINSTANCE.createIntegerObject();
		valueObject.setValue(num);
		final Value oldValue = object.getCustomProperties().put(key, valueObject);

		return oldValue;
	}

	public static Value customPut(@NonNull final IAnnotatable object, @NonNull final String key, final String str) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		StringObject valueObject;
		valueObject = AmaltheaFactory.eINSTANCE.createStringObject();
		valueObject.setValue(str);
		final Value oldValue = object.getCustomProperties().put(key, valueObject);

		return oldValue;
	}

	public static Value customPut(@NonNull final IAnnotatable object, @NonNull final String key, final Time time) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		Time valueObject;
		valueObject = AmaltheaFactory.eINSTANCE.createTime();
		valueObject.setValue(time.getValue());
		valueObject.setUnit(time.getUnit());
		final Value oldValue = object.getCustomProperties().put(key, valueObject);

		return oldValue;
	}

	public static Value customPut(@NonNull final IAnnotatable object, @NonNull final String key,
			final IReferable reference) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		ReferenceObject valueObject;
		valueObject = AmaltheaFactory.eINSTANCE.createReferenceObject();
		valueObject.setValue(reference);
		final Value oldValue = object.getCustomProperties().put(key, valueObject);

		return oldValue;
	}

	public static Integer customGetInteger(@NonNull final IAnnotatable object, @NonNull final String key) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		final Value valueObject = object.getCustomProperties().get(key);
		if (valueObject instanceof IntegerObject) {
			return ((IntegerObject) valueObject).getValue();
		}
		else {
			return null;
		}
	}

	public static String customGetString(@NonNull final IAnnotatable object, @NonNull final String key) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		final Value valueObject = object.getCustomProperties().get(key);
		if (valueObject instanceof StringObject) {
			return ((StringObject) valueObject).getValue();
		}
		else {
			return null;
		}
	}

	public static Time customGetTime(@NonNull final IAnnotatable object, @NonNull final String key) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		final Value valueObject = object.getCustomProperties().get(key);
		if (valueObject instanceof Time) {
			final Time time = (Time) valueObject;
			final Time result = AmaltheaFactory.eINSTANCE.createTime();
			result.setValue(time.getValue());
			result.setUnit(time.getUnit());
			return result;
		}
		else {
			return null;
		}
	}

	public static IReferable customGetReference(@NonNull final IAnnotatable object, @NonNull final String key) {
		checkArgument(object != null, ARG1_MESSAGE);
		checkArgument(!isNullOrEmpty(key), ARG2_MESSAGE);

		final Value valueObject = object.getCustomProperties().get(key);
		if (valueObject instanceof ReferenceObject) {
			return ((ReferenceObject) valueObject).getValue();
		}
		else {
			return null;
		}
	}

}
