var ContactFormPage = function () {
	'use strict';

	var self = this;
	self.getFirstName = function () {
		return element(by.model('vm.contact.firstName'));
	};
	self.getLastName = function () {
		return element(by.model('vm.contact.lastName'));
	};
	self.getEmail = function () {
		return element(by.model('vm.contact.email'));
	};
	self.getAddress = function () {
		return element(by.model('vm.contact.address'));
	};
	self.submitButton = element(by.binding('$ctrl.submitText'));
};

module.exports = new ContactFormPage();
