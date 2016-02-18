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
        service.find = find;
        service.count = count;

        return service;

        function fetchAll(handleSuccess) {
            return $http.get('rest/user').then(handleSuccess, handleError('Error getting all users'));
        }

        function getById(id, handleSuccess) {
            return $http.get('rest/user/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function getByUsername(name, handleSuccess) {
            return $http.get('rest/user/' + name).then(handleSuccess, handleError('Error getting user by username'));
        }

        function create(user, handleSuccess) {
            return $http.post('rest/user', user).then(handleSuccess, handleError('Error creating user'));
        }

        function update(user, handleSuccess) {
            return $http.put('rest/user', user).then(handleSuccess, handleError('Error updating user'));
        }

        function remove(id) {
            return $http.delete('rest/user' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function find(filter, handleSuccess) {
            return $http.get("rest/user/find", {params: filter}).then(handleSuccess, handleError("Error during searching of users"))
        }

        function count(handleSuccess) {
            return $http.get("rest/user/count").then(handleSuccess, handleError("Error during getting count of users"));
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
