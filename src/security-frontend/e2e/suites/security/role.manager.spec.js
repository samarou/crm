/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Manager security', function () {

  var auth = require('../../authentication.js');

  var contactsPage = require('../../pages/contacts.po.js');
  beforeAll(function () {
    auth.login('manager', 'manager');
  });

  it('should have permission to access contacts page', function () {
    browser.get('/#/contacts').then(function () {
      expect(contactsPage.searchTab.isPresent()).toBe(true);
      expect(contactsPage.contactRows.isPresent()).toBe(true);
      expect(contactsPage.table.isPresent()).toBe(true);
      expect(contactsPage.tableName()).toBe('Contacts');
    });
  });

  afterAll(function () {
    auth.logout();
  });

});
