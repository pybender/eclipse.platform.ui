/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tests.decorators;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;

/**
 * The BackgroundColorDecorator is a test for background coloring of the
 * navigator.
 */
public class BackgroundColorDecorator implements ILightweightLabelDecorator {

	public static final String ID = "org.eclipse.ui.tests.backgroundDecorator";

	public static Color color;


	@Override
	public void decorate(Object element, IDecoration decoration) {

		if(color == null){
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					setUpColor();

				}
			});

		}
		decoration.setBackgroundColor(color);

	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}
	public static void setUpColor(){
		color = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_CYAN);
	}


}
