(function () {
    'use strict';

    angular
        .module('crm.user')
        .controller('UsersAddController', UserAddController);

    /** @ngInject */
    function UserAddController(userDetailsService) {
        var vm = this;

        vm.user = {active: true};
        vm.groups = [];
        vm.roles = [];
        vm.smgProfile = '';
        vm.submitText = 'Add';
        vm.title = 'Add user';
        vm.submit = submit;
        vm.cancel = userDetailsService.cancel;
        vm.aclHandler = userDetailsService.createAclHandler(function () {
            return vm.user.id;
        });
        vm.loadProfile = userDetailsService.loadProfile;

        init();

        function init() {
            userDetailsService.getGroupsAndRoles().then(function (tuple) {
                vm.groups = tuple[0].data;
                vm.roles = tuple[1].data;
            });
        }

        function submit() {
            vm.user.acls = vm.aclHandler.acls;
            userDetailsService.save(vm.user, vm.roles, vm.groups, true);
        }
    }
})();
