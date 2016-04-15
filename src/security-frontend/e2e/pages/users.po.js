var UsersPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('vm.bundle.filter.text'));
  self.groupSelector = element(by.model('vm.bundle.filter.groupId'));
  self.roleSelector = element(by.model('vm.bundle.filter.roleId'));
  self.activeUserFlag = element(by.model('vm.bundle.filter.active'));

  self.userRows = element(by.repeater('user in vm.bundle.itemsList'));

  self.table = element(by.css('table.table'));
  self.tableName = function () {
    return self.table.element(by.tagName('caption')).getText();
  };
  self.getUsernamesInTable = function () {
    return element.all(by.binding('user.userName'))
      .map(function (el) {
        return el.getText();
      });
  };
  self.isLastPage = function () {
    return element(by.css('.pagination-next.disabled')).isPresent();
  };
  self.nextPageButton = function () {
    return element(by.css('a[ng-click="selectPage(page + 1, $event)"]'));
  };
  self.getOption = function (name) {
    return element(by.cssContainingText('option', name));
  };
};

module.exports = new UsersPage();
