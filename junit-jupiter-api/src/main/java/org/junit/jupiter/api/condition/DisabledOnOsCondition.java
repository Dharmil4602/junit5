/*
 * Copyright 2015-2022 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api.condition;

import static org.junit.jupiter.api.condition.EnabledOnOsCondition.DISABLED_ON_CURRENT_OS;
import static org.junit.jupiter.api.condition.EnabledOnOsCondition.ENABLED_ON_CURRENT_OS;

import java.util.Arrays;

import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.platform.commons.util.Preconditions;

/**
 * {@link ExecutionCondition} for {@link DisabledOnOs @DisabledOnOs}.
 *
 * @since 5.1
 * @see DisabledOnOs
 */
class DisabledOnOsCondition extends BooleanExecutionCondition<DisabledOnOs> {

	DisabledOnOsCondition() {
		super(DisabledOnOs.class, ENABLED_ON_CURRENT_OS, DISABLED_ON_CURRENT_OS, DisabledOnOs::disabledReason);
	}

	@Override
	boolean isEnabled(DisabledOnOs annotation) {
		Preconditions.condition(annotation.value().length > 0 || annotation.architectures().length > 0,
			"You must declare at least one OS or architecture in @DisabledOnOs");
		return isEnabledBasedOnOs(annotation) || isEnabledBasedOnArchitecture(annotation);
	}

	private boolean isEnabledBasedOnArchitecture(DisabledOnOs annotation) {
		String[] architectures = annotation.architectures();
		if (architectures.length == 0) {
			return false;
		}

		String currentArchitecture = getArchitecture();
		return Arrays.stream(architectures).noneMatch(currentArchitecture::equalsIgnoreCase);
	}

	private boolean isEnabledBasedOnOs(DisabledOnOs annotation) {
		OS[] operatingSystems = annotation.value();
		if (operatingSystems.length == 0) {
			return false;
		}

		return Arrays.stream(operatingSystems).noneMatch(OS::isCurrentOs);
	}

	protected String getArchitecture() {
		return System.getProperty("os.arch");
	}

}
