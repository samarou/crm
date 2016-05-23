(function () {
    'use strict';

    angular
        .module('crm.role')
        .factory('rolePrivilegeService', rolePrivilegeService);

    /** @ngInject */
    function rolePrivilegeService(collections, privilegeService) {
        return {
            getObjectTypes: getObjectTypes,
            checkPrivilegesOfRole: checkPrivilegesOfRole,
            getPrivilegesOfRole: getPrivilegesOfRole
        };

        function getObjectTypes(scope) {
            return privilegeService.getAll().then(function (response) {
                var privileges = response.data;
                var objectTypeList = fillObjectTypeList(privileges);
                scope.objectTypes = sortObjectTypes(objectTypeList);
            });
        }

        function sortObjectTypes(objectTypeList) {
            return collections.sort(Object.keys(objectTypeList)).map(function (objectTypeName) {
                return {
                    objectTypeName: objectTypeName,
                    actions: collections.sort(objectTypeList[objectTypeName], true, collections.propertyComparator('id'))
                };
            });
        }

        function fillObjectTypeList(privileges) {
            var objectTypeList = Object.create(null);
            privileges.forEach(function (privilege) {
                if (!(privilege.objectTypeName in objectTypeList)) {
                    objectTypeList[privilege.objectTypeName] = [];
                }
                objectTypeList[privilege.objectTypeName].push({
                    id: privilege.action.id,
                    name: privilege.actionName,
                    privilege: privilege
                });
            });
            return objectTypeList;
        }

        function checkPrivilegesOfRole(scope) {
            scope.objectTypes.forEach(function (privilegeObject) {
                privilegeObject.actions.forEach(function (action) {
                    action.privilege.checked = !!collections.find(action.privilege, scope.role.privileges);
                });
            });
        }

        function getPrivilegesOfRole(scope) {
            scope.objectTypes.forEach(function (objectType) {
                objectType.actions.forEach(function (action) {
                    if (action.privilege.checked) {
                        scope.role.privileges.push(action.privilege);
                    }
                });
            });
        }
    }
})();
