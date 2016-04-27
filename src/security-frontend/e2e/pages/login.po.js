var LoginPage = function () {
	'use strict';
	var self = this;
	self.userInput = element(by.id('username'));
	self.passwordInput = element(by.id('password'));
	self.form = element(by.css('form[name="form"]'));
};

module.exports = new LoginPage();
