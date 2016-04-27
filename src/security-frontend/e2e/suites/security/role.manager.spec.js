/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Manager security', function () {

  var loginService = require('../../login.service.js');
  var contactsPage = require('../../pages/contacts.po.js');
  var credentials = require('../../credentials');
  var navbarPO = require('../../pages/navbar.po');

  beforeAll(function () {
    loginService.login(credentials.manager);
  });

  it('should have permission to access contacts page', function () {
    navbarPO.contactsLink.click();
    expect(contactsPage.searchTab.isPresent()).toBe(true);
    expect(contactsPage.contactRows.isPresent()).toBe(true);
    expect(contactsPage.table.isPresent()).toBe(true);
    expect(contactsPage.tableName()).toBe('Contacts');
  });

  afterAll(function () {
    loginService.logout();
  });

});
