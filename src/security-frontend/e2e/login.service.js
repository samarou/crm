var LoginService = function () {
	'use strict';

	var loginPage = require('./pages/login.po.js');
	var navbarPage = require('./pages/navbar.po.js');

	var self = this;
	self.login = function (credentials) {
		loginPage.userInput.sendKeys(credentials.username);
		loginPage.passwordInput.sendKeys(credentials.password);
		loginPage.form.submit();
	};

	self.logout = function () {
		navbarPage.logoutButton.click();
	}
};

module.exports = new LoginService();

