/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role ADMIN', function () {
	var loginService = require('../../login.service');
	var searchService = require('../../search.service');
	var credentials = require('../../credentials');
	var groupsPO = require('../../pages/groups.po');
	var groupFormPO = require('../../pages/group.form.po');
	var navbarPO = require('../../pages/navbar.po');

	var GROUP_NAME = 'TestGroup';
	var GROUP_DESCRIPTION = 'TEST_GROUP_DESCRIPTION';

	beforeAll(function () {
		loginService.login(credentials.admin);
	});

	it('should go to groups list', function () {
		navbarPO.groupsLink.click();
	});

	it('should be able to create the group', function () {
		groupsPO.addButton.click();
		groupFormPO.getName().sendKeys(GROUP_NAME);
		groupFormPO.getDescription().sendKeys(GROUP_DESCRIPTION);
		groupFormPO.submitButton.click();
	});

	it('should be able to find group on page', function () {
		searchService.search(groupsPO, GROUP_NAME, by.binding('group.name')).then(checkGroup);
	});


	afterAll(function () {
		deleteGroup();
		loginService.logout();
	});

	function checkGroup(group) {
		group.element(by.binding('group.name')).click();
		expect(groupFormPO.getName().getAttribute('value')).toBe(GROUP_NAME);
		expect(groupFormPO.getDescription().getAttribute('value')).toBe(GROUP_DESCRIPTION);
		groupFormPO.submitButton.click();
	}


	function deleteGroup() {
		navbarPO.groupsLink.click();
		searchService.search(groupsPO, GROUP_NAME, by.binding('group.name')).then(function (group) {
			group.element(by.model('group.checked')).click();
			groupsPO.deleteButton.click();
			groupsPO.getConfirmButton().click();
		});
	}


});
