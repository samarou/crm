(function () {
    'use strict';

    angular
        .module('crm.user')
        .factory('userService', userService);

    /** @ngInject */
    function userService($http) {
        return {
            getPublicUsers: getPublicUsers,
            getAll: getAll,
            getById: getById,
            create: create,
            update: update,
            remove: remove,
            find: find,
            findPublicUsers: findPublicUsers,
            activate: activate,
            deactivate: deactivate,
            removeAcl: removeAcl,
            getAcls: getAcls,
            getDefaultAcls: getDefaultAcls
        };

        function getPublicUsers() {
            return $http.get('rest/users/public');
        }

        function getAll() {
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
            return $http.get('rest/users/find', {params: filter});
        }

        function findPublicUsers(filter) {
            return $http.get('rest/users/public/find', {params: filter});
        }

        function activate(id) {
            return $http.put('rest/users/activate/' + id);
        }

        function deactivate(id) {
            return $http.put('rest/users/deactivate/' + id);
        }

        function removeAcl(id, aclId) {
            return $http.delete('rest/users/' + id + '/acls/' + aclId);
        }

        function getAcls(id) {
            return $http.get('rest/users/' + id + '/acls');
        }

        function getDefaultAcls() {
            return $http.get('rest/users/current/acls');
        }
    }
})();
