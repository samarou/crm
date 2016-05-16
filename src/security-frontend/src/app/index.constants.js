(function () {
	'use strict';

	angular
		.module('crm')
		.constant('version', '${project.version}')
        .constant('fileUpload', 104857600);
})();
