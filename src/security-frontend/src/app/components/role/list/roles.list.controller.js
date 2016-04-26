/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesListController', RolesListController);

	/** @ngInject */
	function RolesListController($q, roleService, rolePrivilegeService, $state) {
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

		fetchAllRoles();
		rolePrivilegeService.getObjectTypes(vm);


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

		function fetchAllRoles() {
			roleService.fetchAll().then(function (response) {
				vm.roleList = response.data;
				vm.pagingFilterConfig.totalItems = vm.roleList.length;
			});
		}
	}
})();
