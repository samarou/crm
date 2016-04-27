(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('UsersAddController', UserAddController);


	/** @ngInject */
	function UserAddController(UserService, GroupService, RoleService, AclDialogServiceBuilder, AclServiceBuilder, $state) {
		'use strict';
		var vm = this;

    vm.submitText = 'Save';
    vm.cancelText = 'Cancel';
		vm.title = 'Add user';

    vm.user = {active: 'true'};

    vm.gtoups = [];
		vm.roles = [];
    vm.aclHandler = {
      canEdit: true,
      acls:[],
      actions: AclDialogServiceBuilder(AclServiceBuilder(getId, UserService))
    };

		GroupService.getAll().then(function (response) {
			vm.groups = response.data;
		});
		RoleService.fetchAll().then(function (response) {
			vm.roles = response.data;
		});

		vm.submit = function () {
			checkGroups(vm.user);
			checkRoles(vm.user);
      vm.user.acls = vm.aclHandler.acls;
			UserService.create(vm.user).then(function () {
				$state.go('users.list');
			})
		};

		vm.cancel = function () {
			$state.go('users.list');
		};

    function getId() {
      return vm.user.id;
    }

    function checkGroups(user) {
			user.groups = vm.groups.filter(function (group) {
				return group.checked;
			});
		}

		function checkRoles(user) {
			user.roles = vm.roles.filter(function (role) {
				return role.checked;
			});
		}
	}
})();
