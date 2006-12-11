/*
 * Copyright (C) 2005, 2006 db4objects Inc. (http://www.db4o.com) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 *     Boris Bokowski (IBM Corporation) - bug 118429
 */
package org.eclipse.core.databinding.validation;

import org.eclipse.core.internal.databinding.BindingMessages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * FloatValidator. Verify string to float data conversion
 */
public class String2FloatPrimitiveValidator implements IValidator {

	public IStatus validate(Object value) {
		try {
			Float.parseFloat((String) value);
			return Status.OK_STATUS;
		} catch (Exception e) {
			return ValidationStatus.error(getHint());
		}
	}

	private String getHint() {
		return BindingMessages.getString("Validate_Like") + //$NON-NLS-1$
				BindingMessages.getString("Validate_Number_Examples") //$NON-NLS-1$
				+ Float.MIN_VALUE + ", " + Float.MAX_VALUE + "."; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
