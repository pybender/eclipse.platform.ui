/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.ide.actions;

import java.util.HashSet;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * This class contains convenience methods used by the various build commands
 * to determine enablement.  These utilities cannot be factored into a common
 * class because some build actions are API and some are not.
 * 
 * @since 3.1
 */
public class BuildUtilities {
	/**
	 * Extracts the selected projects from a selection.
	 * 
	 * @param selection The selection to analyze
	 * @return The selected projects
	 */
	public static IProject[] extractProjects(Object[] selection) {
		HashSet projects = new HashSet();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof IResource) {
				projects.add(((IResource) selection[i]).getProject());
			} else if (selection[i] instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selection[i];
				IResource resource = (IResource) adaptable.getAdapter(IResource.class);
				if (resource != null)
					projects.add(resource.getProject());
			}
		}
		return (IProject[]) projects.toArray(new IProject[projects.size()]);
	}

	/**
	 * Finds and returns the selected projects in the given window
	 * 
	 * @param window The window to find the selection in
	 * @return The selected projects, or an empty array if no selection could be found.
	 */
	public static IProject[] findSelectedProjects(IWorkbenchWindow window) {
		if (window == null)
			return new IProject[0];
		ISelection selection = window.getSelectionService().getSelection();
		IProject[] selected = null;
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			selected = extractProjects(((IStructuredSelection) selection).toArray());
		} else {
			//see if we can extract a selected project from the active editor
			IWorkbenchPart part = window.getPartService().getActivePart();
			if (part instanceof IEditorPart) {
				IEditorInput input = ((IEditorPart) part).getEditorInput();
				if (input instanceof IFileEditorInput)
					selected = new IProject[] {((IFileEditorInput) input).getFile().getProject()};
			}
		}
		if (selected == null)
			selected = new IProject[0];
		return selected;
	}

	/**
	 * Returns whether a build command with the given trigger should
	 * be enabled for the given selection.
	 * @param projects The projects to use to determine enablement
	 * @param trigger The build trigger (<code>IncrementalProjectBuilder.*_BUILD</code> constants).
	 * @return <code>true</code> if the action should be enabled, and
	 * <code>false</code> otherwise.
	 */
	public static boolean isEnabled(IProject[] projects, int trigger) {
		//incremental build is only enabled if all projects are not autobuilding
		if (trigger == IncrementalProjectBuilder.INCREMENTAL_BUILD && ResourcesPlugin.getWorkspace().isAutoBuilding()) {
			if (!matchingTrigger(projects, IncrementalProjectBuilder.AUTO_BUILD, false))
				return false;
		}
		//finally we are building only if there is a builder that accepts the trigger
		return matchingTrigger(projects, trigger, true);
	}

	/**
	 * Returns whether one of the projects has a builder whose trigger setting
	 * for the given trigger matches the given value.
	 * 
	 * @param projects The projects to check
	 * @param trigger The trigger to look for
	 * @param value The trigger value to look for
	 * @return <code>true</code> if one of the projects has a builder whose
	 * trigger activation matches the provided value, and <code>false</code> otherwise.
	 */
	private static boolean matchingTrigger(IProject[] projects, int trigger, boolean value) {
		for (int i = 0; i < projects.length; i++) {
			if (!projects[i].isAccessible())
				continue;
			try {
				IProjectDescription description = projects[i].getDescription();
				ICommand[] buildSpec = description.getBuildSpec();
				for (int j = 0; j < buildSpec.length; j++) {
					if (buildSpec[j].isBuilding(trigger) == value)
						return true;
				}
			} catch (CoreException e) {
				//ignore projects that are not available
			}
		}
		return false;
	}

	/**
	 * Doesn't need to be instantiated
	 */
	private BuildUtilities() {
	}
}
