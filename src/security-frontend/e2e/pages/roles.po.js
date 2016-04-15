var GroupsPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('vm.pagingFilterConfig.filterObject.$'));
  self.roleRows = element(by.repeater('role in (vm.pageRoles = (vm.roleList | pagingFilter:vm.pagingFilterConfig))'));
  self.table = element(by.css('table.table'));
  self.tableName = function () {
    return self.table.element(by.tagName('caption')).getText();
  };
};

module.exports = new GroupsPage();
