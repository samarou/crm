/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role ADMIN', function () {
	var loginService = require('../../login.service');
	var searchService = require('../../search.service');
	var credentials = require('../../credentials');
	var rolesPO = require('../../pages/roles.po');
	var rolesFormPO = require('../../pages/role.form.po');
	var navbarPO = require('../../pages/navbar.po');

	var ROLE_NAME = 'RoleName';
	var ROLE_DESCRIPTION = 'RoleDescription';

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
		searchService.search(rolesPO, ROLE_NAME, by.binding('role.name')).then(checkRole);
	});

	it('should be able to delete role from page', function () {
		navbarPO.rolesLink.click();
		deleteRole();
		searchService.search(rolesPO, ROLE_NAME, by.binding('role.name')).then(function (role) {
			expect(role).toBeNull();
		});
	});


	afterAll(function () {
		loginService.logout();
	});

	function checkRole(roles) {
		roles.element(by.binding('role.name')).click();
		expect(rolesFormPO.getName().getAttribute('value')).toBe(ROLE_NAME);
		expect(rolesFormPO.getDescription().getAttribute('value')).toBe(ROLE_DESCRIPTION);
		rolesFormPO.submitButton.click();
	}

	function deleteRole() {
		searchService.search(rolesPO, ROLE_NAME, by.binding('role.name')).then(function (role) {
			role.element(by.model('role.checked')).click();
			rolesPO.deleteButton.click();
			rolesPO.getConfirmButton().click();
		});
	}
});
