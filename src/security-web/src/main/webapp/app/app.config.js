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
            .when('/users', {
                controller: 'UserListController',
                templateUrl: 'app/user/userList.view.html',
                controllerAs: 'vm'
            })
 
            .otherwise({ redirectTo: '/login' });
    }
})();