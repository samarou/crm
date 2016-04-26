(function () {
	'use strict';

	angular
			.module('crm')
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
				.state('home', {
					url: '/'
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
				/** Roles routes */
				.state('roles', {
					abstract: true,
					url: '/roles',
					template: '<ui-view/>'
				})
				.state('roles.list', {
					url: '/list',
					controller: 'RolesListController',
					templateUrl: 'app/components/role/list/roles.list.view.html',
					controllerAs: 'vm'
				})
				.state('roles.add', {
					url: '/add',
					controller: 'RolesAddController',
					templateUrl: 'app/components/role/roles.details.view.html',
					controllerAs: 'vm'
				})
				.state('roles.edit', {
					url: '/:id',
					controller: 'RolesEditController',
					templateUrl: 'app/components/role/roles.details.view.html',
					controllerAs: 'vm'
				})
				/** Contact routes */
				.state('contacts', {
					abstract: true,
					url: '/contacts',
					template: '<ui-view/>'
				})
				.state('contacts.list', {
					url: '/list',
					controller: 'ContactsListController',
					templateUrl: 'app/components/contact/list/contacts.list.view.html',
					controllerAs: 'vm'
				})
				.state('contacts.add', {
					url: '/add',
					controller: 'ContactsAddController',
					templateUrl: 'app/components/contact/contacts.details.view.html',
					controllerAs: 'vm'
				})
				.state('contacts.edit', {
					url: '/:id',
					controller: 'ContactsEditController',
					templateUrl: 'app/components/contact/contacts.details.view.html',
					controllerAs: 'vm'
				});

		$urlRouterProvider.otherwise('/');
	}

})();
