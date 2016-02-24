(function () {
    'use strict';

    angular
        .module('app')
        .config(config)
        .run(run);

    config.$inject = ['$routeProvider', '$httpProvider', '$uibModalProvider'];

    function config($routeProvider, $httpProvider, $uibModalProvider) {
        console.log("Config");
        $routeProvider
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

        $uibModalProvider.options = {backdrop: 'static', keyboard: false};
    }

    run.$inject = ['$rootScope', '$location', 'AuthService'];

    function run($rootScope, $location, AuthService) {
        $rootScope.$on('$routeChangeStart', function (event, next) {
            if (next.controller == 'LoginController') {
                AuthService.logout();
            } else if (!AuthService.isAuthenticated()) {
                console.log("Try to restore token");
                if (!AuthService.restore()) {
                    $location.path('/login');
                }
            }
        });
    }

})();
