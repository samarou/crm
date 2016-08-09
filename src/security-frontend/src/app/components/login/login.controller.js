(function () {
    'use strict';

    angular
        .module('crm')
        .controller('LoginController', LoginController);

    function LoginController($state, authService) {
        var vm = this;

        vm.login = function () {
            authService.login(vm.username, vm.password, vm.rememberMe).then(goToHomePage).catch(showError);
        };

        function goToHomePage() {
            $state.go(authService.isAdmin() ? 'users.list' : 'contacts.list');
        }

        function showError() {
            vm.error = 'Invalid login or password';
        }
    }
})();
