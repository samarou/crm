/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Specialist security', function () {

	var loginService = require('../../login.service.js');
	var contactsPage = require('../../pages/contacts.po.js');
	var credentials = require('../../credentials');
	var contactFormPO = require('../../pages/contact.form.po');
	var navbarPO = require('../../pages/navbar.po');

	beforeAll(function () {
		loginService.login(credentials.specialist);
	});

	it('should have permission to access contacts page', function () {
		navbarPO.contactsLink.click();
		expect(contactsPage.searchTab.isPresent()).toBeTruthy();
		expect(contactsPage.contactRows.isPresent()).toBeTruthy();
		expect(contactsPage.table.isPresent()).toBeTruthy();
		expect(contactsPage.tableName()).toBe('Contacts');
	});

	it('should not have permission to add contacts', function () {
		navbarPO.contactsLink.click();
		expect(contactsPage.addButton.isDisplayed()).toBeFalsy();
	});

	it('should not have permission to edit contacts, only read', function () {
		navbarPO.contactsLink.click();
		contactsPage.pageList().first().element(by.binding('contact.email')).click();
		expect(contactFormPO.getFirstName().isEnabled()).toBeFalsy();
		expect(contactFormPO.getLastName().isEnabled()).toBeFalsy();
		expect(contactFormPO.getEmail().isEnabled()).toBeFalsy();
		expect(contactFormPO.getAddress().isEnabled()).toBeFalsy();
		contactFormPO.cancelButton.click();
	});

	it('should not have permission to edit contacts, only read', function () {
		navbarPO.contactsLink.click();
		contactsPage.pageList().first().element(by.binding('contact.email')).click();
		expect(contactFormPO.getFirstName().isEnabled()).toBeFalsy();
		expect(contactFormPO.getLastName().isEnabled()).toBeFalsy();
		expect(contactFormPO.getEmail().isEnabled()).toBeFalsy();
		expect(contactFormPO.getAddress().isEnabled()).toBeFalsy();
		contactFormPO.accessTab.click();
		contactFormPO.getAccessCheckBox().each(function (checkBox) {
			expect(checkBox.isEnabled()).toBeFalsy();
		});
		contactFormPO.cancelButton.click();
	});

	it('should not have permission to delete contacts', function () {
		navbarPO.contactsLink.click();
		contactsPage.pageList().first().element(by.model('contact.checked')).click();
		contactsPage.deleteButton.click();
		contactsPage.getConfirmButton().click();
		contactsPage.getNotifyButton().click();
	});

	afterAll(function () {
		loginService.logout();
	});

});
