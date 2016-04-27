var Credentials = function () {
	'use strict';

	var self = this;
	self.manager = acc('manager', 'manager');
	self.admin = acc('admin', 'admin');
	self.specialist = acc('specialist', 'specialist');

	function acc(username, password) {
		return {username: username, password: password}
	}
};

module.exports = new Credentials();
