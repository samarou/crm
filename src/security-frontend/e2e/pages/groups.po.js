var GroupsPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('vm.bundle.pagingFilterConfig.filterObject.$'));
  self.groupRows = element(by.repeater('group in (vm.bundle.pageGroups = (vm.bundle.groupList | pagingFilter:vm.bundle.pagingFilterConfig))'));
  self.table = element(by.css('table.table'));
  self.tableName = function () {
    return self.table.element(by.tagName('caption')).getText();
  };
  self.addButton = element(by.css('span[ng-click="vm.add()"]'));
  self.deleteButton = element(by.css('span[ng-click="vm.remove()"]'));
  self.nextPageButton = element(by.linkText('Next'));
  self.groupsOnPage = function () {
    return element.all(
            by.repeater('group in (vm.bundle.pageGroups = (vm.bundle.groupList | pagingFilter:vm.bundle.pagingFilterConfig))'));
  };
};

module.exports = new GroupsPage();
