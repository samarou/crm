/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Admin security', function () {
  var auth = require('../../login.service.js');
  var usersPage = require('../../pages/users.po.js');
  var groupsPage = require('../../pages/groups.po.js');
  var rolesPage = require('../../pages/roles.po.js');
  var credentials = require('../../credentials');
  var navbarPO = require('../../pages/navbar.po');

  beforeAll(function () {
    auth.login(credentials.admin);
  });

  it('should have permission to access users page', function () {
    expect(usersPage.searchTab.isPresent()).toBe(true);
    expect(usersPage.groupSelector.isPresent()).toBe(true);
    expect(usersPage.roleSelector.isPresent()).toBe(true);
    expect(usersPage.activeUserFlag.isPresent()).toBe(true);
    expect(usersPage.table.isPresent()).toBe(true);
    expect(usersPage.userRows.isPresent()).toBe(true);
    expect(usersPage.tableName()).toBe('Users');
  });

  it('should have permission to access groups page', function () {
    navbarPO.groupsLink.click();
    expect(groupsPage.searchTab.isPresent()).toBe(true);
    expect(groupsPage.groupRows.isPresent()).toBe(true);
    expect(groupsPage.table.isPresent()).toBe(true);
    expect(groupsPage.tableName()).toBe('Groups');
  });

  it('should have permission to access roles page', function () {
    navbarPO.rolesLink.click();
    expect(rolesPage.searchTab.isPresent()).toBe(true);
    expect(rolesPage.rolesOnPage().first().isPresent()).toBe(true);
    expect(rolesPage.table.isPresent()).toBe(true);
    expect(rolesPage.tableName()).toBe('Roles');
  });


  afterAll(function () {
    auth.logout();
  })
});
