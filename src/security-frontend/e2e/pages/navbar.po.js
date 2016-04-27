var LoginPage = function () {
	'use strict';
	var self = this;
	self.logoutButton = element(by.css('button[ng-click="vm.logout()"]'));
	self.usersLink = element(by.css('a[ui-sref="users.list"]'));
	self.groupsLink = element(by.css('a[ui-sref="groups.list"]'));
	self.rolesLink = element(by.css('a[ui-sref="roles.list"]'));
	self.contactsLink = element(by.css('a[ui-sref="contacts.list"]'));
	self.getUserName = function () {
		return element(by.binding('vm.getUserName()')).getText();
	}
};

module.exports = new LoginPage();
