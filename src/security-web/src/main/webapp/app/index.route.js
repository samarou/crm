(function () {
	'use strict';

	angular
			.module('securityManagement')
			.config(routerConfig);

	/** @ngInject */
	function routerConfig($stateProvider, $urlRouterProvider) {
		$stateProvider
				.state('login', {
					url: '/login',
					controller: 'LoginController',
					templateUrl: 'app/components/login/login.view.html',
					controllerAs: 'vm'
				})
				.state('users', {
					url: '/users',
					controller: 'UsersController',
					templateUrl: 'app/components/user/users.view.html',
					controllerAs: 'vm'
				})
				.state('groups', {
					url: '/groups',
					controller: 'GroupsController',
					templateUrl: 'app/components/group/groups.view.html',
					controllerAs: 'vm'
				})
				.state('roles', {
					url: '/roles',
					controller: 'RolesController',
					templateUrl: 'app/components/role/roles.view.html',
					controllerAs: 'vm'
				})
				.state('customers', {
					url: '/customers',
					controller: 'CustomersController',
					templateUrl: 'app/components/customer/customers.view.html',
					controllerAs: 'vm'
				});

		$urlRouterProvider.otherwise('/login');
	}

})();
