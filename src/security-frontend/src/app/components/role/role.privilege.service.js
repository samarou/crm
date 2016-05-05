/**
 * Created by anton.charnou on 13.04.2016.
 */
(function () {
	'use strict';
	angular
			.module('securityManagement')
			.factory('RolePrivilegeService', RolePrivilegeService);

	/** @ngInject */
	function RolePrivilegeService(Collections, PrivilegeService) {

		function getObjectTypes(scope) {
			return PrivilegeService.fetchAll().then(function (response) {
				var objectTypeList = Object.create(null);
				var privileges = response.data;
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
				scope.objectTypes = Collections.sort(Object.keys(objectTypeList)).map(function (objectTypeName) {
					return {
						objectTypeName: objectTypeName,
						actions: Collections.sort(objectTypeList[objectTypeName], true, Collections.propertyComparator('id'))
					};
				});
			});
		}

		function checkPrivilegesOfRole(scope) {
			scope.objectTypes.forEach(function (privilegeObject) {
				privilegeObject.actions.forEach(function (action) {
					action.privilege.checked = !!Collections.find(action.privilege, scope.role.privileges);
				})
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

		return {
			getObjectTypes: getObjectTypes,
			checkPrivilegesOfRole: checkPrivilegesOfRole,
			getPrivilegesOfRole: getPrivilegesOfRole
		}
	}
})();
