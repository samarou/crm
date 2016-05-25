(function () {
    'use strict';

    angular
        .module('crm.contact')
        .constant('permissions', {
            read: 'read',
            write: 'write',
            create: 'create',
            delete: 'delete',
            admin: 'admin'
        });

})();
