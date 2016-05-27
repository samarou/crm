(function () {
    'use strict';

    angular
        .module('crm.company')
        .factory('companyService', companyService);

    /** @ngInject */
    function companyService($http) {

        return {
            find: find,
            create: create,
            get: get,
            update: update,
            remove: remove,
            getAcls: getAcls,
            updateAcls: updateAcls,
            removeAcl: removeAcl,
            isAllowed: isAllowed,
            getCompanyTypes: getCompanyTypes,
            getBusinessSpheres: getBusinessSpheres,
            getEmployeeCategories: getEmployeeCategories
        };

        function getCompanyTypes() {
            return $http.get('rest/companies/company_types', {cache: true});
        }

        function getBusinessSpheres() {
            return $http.get('rest/companies/business_spheres', {cache: true});
        }

        function getEmployeeCategories() {
            return $http.get('rest/companies/employee_number_categories', {cache: true});
        }

        function find(filter) {
            return $http.get('rest/companies', {params: filter});
        }

        function create(company) {
            return $http.post('rest/companies', company);
        }

        function get(id) {
            return $http.get('rest/companies/' + id);
        }

        function update(company) {
            return $http.put('rest/companies', company);
        }

        function remove(id) {
            return $http.delete('rest/companies/' + id);
        }

        function getAcls(id) {
            return $http.get('rest/companies/' + id + '/acls');
        }

        function updateAcls(id, acls) {
            return $http.put('rest/companies/' + id + '/acls', acls);
        }

        function removeAcl(id, aclId) {
            return $http.delete('rest/companies/' + id + '/acls/' + aclId);
        }

        function isAllowed(companyId, permission) {
            return $http.get('rest/companies/' + companyId + '/actions/' + permission);
        }

    }

})();
