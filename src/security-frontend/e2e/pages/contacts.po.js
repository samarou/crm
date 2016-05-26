var GroupsPage = function () {
	'use strict';

	var self = this;
	self.searchTab = element(by.model('vm.searchContactBundle.filter.text'));
	self.contactRows = element(by.repeater('contact in vm.searchContactBundle.itemsList'));
	self.table = element(by.css('table.table'));
	self.tableName = function () {
		return element(by.tagName('h4')).getText();
	};
	self.addButton = element(by.css('a[ng-click="vm.add()"]'));
	self.deleteButton = element(by.css('a[ng-click="vm.remove()"]'));
	self.nextPageButton = element(by.css('li.pagination-next'));
	self.pageList = function () {
		return element.all(
				by.repeater('contact in vm.searchContactBundle.itemsList'));
	};
	self.getConfirmButton = function () {
		return element(by.css('button[ng-click="vm.yes()"]'));
	};
	self.getNotifyButton = function () {
		return element.all(by.css('button[ng-click="vm.close()"]')).first();
	};
};

module.exports = new GroupsPage();
