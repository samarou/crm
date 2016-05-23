(function () {
    'use strict';

    angular
        .module('crm.user')
        .controller('UsersEditController', UserEditController);

    /** @ngInject */
    function UserEditController(userService, userDetailsService, collections, $stateParams) {
        var vm = this;

        vm.user = {};
        vm.groups = [];
        vm.roles = [];
        vm.submitText = 'Save';
        vm.title = 'Edit user';
        vm.submit = submit;
        vm.cancel = userDetailsService.cancel;
        vm.aclHandler = userDetailsService.createAclHandler(function () {
            return vm.user.id;
        });

        init();

        function init() {
            userDetailsService.getGroupsAndRoles().then(initUser);
        }

        function submit() {
            vm.user.acls = vm.aclHandler.acls;
            userDetailsService.save(vm.user, vm.roles, vm.groups, false);
        }

        function initUser(groupsAndRoles) {
            vm.groups = groupsAndRoles[0].data;
            vm.roles = groupsAndRoles[1].data;
            return userService.getById($stateParams.id).then(function (response) {
                vm.user = response.data;
                checkGroupsAndRolesWhichUserHas(vm.user);
                userService.getAcls(vm.user.id).then(function (response) {
                    vm.aclHandler.acls = response.data;
                });
            });
        }

        function checkGroupsAndRolesWhichUserHas(user) {
            vm.groups.forEach(function (group) {
                group.checked = !!collections.find(group, user.groups);
            });
            vm.roles.forEach(function (role) {
                role.checked = !!collections.find(role, user.roles);
            });
        }
    }
})();
