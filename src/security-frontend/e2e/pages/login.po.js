var LoginPage = function () {
	'use strict';
	var self = this;
	self.userInput = element(by.id('username'));
	self.passwordInput = element(by.id('password'));
	self.loginButton = element(by.id('login_submit'));
};

module.exports = new LoginPage();
