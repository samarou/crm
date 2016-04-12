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
				/** Users routes */
				.state('users', {
					abstract: true,
					url: '/users',
					template: '<ui-view/>'
				})
				.state('users.list', {
					url: '/list',
					controller: 'UsersListController',
					templateUrl: 'app/components/user/list/users.list.view.html',
					controllerAs: 'vm'
				})
				.state('users.add', {
					url: '/add',
					controller: 'UsersAddController',
					templateUrl: 'app/components/user/users.details.view.html',
					controllerAs: 'vm'
				})
				.state('users.edit', {
					url: '/:id',
					controller: 'UsersEditController',
					templateUrl: 'app/components/user/users.details.view.html',
					controllerAs: 'vm'
				})
				/** Group routes */
				.state('groups', {
					abstract: true,
					url: '/groups',
					template: '<ui-view/>'
				})
				.state('groups.list', {
					url: '/list',
					controller: 'GroupsListController',
					templateUrl: 'app/components/group/list/groups.list.view.html',
					controllerAs: 'vm'
				})
				.state('groups.add', {
					url: '/add',
					controller: 'GroupsAddController',
					templateUrl: 'app/components/group/groups.details.view.html',
					controllerAs: 'vm'
				})
				.state('groups.edit', {
					url: '/:id',
					controller: 'GroupsEditController',
					templateUrl: 'app/components/group/groups.details.view.html',
					controllerAs: 'vm'
				})

				.state('roles', {
					url: '/roles',
					controller: 'RolesController',
					templateUrl: 'app/components/role/roles.view.html',
					controllerAs: 'vm'
				})
				.state('contacts', {
					url: '/contacts',
					controller: 'ContactsController',
					templateUrl: 'app/components/contact/contacts.view.html',
					controllerAs: 'vm'
				});

		$urlRouterProvider.otherwise('/login');
	}

})();
