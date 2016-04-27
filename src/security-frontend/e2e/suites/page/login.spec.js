/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('User', function () {
	var navbarPO = require('../../pages/navbar.po');
	var loginService = require('../../login.service');
	var credentials = require('../../credentials');

	it('should be able to login under role ADMIN', function () {
		loginService.login(credentials.admin);
		expect(navbarPO.getUserName()).toBe(credentials.admin.username);
	});

	it('should be able to login under role MANAGER', function () {
		loginService.login(credentials.manager);
		expect(navbarPO.getUserName()).toBe(credentials.manager.username);
	});

	it('should be able to login under role SPECIALIST', function () {
		loginService.login(credentials.specialist);
		expect(navbarPO.getUserName()).toBe(credentials.specialist.username);
	});

	afterEach(function () {
		loginService.logout();
	});

});
