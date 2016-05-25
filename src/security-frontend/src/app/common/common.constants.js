(function () {
    'use strict';

    angular
        .module('crm.common')
        .constant('permissions', {
            read: 'read',
            write: 'write',
            create: 'create',
            delete: 'delete',
            admin: 'admin'
        });

})();
