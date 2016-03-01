(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', 'AuthService'];

    function LoginController($location, AuthService) {
        var vm = this;
        vm.login = function () {
            AuthService.login(vm.username, vm.password).then(
                function () {
                    $location.path(AuthService.isAdmin() ? "/users" : "/customers");
                },
                function (error) {
                    console.log(error);
                    vm.error = "Invalid login or password";
                });
        };
    }

})();