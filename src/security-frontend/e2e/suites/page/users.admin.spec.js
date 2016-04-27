/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role ADMIN', function () {
  var loginService = require('../../login.service.js');
  var usersPO = require('../../pages/users.po.js');
  var credentials = require('../../credentials');
  var usersFormPO = require('../../pages/users.form.po.js');
  var contactsPage = require('../../pages/contacts.po.js');
  var navbarPO = require('../../pages/navbar.po');
  var searchService = require('../../search.service');

  var ROLE_BLOCKING_USER = 'MANAGER';
  var ROLE_FILTERING_USER = 'ADMIN';
  var ROLE_CHANGING_ROLE_USER = 'MANAGER';
  var ADMIN = credentials.admin;
  var BLOCKING_USER = credentials.manager;
  var FILTERING_USER = credentials.admin;
  var CHANGING_ROLE_USER = credentials.admin;

  beforeEach(function () {
    loginService.login(ADMIN)
  });

  it('should be able to filter user records by roles', function () {
    usersPO.getOption(ROLE_FILTERING_USER).click();
    searchService.search(usersPO, FILTERING_USER.username, by.binding('user.userName')).then(expectToBeTruthy);
  });

  it('should be able to block the user and check if it`s blocked', function () {
    var userToBlock = BLOCKING_USER.username;
    usersPO.getOption(ROLE_BLOCKING_USER).click();
    searchService.search(usersPO, userToBlock, by.binding('user.userName')).then(expectToBeTruthy);
    activateUser(false, userToBlock);
    expect(checkUserOnPage(userToBlock)).toBe(false);
    usersPO.activeUserFlag.click();
    expect(checkUserOnPage(userToBlock)).toBe(true);
    activateUser(true, userToBlock);
    usersPO.activeUserFlag.click();
    expect(checkUserOnPage(userToBlock)).toBe(true);
  });

  it('should be able to add Manager role to admin and access contacts page', function () {
    var userChangingRole = CHANGING_ROLE_USER.username;
    searchService.search(usersPO, userChangingRole, by.binding('user.userName')).then(expectToBeTruthy);
    usersPO.editUserClick(userChangingRole);
    usersFormPO.checkRole(ROLE_CHANGING_ROLE_USER);
    usersFormPO.submitButton.click();
    loginService.logout();

    loginService.login(CHANGING_ROLE_USER);
    navbarPO.contactsLink.click();
    expect(contactsPage.searchTab.isPresent()).toBe(true);
    loginService.logout();

    loginService.login(ADMIN);
    searchService.search(usersPO, userChangingRole, by.binding('user.userName')).then(expectToBeTruthy);
    usersPO.editUserClick(userChangingRole);
    usersFormPO.uncheckRole(ROLE_CHANGING_ROLE_USER);
    usersFormPO.submitButton.click();
  });

  afterEach(function () {
    loginService.logout();
  });

  function expectToBeTruthy(found) {
    expect(found).toBeTruthy();
  }

  function activateUser(isMakeActive, userName) {
    usersPO.clickOnCheckboxOfUser(userName);
    if (isMakeActive) {
      usersPO.userActivation.click();
    } else {
      usersPO.userDeactivation.click();
    }
  }

  function checkUserOnPage(userName) {
    return usersPO.getUserNamesInTable().then(function (users) {
      return checkIfUserInArray(users, userName);
    })
  }

  function checkIfUserInArray(users, name) {
    return (users.indexOf(name)) != -1;
  }
});
