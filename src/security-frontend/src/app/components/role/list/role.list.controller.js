(function () {
	'use strict';

	angular
			.module('crm.role')
			.controller('RolesListController', RolesListController);

	/** @ngInject */
	function RolesListController($q, roleService, rolePrivilegeService, pagingFilter, $state) {
		var vm = this;

		vm.searchText = '';
		vm.pageRoles = [];
		vm.objectTypes = [];
		vm.pagingFilterConfig = pagingFilter.config;
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
			pagingFilter.updateFilterObject(vm.searchText)
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
				pagingFilter.setLength(vm.roleList.length);
			});
		}
	}
})();
