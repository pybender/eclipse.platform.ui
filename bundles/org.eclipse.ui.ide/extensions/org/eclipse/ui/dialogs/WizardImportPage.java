/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.dialogs;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

/**
 * The abstract superclass for a typical import wizard's main page.
 * <p>
 * Clients may subclass this page to inherit its common destination resource
 * selection facilities.
 * </p>
 * <p>
 * Subclasses must implement
 * <ul>
 *   <li><code>createSourceGroup</code></li>
 * </ul>
 * </p>
 * <p>
 * Subclasses may override
 * <ul>
 *   <li><code>allowNewContainerName</code></li>
 * </ul>
 * </p>
 * <p>
 * Subclasses may extend
 * <ul>
 *   <li><code>handleEvent</code></li>
 * </ul>
 * </p>
 * @deprecated use WizardResourceImportPage
 */
@Deprecated
public abstract class WizardImportPage extends WizardDataTransferPage {
    private IResource currentResourceSelection;

    // initial value stores
    private String initialContainerFieldValue;

    // widgets
    private Text containerNameField;

    private Button containerBrowseButton;

    /**
     * Creates an import wizard page. If the initial resource selection
     * contains exactly one container resource then it will be used as the default
     * import destination.
     *
     * @param name the name of the page
     * @param selection the current resource selection
     */
    protected WizardImportPage(String name, IStructuredSelection selection) {
        super(name);

        if (selection.size() == 1) {
			currentResourceSelection = (IResource) selection.getFirstElement();
		} else {
			currentResourceSelection = null;
		}

        if (currentResourceSelection != null) {
            if (currentResourceSelection.getType() == IResource.FILE) {
				currentResourceSelection = currentResourceSelection.getParent();
			}

            if (!currentResourceSelection.isAccessible()) {
				currentResourceSelection = null;
			}
        }

    }

    /**
     * The <code>WizardImportPage</code> implementation of this
     * <code>WizardDataTransferPage</code> method returns <code>true</code>.
     * Subclasses may override this method.
     */
    @Override
	protected boolean allowNewContainerName() {
        return true;
    }

    @Override
	public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
                | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        createSourceGroup(composite);

        createSpacer(composite);

        createBoldLabel(composite, IDEWorkbenchMessages.WizardImportPage_destinationLabel);
        createDestinationGroup(composite);

        createSpacer(composite);

        createBoldLabel(composite, IDEWorkbenchMessages.WizardImportPage_options);
        createOptionsGroup(composite);

        restoreWidgetValues();
        updateWidgetEnablements();
        setPageComplete(determinePageCompletion());

        setControl(composite);
    }

    /**
     * Creates the import destination specification controls.
     *
     * @param parent the parent control
     */
    protected final void createDestinationGroup(Composite parent) {
        // container specification group
        Composite containerGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        containerGroup.setLayout(layout);
        containerGroup.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

        // container label
        Label resourcesLabel = new Label(containerGroup, SWT.NONE);
        resourcesLabel.setText(IDEWorkbenchMessages.WizardImportPage_folder);

        // container name entry field
        containerNameField = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
        containerNameField.addListener(SWT.Modify, this);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        containerNameField.setLayoutData(data);

        // container browse button
        containerBrowseButton = new Button(containerGroup, SWT.PUSH);
        containerBrowseButton.setText(IDEWorkbenchMessages.WizardImportPage_browseLabel);
        containerBrowseButton.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL));
        containerBrowseButton.addListener(SWT.Selection, this);

        initialPopulateContainerField();
    }

    /**
     * Creates the import source specification controls.
     * <p>
     * Subclasses must implement this method.
     * </p>
     *
     * @param parent the parent control
     */
    protected abstract void createSourceGroup(Composite parent);

    /**
     * Display an error dialog with the specified message.
     *
     * @param message the error message
     */
    @Override
	protected void displayErrorDialog(String message) {
        MessageDialog.open(MessageDialog.ERROR, getContainer().getShell(), IDEWorkbenchMessages.WizardImportPage_errorDialogTitle, message, SWT.SHEET);
    }

    /**
     * Returns the path of the container resource specified in the container
     * name entry field, or <code>null</code> if no name has been typed in.
     * <p>
     * The container specified by the full path might not exist and would need to
     * be created.
     * </p>
     *
     * @return the full path of the container resource specified in
     *   the container name entry field, or <code>null</code>
     */
    protected IPath getContainerFullPath() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

        //make the path absolute to allow for optional leading slash
        IPath testPath = getResourcePath();

        IStatus result = workspace.validatePath(testPath.toString(),
                IResource.PROJECT | IResource.FOLDER);
        if (result.isOK()) {
            return testPath;
        }

        return null;
    }

    /**
     * Return the path for the resource field.
     * @return org.eclipse.core.runtime.IPath
     */
    protected IPath getResourcePath() {
        return getPathFromText(this.containerNameField);
    }

    /**
     * Returns the container resource specified in the container name entry field,
     * or <code>null</code> if such a container does not exist in the workbench.
     *
     * @return the container resource specified in the container name entry field,
     *   or <code>null</code>
     */
    protected IContainer getSpecifiedContainer() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();
        IPath path = getContainerFullPath();
        if (workspace.getRoot().exists(path)) {
			return (IContainer) workspace.getRoot().findMember(path);
		}

        return null;
    }

    /**
     * Opens a container selection dialog and displays the user's subsequent
     * container resource selection in this page's container name field.
     */
    protected void handleContainerBrowseButtonPressed() {
        // see if the user wishes to modify this container selection
        IPath containerPath = queryForContainer(getSpecifiedContainer(),
                IDEWorkbenchMessages.WizardImportPage_selectFolderLabel);

        // if a container was selected then put its name in the container name field
        if (containerPath != null) {
			containerNameField.setText(containerPath.makeRelative().toString());
		}
    }

    /**
     * The <code>WizardImportPage</code> implementation of this
     * <code>Listener</code> method handles all events and enablements for controls
     * on this page. Subclasses may extend.
     */
    @Override
	public void handleEvent(Event event) {
        Widget source = event.widget;

        if (source == containerBrowseButton) {
			handleContainerBrowseButtonPressed();
		}

        setPageComplete(determinePageCompletion());
        updateWidgetEnablements();
    }

    /**
     * Sets the initial contents of the container name field.
     */
    protected final void initialPopulateContainerField() {
        if (initialContainerFieldValue != null) {
			containerNameField.setText(initialContainerFieldValue);
		} else if (currentResourceSelection != null) {
			containerNameField.setText(currentResourceSelection.getFullPath()
                    .toString());
		}
    }

    /**
     * Sets the value of this page's container resource field, or stores
     * it for future use if this page's controls do not exist yet.
     *
     * @param value new value
     */
    public void setContainerFieldValue(String value) {
        if (containerNameField == null) {
			initialContainerFieldValue = value;
		} else {
			containerNameField.setText(value);
		}
    }

    @Override
	protected final boolean validateDestinationGroup() {
        if (getContainerFullPath() == null) {
			return false;
		}

        // If the container exist, validate it
        IContainer container = getSpecifiedContainer();
        if (container != null) {
            if (!container.isAccessible()) {
                setErrorMessage(IDEWorkbenchMessages.WizardImportPage_folderMustExist);
                return false;
            }
        }

        return true;

    }
}
