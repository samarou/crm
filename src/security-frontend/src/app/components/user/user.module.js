(function () {
    'use strict';

    angular
        .module('crm.user', ['crm.acl', 'crm.common',
            'crm.role',
            'crm.group',
            'crm.validation',
            'crm.formWrapper',
            'crm.search']);
})();
