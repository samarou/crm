var UsersPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('vm.bundle.filter.text'));
  self.groupSelector = element(by.model('vm.bundle.filter.groupId'));
  self.roleSelector = element(by.model('vm.bundle.filter.roleId'));
  self.activeUserFlag = element(by.model('vm.bundle.filter.active'));
  self.userRows = element(by.repeater('user in vm.bundle.itemsList'));
  self.table = element(by.css('table.table'));
  self.tableRows = element.all(by.css('tr[ng-repeat="user in vm.bundle.itemsList"]'));
  self.userDeactivation = element(by.css('span[ng-click="vm.activate(false)"]'));
  self.userActivation = element(by.css('span[ng-click="vm.activate(true)"]'));

  self.tableName = function () {
    return self.table.element(by.tagName('caption')).getText();
  };

  self.getUserNamesInTable = function () {
    return element.all(by.binding('user.userName'))
      .map(function (el) {
        return el.getText();
      });
  };

  self.clickOnCheckboxOfUser = function (userName) {
    return self.getUserRow(userName)
      .element(by.model('user.checked'))
      .click();
  };

  self.editUserClick = function (userName) {
    return self.getUserRow(userName)
      .element(by.css('span[ng-click="vm.edit(user)"]'))
      .click();
  };

  self.getUserRow = function (userName) {
    return self.tableRows.filter(function (elem) {
        return elem.element(by.binding('user.userName'))
          .getText()
          .then(function (text) {
            return text === userName;
          });
      })
      .first();
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
