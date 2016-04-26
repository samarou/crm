(function () {
	'use strict';

	angular
			.module('securityManagement')
			.constant('permissions', {
				read: 'read',
				write: 'write',
				create: 'create',
				delete: 'delete',
				admin: 'admin'
			});

})();
