/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesListController', RolesListController);

	/** @ngInject */
	function RolesListController($q, roleService, privilegeService, dialogService, collections, $state) {
		'use strict';

		var vm = this;

		vm.searchText = '';
		vm.pageRoles = [];

		// todo: resolve problem with filtering

		vm.pagingFilterConfig = {
			currentPage: 1,
			itemsPerPage: 10,
			visiblePages: 5,
			totalItems: null,
			privilegeSearchText: null,
			filterObject: {
				name: ''
			},
			sortProperty: 'name',
			sortAsc: true
		};
		vm.objectTypes = [];

		function fetchAllRoles() {
			roleService.fetchAll().then(function (response) {
				vm.roleList = response.data;
				vm.pagingFilterConfig.totalItems = vm.roleList.length;
			});
		}

		fetchAllRoles();


		privilegeService.getAll().then(function (response) {
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
			vm.objectTypes = collections.sort(Object.keys(objectTypeList)).map(function (objectTypeName) {
				return {
					objectTypeName: objectTypeName,
					actions: collections.sort(objectTypeList[objectTypeName], true, collections.byProperty('id'))
				};
			});
		});

		vm.updateFilterObject = function () {
			vm.pagingFilterConfig.filterObject.name = vm.searchText;
			vm.pagingFilterConfig.filterObject.description = vm.searchText;
		};

		vm.selectAll = function (checked) {
			if (checked) {
				vm.pageRoles.forEach(function (role) {
					role.checked = true;
				});
			} else {
				vm.roleList.forEach(function (role) {
					role.checked = false;
				});
			}
		};

		vm.selectOne = function () {
			vm.isSelectedAll = vm.pageRoles.every(function (role) {
				return role.checked;
			});
		};

		vm.editOld = function (role) {
			checkPrivilegesOfRole(role);
			showDialog({
				title: 'Editing Role',
				okTitle: 'Update',
				objectTypes: vm.objectTypes,
				role: angular.copy(role)
			});
		};

		vm.create = function () {
			checkPrivilegesOfRole({});
			showDialog({
				title: 'Create a New Role',
				okTitle: 'Add',
				objectTypes: vm.objectTypes,
				role: {}
			});
		};

		vm.edit = function (role) {
			$state.go('roles.edit', {id: role.id});
		};

		vm.add = function () {
			$state.go('roles.add');
		};

		vm.remove = function () {
			var tasks = [];
			vm.pageRoles.forEach(function (role) {
				if (role.checked) {
					tasks.push(roleService.remove(role.id))
				}
			});
			$q.all(tasks).then(fetchAllRoles);
			vm.isSelectedAll = false;
		};

		function update(role) {
			role.privileges = [];
			vm.objectTypes.forEach(function (objectType) {
				objectType.actions.forEach(function (action) {
					if (action.privilege.checked) {
						role.privileges.push(action.privilege);
					}
				});
			});
			if (role.id) {
				var originRole = vm.roleList.find(function (r) {
					return r.id === role.id
				});
				angular.copy(role, originRole);
				roleService.update(role);
			} else {
				roleService.create(role).then(function (response) {
					role.id = response.data;
					vm.roleList.push(role);
					vm.pagingFilterConfig.totalItems = vm.roleList.length;
				});
			}
		}

		function checkPrivilegesOfRole(role) {
			vm.objectTypes.forEach(function (privilegeObject) {
				privilegeObject.actions.forEach(function (action) {
					action.privilege.checked = !!collections.find(action.privilege, role.privileges);
				})
			});
		}

		function showDialog(model) {
			var dialog = dialogService.custom('app/components/role/role.modal.view.html', model);
			dialog.result.then(function (model) {
				update(model.role);
			});
		}
	}
})();
