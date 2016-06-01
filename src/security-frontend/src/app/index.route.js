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
                templateUrl: 'app/components/user/list/user.list.view.html',
                controllerAs: 'vm'
            })
            .state('users.add', {
                url: '/add',
                controller: 'UsersAddController',
                templateUrl: 'app/components/user/user.details.view.html',
                controllerAs: 'vm'
            })
            .state('users.edit', {
                url: '/:id',
                controller: 'UsersEditController',
                templateUrl: 'app/components/user/user.details.view.html',
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
                templateUrl: 'app/components/group/list/group.list.view.html',
                controllerAs: 'vm'
            })
            .state('groups.add', {
                url: '/add',
                controller: 'GroupsAddController',
                templateUrl: 'app/components/group/group.details.view.html',
                controllerAs: 'vm'
            })
            .state('groups.edit', {
                url: '/:id',
                controller: 'GroupsEditController',
                templateUrl: 'app/components/group/group.details.view.html',
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
                templateUrl: 'app/components/role/list/role.list.view.html',
                controllerAs: 'vm'
            })
            .state('roles.add', {
                url: '/add',
                controller: 'RolesAddController',
                templateUrl: 'app/components/role/role.details.view.html',
                controllerAs: 'vm'
            })
            .state('roles.edit', {
                url: '/:id',
                controller: 'RolesEditController',
                templateUrl: 'app/components/role/role.details.view.html',
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
                controller: 'contactListController',
                templateUrl: 'app/components/contact/list/contact.list.view.html',
                controllerAs: 'vm'
            })
            .state('contacts.add', {
                url: '/add',
                controller: 'contactAddController',
                templateUrl: 'app/components/contact/contact.details.view.html',
                controllerAs: 'vm'
            })
            .state('contacts.edit', {
                url: '/:id',
                controller: 'contactEditController',
                templateUrl: 'app/components/contact/contact.details.view.html',
                controllerAs: 'vm'
            })
            /** Company routes */
            .state('companies', {
                abstract: true,
                url: '/companies',
                template: '<ui-view/>'
            })
            .state('companies.list', {
                url: '/list',
                controller: 'companyListController',
                templateUrl: 'app/components/company/list/company.list.view.html',
                controllerAs: 'vm'
            })
            .state('companies.add', {
                url: '/add',
                controller: 'companyAddController',
                templateUrl: 'app/components/company/company.details.view.html',
                controllerAs: 'vm'
            })
            .state('companies.edit', {
                url: '/:id',
                controller: 'companyEditController',
                templateUrl: 'app/components/company/company.details.view.html',
                controllerAs: 'vm'
            })
            /*Task routes*/
            .state('tasks', {
                abstract: true,
                url: '/tasks',
                template: '<ui-view/>'
            })
            .state('tasks.list', {
                url: '/list',
                controller: 'TaskListController',
                templateUrl: 'app/components/task/list/task.list.view.html',
                controllerAs: 'vm'
            })
            .state('tasks.add', {
                url: '/add',
                controller: 'TaskAddController',
                templateUrl: 'app/components/task/task.details.view.html',
                controllerAs: 'vm'
            })
            .state('tasks.edit', {
                url: '/:id',
                controller: 'TaskEditController',
                templateUrl: 'app/components/task/task.details.view.html',
                controllerAs: 'vm'
            })
        ;

        $urlRouterProvider.otherwise('/');
    }

})();
