(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', '$http', 'AuthService'];

    function LoginController($location, $http, AuthService) {
        var vm = this;
        vm.login = function () {
            AuthService.login(vm.username, vm.password).then(
                function (response) {
                    $location.path('/users');
                },
                function (error) {
                    console.log(error);
                    vm.error = "Invalid login or password";
                });
        };
    }

})();