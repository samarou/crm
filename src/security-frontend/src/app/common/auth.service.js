(function () {
    'use strict';

    angular
        .module('crm.common')
        .factory('authService', authService);

    /** @ngInject */
    function authService($http, $q, $log) {
        var service = this;

        service.authentication = null;

        return {
            login: login,
            logout: logout,
            restore: restore,
            getUserName: getUserName,
            isAuthenticated: isAuthenticated,
            getAuthentication: getAuthentication,
            setAuthentication: setAuthentication,
            isAdmin: isAdmin,
            isManager: isManager,
            isSpecialist: isSpecialist,
            getAuthStatus: getAuthStatus
        };

        function getUserName() {
            var auth = getAuthentication();
            return auth ? auth.username : null;
        }
        
        function setAuthentication(authData) {
            service.authentication = authData;
        }

        function login(username, password) {
            return $http.post('rest/login', {
                username: username,
                password: password
            }).then(function (response) {
                setAuthentication(response.data);
                return response;
            }, function (error) {
                setAuthentication(null);
                return $q.reject(error);
            });
        }

        function logout() {
            return getAuthStatus()
                .then(function () {
                    return $http.post('rest/logout').then(function () {
                        setAuthentication(null);
                        return $q.resolve();
                    });
                });
        }

        function restore() {
            return $http.get('rest/login/roles')
                .then(function (response) {
                        setAuthentication(response.data);
                        return $q.resolve(response);
                    }, function () {
                        setAuthentication(null);
                        return $q.reject();
                    }
                );
        }

        function isAuthenticated() {
            return !!service.authentication;
        }

        function getAuthStatus() {
            return $http.get('rest/login/check').then(
                function (response) {
                    $log.log(response);
                    if (response.data) {
                        return $q.resolve();
                    } else {
                        return $q.reject();
                    }
                }
            );
        }


        function getAuthentication() {
            return angular.copy(service.authentication);
        }

        function hasRole(role) {
            return isAuthenticated()
                && service.authentication
                && service.authentication.roles
                && service.authentication.roles.indexOf(role) !== -1;
        }

        function isAdmin() {
            return hasRole('ADMIN');
        }

        function isManager() {
            return hasRole('MANAGER');
        }

        function isSpecialist() {
            return hasRole('SPECIALIST');
        }
    }
})();
