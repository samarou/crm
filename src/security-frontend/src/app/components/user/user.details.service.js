(function () {
    'use strict';

    angular
        .module('crm.user')
        .factory('userDetailsService', userDetailsService);

    /** @ngInject */
    function userDetailsService(userService, groupService, roleService, aclServiceBuilder, $state, $q, $log) {
        return {
            save: save,
            cancel: goToList,
            getGroupsAndRoles: getGroupsAndRoles,
            createAclHandler: createAclHandler
        };

        function createAclHandler(getId) {
            return {
                canEdit: true,
                acls: [],
                actions: aclServiceBuilder(getId, userService)
            };
        }

        function save(user, roles, groups, isNew) {
            checkGroups(user, groups);
            checkRoles(user, roles);
            if (isNew) {
                userService.create(user).then(goToList);
            } else {
                userService.update(user).then(goToList);
            }
        }

        function goToList() {
            $state.go('users.list');
        }

        function getGroupsAndRoles() {
            return $q.all([getGroups(), getRoles()]);
        }

        function getGroups() {
            return groupService.getAll();
        }

        function getRoles() {
            return roleService.fetchAll();
        }

        function checkGroups(user, groups) {
            user.groups = groups.filter(function (group) {
                return group.checked;
            });
        }

        function checkRoles(user, roles) {
            user.roles = roles.filter(function (role) {
                return role.checked;
            });
        }
    }
})();
