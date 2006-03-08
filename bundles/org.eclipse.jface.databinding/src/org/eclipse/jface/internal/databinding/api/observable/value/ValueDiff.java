/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.jface.internal.databinding.api.observable.value;

import org.eclipse.jface.internal.databinding.api.observable.Diffs;
import org.eclipse.jface.internal.databinding.api.observable.IDiff;

/**
 * @since 1.0
 * 
 */
public abstract class ValueDiff implements IDiff {

	/**
	 * Creates a value diff.
	 */
	public ValueDiff() {
	}

	/**
	 * @return the old value
	 */
	public abstract Object getOldValue();

	/**
	 * @return the new value
	 */
	public abstract Object getNewValue();

	public boolean equals(Object obj) {
		if (obj instanceof ValueDiff) {
			ValueDiff val = (ValueDiff) obj;

			return Diffs.equals(val.getNewValue(), getNewValue())
					&& Diffs.equals(val.getOldValue(), getOldValue());

		}
		return false;
	}

}
