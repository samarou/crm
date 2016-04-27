/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role Manager', function () {
	var loginService = require('../../login.service');
	var searchService = require('../../search.service');
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
		searchService.search(contactsPO, CONTACT_EMAIL, by.binding('contact.email')).then(checkContact);
	});


	it('should be able to delete contact from page', function () {
		navbarPO.contactsLink.click();
		deleteContact();
		searchService.search(contactsPO, CONTACT_EMAIL, by.binding('contact.email')).then(function (contact) {
			expect(contact).toBeNull();
		});
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


	function deleteContact() {
		navbarPO.contactsLink.click();
		searchService.search(contactsPO, CONTACT_EMAIL, by.binding('contact.email')).then(function (contact) {
			contact.element(by.model('contact.checked')).click();
			contactsPO.deleteButton.click();
			contactsPO.getConfirmButton().click();
		});
	}


});
