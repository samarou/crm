var GroupsPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('vm.bundle.pagingFilterConfig.filterObject.$'));
  self.groupRows = element(by.repeater('group in (vm.bundle.pageGroups = (vm.bundle.groupList | pagingFilter:vm.bundle.pagingFilterConfig))'));
  self.table = element(by.css('table.table'));
  self.tableName = function () {
    return element(by.tagName('h4')).getText();
  };
  self.addButton = element(by.css('a[ng-click="vm.add()"]'));
  self.deleteButton = element(by.css('a[ng-click="vm.remove()"]'));
  self.nextPageButton = element(by.css('li.pagination-next'));
  self.pageList = function () {
    return element.all(
            by.repeater('group in (vm.bundle.pageGroups = (vm.bundle.groupList | pagingFilter:vm.bundle.pagingFilterConfig))'));
  };
  self.getConfirmButton = function () {
    return element(by.css('button[ng-click="vm.yes()"]'));
  };
};

module.exports = new GroupsPage();
