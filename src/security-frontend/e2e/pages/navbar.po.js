var LoginPage = function () {
	'use strict';
	var self = this;
	self.logoutButton = element(by.css('[ng-click="vm.logout();"]'));
	self.getUserName = function () {
		return element(by.binding('vm.getUserName()')).getText();
	}
};

module.exports = new LoginPage();
