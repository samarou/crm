var RolesPage = function () {
	'use strict';

	var self = this;
	self.searchTab = element(by.model('vm.pagingFilterConfig.filterObject.$'));
	self.table = element(by.css('table.table'));
	self.addButton = element(by.css('a[ng-click="vm.add()"]'));
	self.deleteButton = element(by.css('a[ng-click="vm.remove()"]'));
	self.nextPageButton = element(by.css('li.pagination-next'));
	self.tableName = function () {
		return element(by.tagName('h4')).getText();
	};
	self.pageList = function () {
		return element.all(
				by.repeater('role in (vm.pageRoles = (vm.roleList | pagingFilter:vm.pagingFilterConfig))'));
	};
	self.getConfirmButton = function () {
		return element(by.css('button[ng-click="vm.yes()"]'));
	};
};

module.exports = new RolesPage();
