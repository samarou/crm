(function () {
	'use strict';

	angular
			.module('crm')
			.controller('RolesListController', RolesListController);

	/** @ngInject */
	function RolesListController($q, roleService, rolePrivilegeService, $state) {
		var vm = this;
		vm.searchText = '';
		vm.pageRoles = [];
		vm.objectTypes = [];
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
		vm.updateFilterObject = updateFilterObject;
		vm.selectAll = selectAll;
		vm.selectOne = selectOne;
		vm.edit = edit;
		vm.add = add;
		vm.remove = remove;

		init();

		function init() {
			fetchAllRoles();
			rolePrivilegeService.getObjectTypes(vm);
		}

		function updateFilterObject() {
			vm.pagingFilterConfig.filterObject.name = vm.searchText;
			vm.pagingFilterConfig.filterObject.description = vm.searchText;
		}

		function selectAll(checked) {
			if (checked) {
				vm.pageRoles.forEach(function (role) {
					role.checked = true;
				});
			} else {
				vm.roleList.forEach(function (role) {
					role.checked = false;
				});
			}
		}

		function selectOne() {
			vm.isSelectedAll = vm.pageRoles.every(function (role) {
				return role.checked;
			});
		}

		function edit(role) {
			$state.go('roles.edit', {id: role.id});
		}

		function add() {
			$state.go('roles.add');
		}

		function remove() {
			var tasks = [];
			vm.pageRoles.forEach(function (role) {
				if (role.checked) {
					tasks.push(roleService.remove(role.id))
				}
			});
			$q.all(tasks).then(fetchAllRoles);
			vm.isSelectedAll = false;
		}

		function fetchAllRoles() {
			roleService.fetchAll().then(function (response) {
				vm.roleList = response.data;
				vm.pagingFilterConfig.totalItems = vm.roleList.length;
			});
		}
	}
})();
