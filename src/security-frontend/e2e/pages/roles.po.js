var RolesPage = function () {
	'use strict';

	var self = this;
	self.searchTab = element(by.model('vm.pagingFilterConfig.filterObject.$'));
	self.table = element(by.css('table.table'));
	self.addButton = element(by.css('span[ng-click="vm.add()"]'));
	self.deleteButton = element(by.css('span[ng-click="vm.remove()"]'));
	self.nextPageButton = element(by.css('li.pagination-next'));
	self.tableName = function () {
		return self.table.element(by.tagName('caption')).getText();
	};
	self.pageList = function () {
		return element.all(
				by.repeater('role in (vm.pageRoles = (vm.roleList | pagingFilter:vm.pagingFilterConfig))'));
	};
};

module.exports = new RolesPage();
