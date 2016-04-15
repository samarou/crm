/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Admin security', function () {
  var auth = require('../../authentication.js');
  var usersPage = require('../../pages/users.po.js');
  var groupsPage = require('../../pages/groups.po.js');
  var rolesPage = require('../../pages/roles.po.js');

  beforeAll(function () {
    auth.login('admin', 'admin');
  });

  it('should have permission to access users page', function () {
    browser.get('/#/users');
    waitForSearchTab()
      .then(function () {
        expect(usersPage.searchTab.isPresent()).toBe(true);
        expect(usersPage.groupSelector.isPresent()).toBe(true);
        expect(usersPage.roleSelector.isPresent()).toBe(true);
        expect(usersPage.activeUserFlag.isPresent()).toBe(true);
        expect(usersPage.table.isPresent()).toBe(true);
        expect(usersPage.userRows.isPresent()).toBe(true);
        expect(usersPage.tableName()).toBe('Users');
      });
  });

  it('should have permission to access groups page', function () {
    browser.get('/#/groups');
    waitForSearchTab()
      .then(function () {
        expect(groupsPage.searchTab.isPresent()).toBe(true);
        expect(groupsPage.groupRows.isPresent()).toBe(true);
        expect(groupsPage.table.isPresent()).toBe(true);
        expect(groupsPage.tableName()).toBe('Groups');
      });
  });

  it('should have permission to access roles page', function () {
    browser.get('/#/roles');
    waitForSearchTab()
      .then(function () {
        expect(rolesPage.searchTab.isPresent()).toBe(true);
        expect(rolesPage.roleRows.isPresent()).toBe(true);
        expect(rolesPage.table.isPresent()).toBe(true);
        expect(rolesPage.tableName()).toBe('Roles');
      });
  });

  function waitForSearchTab() {
    return browser.wait(function () {
      return contactsPage.searchTab.isPresent();
    }, 5000)
  }

  afterAll(function () {
    auth.logout();
  })
});
