var GroupsFormPage = function () {
	'use strict';

	var self = this;
	self.getName = function () {
		return element(by.model('vm.group.name'));
	};
	self.getDescription = function () {
		return element(by.model('vm.group.description'));
	};
	self.submitButton = element(by.binding('$ctrl.submitText'));
};

module.exports = new GroupsFormPage();
