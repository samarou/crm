(function () {
    'use strict';

    angular
        .module('app')
        .service('AuthService', AuthService);

    AuthService.$inject = ['$http', '$q'];

    function AuthService($http, $q) {

        var service = this;

        service.authenticated = false;

        function setAuthToken(token) {
            service.authenticated = !!token;
            $http.defaults.headers.common['X-Auth-Token'] = token;
            if (sessionStorage) {
                if (token) {
                    sessionStorage.setItem("auth.token", token);
                } else {
                    sessionStorage.removeItem("auth.token");
                }
            }
        }

        service.login = function (username, password) {
            return $http.post('/rest/login', {
                username: username,
                password: password
            }).then(function (response) {
                setAuthToken(response.data.token);
                return response;
            }, function (error) {
                setAuthToken(null);
                return $q.reject(error);
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
            return service.authenticated;
        };
    }

})();
