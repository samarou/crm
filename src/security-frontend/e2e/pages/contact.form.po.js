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
	self.getAccessCheckBox = function () {
		return element.all(by.model('p.canRead'));
	};
	self.accessTab = element(by.linkText('Access'));
	self.submitButton = element(by.binding('$ctrl.submitText'));
	self.cancelButton = element(by.binding('$ctrl.cancelText'));
};

module.exports = new ContactFormPage();
