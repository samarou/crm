var GroupsPage = function () {
	'use strict';

	var self = this;
	self.searchTab = element(by.model('vm.searchContactBundle.filter.text'));
	self.contactRows = element(by.repeater('contact in vm.searchContactBundle.itemsList'));
	self.table = element(by.css('table.table'));
	self.tableName = function () {
		return self.table.element(by.tagName('caption')).getText();
	};
	self.addButton = element(by.css('span[ng-click="vm.add()"]'));
	self.deleteButton = element(by.css('span[ng-click="vm.remove()"]'));
	self.nextPageButton = element(by.linkText('Next'));
	self.pageList = function () {
		return element.all(
				by.repeater('contact in vm.searchContactBundle.itemsList'));
	};
	self.getConfirmButton = function () {
		return element(by.css('button[ng-click="vm.yes()"]'));
	};
	self.getNotifyButton = function () {
		return element.all(by.css('button[ng-click="vm.close()"]')).first();
	}
};

module.exports = new GroupsPage();
