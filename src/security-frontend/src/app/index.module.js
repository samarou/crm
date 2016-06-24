(function () {
    'use strict';

    angular
        .module('crm', [
            'crm.core',
            'crm.user',
            'crm.role',
            'crm.group',
            'crm.contact',
            'crm.company',
            'crm.task',
            'crm.navbar',
            'crm.footer']);

    angular
        .module('crm.core', [
            'ngResource',
            'ui.router',
            'ui.bootstrap',
            'blockUI'
        ]);
})();
