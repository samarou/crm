var LoginPage = function () {
	'use strict';
	var self = this;
	self.logoutButton = element(by.css('[ng-click="vm.logout();"]'));
  self.usersLink = element(by.linkText('Users'));
  self.groupsLink = element(by.linkText('Groups'));
  self.rolesLink = element(by.linkText('Roles'));
  self.contactsLink = element(by.linkText('Contacts'));
	self.getUserName = function () {
		return element(by.binding('vm.getUserName()')).getText();
	}
};

module.exports = new LoginPage();
