(function () {
    'use strict';

    angular
        .module('app')
        .service('AuthService', AuthService);

    AuthService.$inject = ['$http', '$q'];

    function AuthService($http, $q) {
        var service = this;

        service.authentication = null;

        function setAuthentication(authData) {
            service.authentication = authData;
            $http.defaults.headers.common['X-Auth-Token'] =
                authData ? authData.token : undefined;
            if (sessionStorage) {
                if (authData) {
                    sessionStorage.setItem("auth.data", JSON.stringify(authData));
                } else {
                    sessionStorage.removeItem("auth.data");
                }
            }
        }

        service.login = function (username, password) {
            return $http.post('/rest/login', {
                username: username,
                password: password
            }).then(function (response) {
                setAuthentication(response.data);
                return response;
            }, function (error) {
                setAuthentication(null);
                return $q.reject(error);
            });
        };

        service.logout = function () {
            setAuthentication(null);
        };

        service.restore = function () {
            if (sessionStorage) {
                var authData = sessionStorage.getItem("auth.data");
                if (authData) {
                    try {
                        setAuthentication(JSON.parse(authData));
                        return true;
                    } catch (e) {
                        console.log(e);
                    }
                }
            }
            return false;
        };

        service.isAuthenticated = function () {
            return !!service.authentication;
        };

        service.getAuthentication = function () {
            return angular.copy(service.authentication);
        };
    }

})();
