/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.commands;


/**
 * An instance of this class describes changes to an instance of
 * <code>Command</code>.
 * <p>
 * This class is not intended to be extended by clients.
 * </p>
 * <p>
 * <em>EXPERIMENTAL</em>. The commands architecture is currently under
 * development for Eclipse 3.1. This class -- its existence, its name and its
 * methods -- are in flux. Do not use this class yet.
 * </p>
 * 
 * @since 3.1
 * @see ICommandListener#commandChanged(CommandEvent)
 */
public class CommandEvent {

	/**
	 * Whether the category of the command has changed.
	 */
	private final boolean categoryChanged;

	/**
	 * The command that has changed; this value is never <code>null</code>.
	 */
	private final Command command;

	/**
	 * Whether the defined state of the command has changed.
	 */
	private final boolean definedChanged;

	/**
	 * Whether the description of the command has changed.
	 */
	private final boolean descriptionChanged;

	/**
	 * Whether the command has either gained or lost a handler.
	 */
	private final boolean handledChanged;

	/**
	 * Whether the name of the command has changed.
	 */
	private final boolean nameChanged;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param command
	 *            the instance of the interface that changed.
	 * @param categoryChanged
	 *            true, iff the category property changed.
	 * @param definedChanged
	 *            true, iff the defined property changed.
	 * @param descriptionChanged
	 *            true, iff the description property changed.
	 * @param handledChanged
	 *            true, iff the handled property changed.
	 * @param nameChanged
	 *            true, iff the name property changed.
	 */
	public CommandEvent(final Command command, final boolean categoryChanged,
			final boolean definedChanged, final boolean descriptionChanged,
			final boolean handledChanged, final boolean nameChanged) {
		if (command == null)
			throw new NullPointerException();

		this.command = command;
		this.categoryChanged = categoryChanged;
		this.definedChanged = definedChanged;
		this.descriptionChanged = descriptionChanged;
		this.handledChanged = handledChanged;
		this.nameChanged = nameChanged;
	}

	/**
	 * Returns the instance of the interface that changed.
	 * 
	 * @return the instance of the interface that changed. Guaranteed not to be
	 *         <code>null</code>.
	 */
	public final Command getCommand() {
		return command;
	}

	/**
	 * Returns whether or not the category property changed.
	 * 
	 * @return true, iff the category property changed.
	 */
	public final boolean hasCategoryChanged() {
		return categoryChanged;
	}

	/**
	 * Returns whether or not the defined property changed.
	 * 
	 * @return true, iff the defined property changed.
	 */
	public final boolean hasDefinedChanged() {
		return definedChanged;
	}

	/**
	 * Returns whether or not the description property changed.
	 * 
	 * @return true, iff the description property changed.
	 */
	public final boolean hasDescriptionChanged() {
		return descriptionChanged;
	}

	/**
	 * Returns whether or not the handled property changed.
	 * 
	 * @return true, iff the handled property changed.
	 */
	public final boolean hasHandledChanged() {
		return handledChanged;
	}

	/**
	 * Returns whether or not the name property changed.
	 * 
	 * @return true, iff the name property changed.
	 */
	public final boolean hasNameChanged() {
		return nameChanged;
	}
}
