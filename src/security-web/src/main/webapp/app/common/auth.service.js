(function () {
    'use strict';

    angular
        .module('app')
        .service('AuthService', AuthService);

    AuthService.$inject = ['$http'];

    function AuthService($http) {

        var service = this;

        function setAuthToken(token) {
            service.authToken = token;
            $http.defaults.headers.common['X-Auth-Token'] = token;
            if (sessionStorage) {
                sessionStorage.setItem("auth.token", token);
            }
        }

        service.login = function (username, password) {
            return $http.post('/rest/login', {
                username: username,
                password: password
            }).then(function (response) {
                setAuthToken(response.data.token);
                return response;
            });
        };

        service.logout = function () {
            setAuthToken(null);
        };

        service.restore = function () {
            if (sessionStorage) {
                var token = sessionStorage.getItem("auth.token");
                if (token) {
                    setAuthToken(token);
                    return true;
                }
            }
            return false;
        };

        service.isAuthenticated = function () {
            return !!service.authToken;
        };
    }

})();
