(function () {
    'use strict';

    angular
        .module('app')
        .config(config);

    config.$inject = ['$routeProvider', '$locationProvider'];

    function config($routeProvider, $locationProvider) {
        console.log("Config");
        $routeProvider

            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'app/login/login.view.html',
                controllerAs: 'vm'
            })
            .when('/user', {
                controller: 'UsersController',
                templateUrl: 'app/user/users.view.html',
                controllerAs: 'vm'
            })
            .when("/user/:param", {
                controller: "UserController",
                templateUrl: "app/user/user.view.html",
                controllerAs: "vm"
            })
            .when("/group", {
                controller: "GroupsController",
                templateUrl: "app/group/groups.view.html",
                controllerAs: "vm"
            })

            .otherwise({redirectTo: '/login'});
    }
})();
