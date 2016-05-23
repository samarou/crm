(function () {
    'use strict';

    angular
        .module('crm')
        .constant('version', '${project.version}')
        .constant('MAX_FILE_SIZE', 104857600);
})();
