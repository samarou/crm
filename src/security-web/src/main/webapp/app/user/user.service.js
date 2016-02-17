(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];

    function UserService($http) {
        console.log('UserService');

        var service = {};

        service.getAll = getAll;
        service.getById = getById;
        service.getByUsername = getByUsername;
        service.createUser = createUser;
        service.updateUser = updateUser;
        service.deleteUser = deleteUser;
        service.find = find;

        return service;

        function getAll(handleSuccess) {
            return $http.get('rest/user').then(handleSuccess, handleError('Error getting all users'));
        }

        function getById(id, handleSuccess) {
            return $http.get('rest/user/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function getByUsername(name, handleSuccess) {
            return $http.get('rest/user/' + name).then(handleSuccess, handleError('Error getting user by username'));
        }

        function createUser(user, handleSuccess) {
            return $http.post('rest/user', user).then(handleSuccess, handleError('Error creating user'));
        }

        function updateUser(user, handleSuccess) {
            return $http.put('rest/user', user).then(handleSuccess, handleError('Error updating user'));
        }

        function deleteUser(id) {
            return $http.delete('rest/user' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function find(filter, successHandler){
            return $http.get("rest/user/find", {params: filter}).then(successHandler, handleError("Error during search of users"))
        }

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return {
                    success: false,
                    message: error
                };
            };
        }
    }
})();
