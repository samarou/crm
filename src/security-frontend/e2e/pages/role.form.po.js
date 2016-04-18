var RolesFormPage = function () {
	'use strict';

	var self = this;
	self.getName = function () {
		return element(by.model('vm.role.name'));
	};
	self.getDescription = function () {
		return element(by.model('vm.role.description'));
	};
	self.submitButton = element(by.binding('$ctrl.submitText'));
};

module.exports = new RolesFormPage();
