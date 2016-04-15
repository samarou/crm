var GroupsPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('vm.searchContactBundle.filter.text'));
  self.contactRows = element(by.repeater('contact in vm.searchContactBundle.itemsList'));
  self.table = element(by.css('table.table'));
  self.tableName = function () {
    return self.table.element(by.tagName('caption')).getText();
  };
};

module.exports = new GroupsPage();
