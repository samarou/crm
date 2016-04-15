var GroupsPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('vm.bundle.pagingFilterConfig.filterObject.$'));
  self.groupRows = element(by.repeater('group in (vm.bundle.pageGroups = (vm.bundle.groupList | pagingFilter:vm.bundle.pagingFilterConfig))'));
  self.table = element(by.css('table.table'));
  self.tableName = function () {
    return self.table.element(by.tagName('caption')).getText();
  };
};

module.exports = new GroupsPage();
