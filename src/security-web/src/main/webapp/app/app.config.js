(function () {
    'use strict';

    angular
        .module('app')
        .config(config);

    config.$inject = ['$routeProvider', '$httpProvider'];

    function config($routeProvider, $httpProvider) {
        console.log("Config");
        $routeProvider
            //todo: only stub, create separate controller if it need
            .when('/', {
                controller: 'LoginController',
                templateUrl: 'app/login/login.view.html',
                controllerAs: 'vm'
            })
            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'app/login/login.view.html',
                controllerAs: 'vm'
            })
            .when('/users', {
                controller: 'UsersController',
                templateUrl: 'app/user/users.view.html',
                controllerAs: 'vm'
            })
            .when("/users/:param", {
                controller: "UserController",
                templateUrl: "app/user/user.view.html",
                controllerAs: "vm"
            })
            .when("/groups", {
                controller: "GroupsController",
                templateUrl: "app/group/groups.view.html",
                controllerAs: "vm"
            })
            .when("/roles", {
                controller: "RolesController",
                templateUrl: "app/role/roles.view.html",
                controllerAs: "vm"
            })

            .otherwise({redirectTo: '/login'});

        $httpProvider.interceptors.push('HttpInterceptor');
    }
})();
