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

package com.liferay.ide.ui.module.tests;

import com.liferay.ide.ui.liferay.SwtbotBase;
import com.liferay.ide.ui.liferay.support.project.ProjectSupport;
import com.liferay.ide.ui.liferay.util.ValidationMsg;
import com.liferay.ide.ui.swtbot.util.StringPool;

import java.io.File;

import org.eclipse.core.runtime.Platform;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Ashley Yuan
 * @author Lily Li
 */
public class ValidationComponentTests extends SwtbotBase {

	@Test
	public void checkBrowsePackageNameDialog() {
		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(project.getName());

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.openNewLiferayComponentClassWizard();

		wizardAction.newLiferayComponent.openSelectPackageNameDialog();

		dialogAction.prepareText("*packagename");

		validationAction.assertEnabledFalse(dialogAction.getConfirmBtn());

		dialogAction.prepareText(project.getName());

		dialogAction.selectTableItem(project.getName());

		dialogAction.selectTableItem(project.getName() + ".constants");

		dialogAction.selectTableItem(project.getName() + ".portlet");

		validationAction.assertEnabledTrue(dialogAction.getConfirmBtn());

		dialogAction.cancel();

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Test
	public void checkComponentClassName() {
		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(project.getName());

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.openNewLiferayComponentClassWizard();

		for (ValidationMsg msg :
				envAction.getValidationMsgs(
					new File(envAction.getValidationDir(), "new-component-wizard-class-name.csv"))) {

			String env = msg.getOs();

			if (!env.equals(Platform.getOS())) {
				continue;
			}

			wizardAction.newLiferayComponent.prepareComponentClass(msg.getInput());

			validationAction.assertEquals(msg.getExpect(), wizardAction.getValidationMsg(2));
		}

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Test
	public void checkComponentClassTemplate() {
		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(project.getName());

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		String[] templates = {
			AUTH_FAILURES, AUTH_MAX_FAILURE, AUTHENTICATOR, FRIENDLY_URL_MAPPER, GOGO_COMMAND, INDEXER_POST_PROCESSOR,
			LOGIN_PRE_ACTION, MVC_PORTLET_UPCASE, MODEL_LISTENER, POLLER_PROCESSOR, PORTLET_UPCASE,
			PORTLET_ACTION_COMMAND, PORTLET_FILTER, REST_UPCASE, SERVICE_WRAPPER_UPCASE, STRUTS_IN_ACTION,
			STRUTS_PORTLET_ACTION
		};

		wizardAction.openNewLiferayComponentClassWizard();

		validationAction.assertEquals(templates, wizardAction.newLiferayComponent.componentClassTemplate());

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Test
	public void checkExistingComponentClass() {
		wizardAction.openNewLiferayModuleWizard();

		String projectName = project.getName();

		wizardAction.newModule.prepareGradle(projectName);

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		String packageName = projectName + ".portlet";

		String firstLetter = projectName.substring(0, 1);

		String className = firstLetter.toUpperCase() + projectName.substring(1);

		wizardAction.openNewLiferayComponentClassWizard();

		wizardAction.newLiferayComponent.preparePackage(packageName);

		validationAction.assertEquals(
			packageName + "." + className + "Portlet" + ALREADY_EXISTS, wizardAction.getValidationMsg(2));

		wizardAction.newLiferayComponent.prepareComponentClass(project.getName() + "portlet");

		// wait for IDE-4059 fixed
		// validationAction.assertEquals(packageName + "." + className + "Portlet" + ALREADY_EXISTS,

		//wizardAction.getValidationMsg(2));

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Test
	public void checkInfoInitialState() {
		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(project.getName());

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.openNewLiferayComponentClassWizard();

		validationAction.assertEquals(CREATE_A_NEW_LIEFRAY_COMPONENT_CLASS, wizardAction.getValidationMsg(2));

		validationAction.assertTextEquals(project.getName(), wizardAction.newLiferayComponent.projectName());

		validationAction.assertEnabledTrue(wizardAction.newLiferayComponent.browsePackageBtn());

		validationAction.assertEnabledTrue(wizardAction.getFinishBtn());

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Test
	public void checkInitialState() {
		wizardAction.openNewLiferayComponentClassWizard();

		validationAction.assertEquals(NO_SUITABLE_LIFERAY_MODULE_PROJECT, wizardAction.getValidationMsg(2));

		validationAction.assertTextEquals(StringPool.BLANK, wizardAction.newLiferayComponent.projectName());

		validationAction.assertTextEquals(StringPool.BLANK, wizardAction.newLiferayComponent.packageName());

		validationAction.assertTextEquals(StringPool.BLANK, wizardAction.newLiferayComponent.componentClassName());

		validationAction.assertTextEquals(PORTLET_UPCASE, wizardAction.newLiferayComponent.componentClassTemplate());

		validationAction.assertEnabledFalse(wizardAction.getFinishBtn());

		wizardAction.cancel();
	}

	@Test
	public void checkModelListenerTemplate() {
		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(project.getName());

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		String template = MODEL_LISTENER;

		wizardAction.openNewLiferayComponentClassWizard();

		wizardAction.newLiferayComponent.prepare(template);

		validationAction.assertEquals(MODEL_CLASS_MUST_BE_SPECIFIED, wizardAction.getValidationMsg(3));

		validationAction.assertEnabledFalse(wizardAction.getFinishBtn());

		wizardAction.newLiferayComponent.prepareModelClass("modelClass");

		validationAction.assertEquals(
			"\"modelClass\"" + IS_NOT_AMONG_POSSIBLE_VALUES, wizardAction.getValidationMsg(3));

		validationAction.assertEnabledFalse(wizardAction.getFinishBtn());

		wizardAction.newLiferayComponent.openSelectModelClassAndServiceDialog();

		dialogAction.prepareText("*modelClass");

		validationAction.assertEnabledFalse(dialogAction.getConfirmBtn());

		dialogAction.prepareText("*blogs");

		dialogAction.selectTableItem("com.liferay.blogs.kernel.model.BlogsEntry");

		dialogAction.selectTableItem("com.liferay.blogs.kernel.model.BlogsStatsUser");

		validationAction.assertEnabledTrue(dialogAction.getConfirmBtn());

		dialogAction.cancel();

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Test
	public void checkPackageName() {
		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(project.getName());

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		wizardAction.openNewLiferayComponentClassWizard();

		for (ValidationMsg msg :
				envAction.getValidationMsgs(
					new File(envAction.getValidationDir(), "new-component-wizard-package-name.csv"))) {

			String env = msg.getOs();

			if (!env.equals(Platform.getOS())) {
				continue;
			}

			wizardAction.newLiferayComponent.preparePackage(msg.getInput());

			validationAction.assertEquals(msg.getExpect(), wizardAction.getValidationMsg(2));
		}

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Test
	public void checkProjectName() {
		String projectName1 = "mvc-portlet-test";

		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(projectName1);

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		String projectName2 = "portlet-test";

		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(projectName2, PORTLET);

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		String[] projectNames = {projectName1, projectName2};

		String packageName = "portlet.test.portlet";

		wizardAction.openNewLiferayComponentClassWizard();

		validationAction.assertEquals(projectNames, wizardAction.newLiferayComponent.projectName());

		wizardAction.newLiferayComponent.prepare(projectName2, packageName);

		validationAction.assertEquals(
			"portlet.test.portlet.PortletTestPortlet" + ALREADY_EXISTS, wizardAction.getValidationMsg(2));

		validationAction.assertEnabledFalse(wizardAction.getFinishBtn());

		wizardAction.cancel();

		viewAction.project.closeAndDelete(projectName1);

		viewAction.project.closeAndDelete(projectName2);
	}

	@Ignore("ignore to wait target platform way")
	@Test
	public void checkServiceWrapperTemplate() {
		wizardAction.openNewLiferayModuleWizard();

		wizardAction.newModule.prepareGradle(project.getName());

		wizardAction.finish();

		jobAction.waitForNoRunningProjectBuildingJobs();

		String template = SERVICE_WRAPPER_UPCASE;

		wizardAction.openNewLiferayComponentClassWizard();

		wizardAction.newLiferayComponent.prepare(template);

		validationAction.assertEquals(SERVICE_NAME_MUST_BE_SPECIFIED, wizardAction.getValidationMsg(3));

		validationAction.assertEnabledFalse(wizardAction.getFinishBtn());

		wizardAction.newLiferayComponent.prepareServiceName("serviceName");

		validationAction.assertEquals(CREATE_A_NEW_LIEFRAY_COMPONENT_CLASS, wizardAction.getValidationMsg(3));

		validationAction.assertEnabledTrue(wizardAction.getFinishBtn());

		wizardAction.newLiferayComponent.openSelectModelClassAndServiceDialog();

		dialogAction.prepareText("*serviceName");

		validationAction.assertEnabledFalse(dialogAction.getConfirmBtn());

		dialogAction.prepareText("*microblogs");

		dialogAction.selectTableItem("com.liferay.microblogs.service.MicroblogsEntryLocalServiceWrapper");

		dialogAction.selectTableItem("com.liferay.microblogs.service.MicroblogsEntryServiceWrapper");

		validationAction.assertEnabledTrue(dialogAction.getConfirmBtn());

		dialogAction.cancel();

		wizardAction.cancel();

		viewAction.project.closeAndDelete(project.getName());
	}

	@Rule
	public ProjectSupport project = new ProjectSupport(bot);

}