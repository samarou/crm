var UsersPage = function () {
  'use strict';

  var self = this;
  self.searchTab = element(by.model('$ctrl.scope.bundle.filter.text'));
  self.groupSelector = element(by.model('$ctrl.scope.bundle.filter.groupId'));
  self.roleSelector = element(by.model('$ctrl.scope.bundle.filter.roleId'));
  self.activeUserFlag = element(by.model('$ctrl.scope.bundle.filter.active'));
  self.pageList = function () {
    return element.all(
      by.repeater('user in vm.bundle.itemsList'));
  };
  self.table = element(by.css('table.table'));
  self.tableRows = element.all(by.css('tr[ng-repeat="user in vm.bundle.itemsList"]'));
  self.userDeactivation = element(by.css('a[ng-click="$ctrl.scope.activate(false)"]'));
  self.userActivation = element(by.css('a[ng-click="$ctrl.scope.activate(true)"]'));
  self.nextPageButton = element(by.css('li.pagination-next'));// element(by.linkText('Next'));
  self.searchOptionsButton = element(by.css('button[data-target="#filter-panel"]'));

  self.tableName = function () {
    return element(by.tagName('h4')).getText();
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
      .element(by.binding('user.userName'))
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
  self.getOption = function (name) {
    return element(by.cssContainingText('option', name));
  };
};

module.exports = new UsersPage();
