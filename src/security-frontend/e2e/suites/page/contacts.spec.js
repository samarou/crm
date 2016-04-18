/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role Manager', function () {
	var loginService = require('../../login.service');
	var credentials = require('../../credentials');
	var contactsPO = require('../../pages/contacts.po');
	var contactFormPO = require('../../pages/contact.form.po');
	var navbarPO = require('../../pages/navbar.po');

	var CONTACT_FIRST_NAME = 'TestName';
	var CONTACT_SECOND_NAME = 'TestSecondName';
	var CONTACT_EMAIL = 'TestEmail';
	var CONTACT_ADDRESS = 'TestAddress';

	beforeAll(function () {
		loginService.login(credentials.manager);
	});

	it('should go to contacts list', function () {
		navbarPO.contactsLink.click();
	});

	it('should be able to create the contact', function () {
		contactsPO.addButton.click();
		contactFormPO.getFirstName().sendKeys(CONTACT_FIRST_NAME);
		contactFormPO.getLastName().sendKeys(CONTACT_SECOND_NAME);
		contactFormPO.getEmail().sendKeys(CONTACT_EMAIL);
		contactFormPO.getAddress().sendKeys(CONTACT_ADDRESS);
		contactFormPO.submitButton.click();
	});

	it('should be able to find contact on page', function () {
		var contacts = findContactsByName(CONTACT_FIRST_NAME);
		contacts.count().then(function (count) {
			if (count === 0) {
				fail('Contact didn\'t find');
			} else {
				checkContact(contacts.first());
			}
		});
	});


	it('should be able to delete contact from page', function () {
		deleteContactByName(CONTACT_FIRST_NAME);
		var contacts = findContactsByName(CONTACT_FIRST_NAME);
		expect(contacts.count()).toBe(0);
	});


	afterAll(function () {
		loginService.logout();
	});

	function checkContact(contact) {
		contact.element(by.css('span[ng-click="vm.edit(contact)"]')).click();
		expect(contactFormPO.getFirstName().getAttribute('value')).toBe(CONTACT_FIRST_NAME);
		expect(contactFormPO.getLastName().getAttribute('value')).toBe(CONTACT_SECOND_NAME);
		expect(contactFormPO.getEmail().getAttribute('value')).toBe(CONTACT_EMAIL);
		expect(contactFormPO.getAddress().getAttribute('value')).toBe(CONTACT_ADDRESS);
		contactFormPO.submitButton.click();
	}

	function findContactsByName(contact) {
		contactsPO.searchTab.sendKeys(CONTACT_FIRST_NAME);
		return contactsPO.contactsOnPage().filter(function (row) {
			return row.element(by.binding('contact.firstName')).getText().then(function (text) {
				return text === contact;
			});
		})
	}

	function deleteContactByName(contact) {
		navbarPO.contactsLink.click();
		findContactsByName(contact).first().element(by.model('contact.checked')).click();
		contactsPO.deleteButton.click();
		contactsPO.getConfirmButton().click();
	}


});
