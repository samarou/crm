/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

fdescribe('Users under role ADMIN', function () {
	var loginService = require('../../login.service');
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
		var groups = findGroupsByName(GROUP_NAME);
		groups.count().then(function (count) {
			if (count === 0) {
				fail('Group didn\'t find');
			} else {
				checkGroup(groups.first());
			}
		});
	});


	afterAll(function () {
		deleteGroupByName(GROUP_NAME);
		loginService.logout();
	});

	function checkGroup(group) {
		group.element(by.css('span[ng-click="vm.edit(group)"]')).click();
		expect(groupFormPO.getName().getAttribute('value')).toBe(GROUP_NAME);
		expect(groupFormPO.getDescription().getAttribute('value')).toBe(GROUP_DESCRIPTION);
		groupFormPO.submitButton.click();
	}

	function findGroupsByName(group) {
		groupsPO.searchTab.sendKeys(GROUP_NAME);
		return groupsPO.groupsOnPage().filter(function (row) {
			return row.element(by.binding('group.name')).getText().then(function (text) {
				return text === group;
			});
		})
	}

	function deleteGroupByName(group) {
		navbarPO.groupsLink.click();
		findGroupsByName(group).first().element(by.model('group.checked')).click();
		groupsPO.deleteButton.click();
	}


});
