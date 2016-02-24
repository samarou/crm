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
        service.create = create;
        service.update = update;
        service.remove = remove;
        service.find = find;
        service.activate = activate;
        service.deactivate = deactivate;

        return service;

        function fetchAll(handleSuccess) {
            return $http.get('rest/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function getById(id, handleSuccess) {
            return $http.get('rest/users/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function create(user) {
            return $http.post('rest/users', user);
        }

        function update(user) {
            return $http.put('rest/users', user);
        }

        function remove(id) {
            return $http.delete('rest/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function find(filter, handleSuccess) {
            return $http.get("rest/users/find", {params: filter}).then(handleSuccess, handleError("Error during searching of users"))
        }

        function activate(id) {
            return $http.put("rest/users/activate/" + id);
        }

        function deactivate(id) {
            return $http.put("rest/users/deactivate/" + id);
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
