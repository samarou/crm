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
                templateUrl: 'app/components/login/login.view.html',
                controllerAs: 'vm'
            })
            .when('/users', {
                controller: 'UsersController',
                templateUrl: 'app/components/user/users.view.html',
                controllerAs: 'vm'
            })
            .when("/groups", {
                controller: "GroupsController",
                templateUrl: "app/components/group/groups.view.html",
                controllerAs: "vm"
            })
            .when("/roles", {
                controller: "RolesController",
                templateUrl: "app/components/role/roles.view.html",
                controllerAs: "vm"
            })
            .when("/customers", {
                controller: "CustomersController",
                templateUrl: "app/components/customer/customers.view.html",
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
