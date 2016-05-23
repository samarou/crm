(function () {
    'use strict';

    angular
        .module('crm.role')
        .controller('RolesAddController', RolesAddController);

    /** @ngInject */
    function RolesAddController(roleDetailsService, rolePrivilegeService) {
        var vm = this;

        vm.role = {};
        vm.submitText = 'Add';
        vm.title = 'Add role';
        vm.objectTypes = [];
        vm.submit = submit;
        vm.cancel = roleDetailsService.cancel;

        init();

        function init() {
            rolePrivilegeService.getObjectTypes(vm);
        }

        function submit() {
            roleDetailsService.submit(vm, true);
        }
    }
})();
