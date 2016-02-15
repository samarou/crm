(function () {
    'use strict';

    angular
        .module('app')
        .service('LoginService', LoginService);

    LoginService.$inject = ['$http'];

    function LoginService($http) {
        this.login = function (username, password) {
            return $http.post('/rest/login', {
                username: username,
                password: password
            });
        }
    }

})();
