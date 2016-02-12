(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', '$http', 'LoginService'];

    function LoginController($location, $http, LoginService) {
        var vm = this;
        vm.login = function () {
            LoginService.login(vm.username, vm.password).then(
                function (sessionInfo) {
                    console.log(sessionInfo);
                    $http.defaults.headers.common['X-Auth-Token'] = sessionInfo.token;
                    $location.path('/users');
                },
                function (error) {
                    console.log(error);
                    vm.error = error.statusText;
                    vm.password = '';
                });
        };
    }

})();