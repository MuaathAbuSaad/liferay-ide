/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.ide.kaleo.ui.action;

import com.liferay.ide.kaleo.core.model.Action;
import com.liferay.ide.kaleo.core.model.Scriptable;
import com.liferay.ide.kaleo.ui.IKaleoEditorHelper;
import com.liferay.ide.kaleo.ui.KaleoUI;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ImageData;
import org.eclipse.sapphire.ui.Presentation;

/**
 * @author Gregory Amerson
 */
public class EditScriptActionHandler extends ListSelectionEditHandler {

	@Override
	public Object edit(Element modelElement, Presentation context) {
		Scriptable scriptable = modelElement.nearest(Scriptable.class);

		IKaleoEditorHelper kaleoEditorHelper = KaleoUI.getKaleoEditorHelper(scriptable.getScriptLanguage().text(true));

		kaleoEditorHelper.openEditor(context.part(), scriptable, Action.PROP_SCRIPT);

		return null;
	}

	@Override
	protected ImageData typeImage() {
		return Action.TYPE.image();
	}

}