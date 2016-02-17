(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];

    function UserService($http) {
        console.log('UserService');

        var service = {};

        service.getAll = fetchAll;
        service.getById = getById;
        service.getByUsername = getByUsername;
        service.create = create;
        service.update = update;
        service.remove = remove;

        return service;

        function fetchAll(handleSuccess) {
            return $http.get('rest/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function getById(id, handleSuccess) {
            return $http.get('rest/users/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function getByUsername(name, handleSuccess) {
            return $http.get('rest/users/' + name).then(handleSuccess, handleError('Error getting user by username'));
        }

        function create(user, handleSuccess) {
            return $http.post('rest/users', user).then(handleSuccess, handleError('Error creating user'));
        }

        function update(user, handleSuccess) {
            return $http.put('rest/users', user).then(handleSuccess, handleError('Error updating user'));
        }

        function remove(id) {
            return $http.delete('rest/users' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function find(filter, successfullyHandler) {
            return $http.get("rest/users", filter).then(successfullyHandler, handleError("Error during searching of users"))
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
