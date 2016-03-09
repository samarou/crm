(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];

    function UserService($http) {
        console.log('UserService');

        var service = {};

        service.getPublicUsers = getPublicUsers;
        service.getAll = fetchAll;
        service.getById = getById;
        service.create = create;
        service.update = update;
        service.remove = remove;
        service.find = find;
        service.findPublicUsers = findPublicUsers;
        service.activate = activate;
        service.deactivate = deactivate;

        return service;

        function getPublicUsers(){
            return $http.get("rest/users/public");
        }

        function fetchAll() {
            return $http.get('rest/users');
        }

        function getById(id) {
            return $http.get('rest/users/' + id);
        }

        function create(user) {
            return $http.post('rest/users', user);
        }

        function update(user) {
            return $http.put('rest/users', user);
        }

        function remove(id) {
            return $http.delete('rest/users/' + id);
        }

        function find(filter) {
            return $http.get("rest/users/find", {params: filter});
        }

        function findPublicUsers(filter){
            return $http.get("rest/users/public/find", {params: filter});
        }

        function activate(id) {
            return $http.put("rest/users/activate/" + id);
        }

        function deactivate(id) {
            return $http.put("rest/users/deactivate/" + id);
        }
    }
})();
