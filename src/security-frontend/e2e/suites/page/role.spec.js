/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

fdescribe('Users under role ADMIN', function () {
	var loginService = require('../../login.service');
	var credentials = require('../../credentials');
	var rolesPO = require('../../pages/roles.po');
	var rolesFormPO = require('../../pages/role.form.po');
	var navbarPO = require('../../pages/navbar.po');

	var ROLE_NAME = 'TestRole';
	var ROLE_DESCRIPTION = 'TEST_DESCRIPTION';

	beforeAll(function () {
		loginService.login(credentials.admin);
	});

	it('should go to roles list', function () {
		navbarPO.rolesLink.click();
	});

	it('should be able to create the role', function () {
		rolesPO.addButton.click();
		rolesFormPO.getName().sendKeys(ROLE_NAME);
		rolesFormPO.getDescription().sendKeys(ROLE_DESCRIPTION);
		rolesFormPO.submitButton.click();
	});

	it('should be able to find role on page', function () {
		var roles = findRolesByName(ROLE_NAME);
		roles.count().then(function (count) {
			if (count === 0) {
				fail('Role didn\'t find');
			} else {
				checkRole(roles.first());
			}
		});
	});


	afterAll(function () {
		deleteRoleByRoleName(ROLE_NAME);
		loginService.logout();
	});

	function checkRole(role) {
		role.element(by.css('span[ng-click="vm.edit(role)"]')).click();
		expect(rolesFormPO.getName().getAttribute('value')).toBe(ROLE_NAME);
		expect(rolesFormPO.getDescription().getAttribute('value')).toBe(ROLE_DESCRIPTION);
		rolesFormPO.submitButton.click();
	}

	function findRolesByName(role) {
		rolesPO.searchTab.sendKeys(ROLE_NAME);
		return rolesPO.rolesOnPage().filter(function (row) {
			return row.element(by.binding('role.name')).getText().then(function (text) {
				return text === role;
			});
		})
	}

	function deleteRoleByRoleName(role) {
		navbarPO.rolesLink.click();
		findRolesByName(role).first().element(by.model('role.checked')).click();
		rolesPO.deleteButton.click();
	}


});
