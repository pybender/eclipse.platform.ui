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

package org.eclipse.jface.internal.databinding.api.observable.set;

import java.util.Set;

import org.eclipse.jface.internal.databinding.api.observable.IDiff;

/**
 * @since 1.0
 *
 */
public abstract class SetDiff implements IDiff {
	
	/**
	 * @return the set of added elements
	 */
	public abstract Set getAdditions();
	
	/**
	 * @return the set of removed elements
	 */
	public abstract Set getRemovals();
}
