(function () {
    'use strict';

    angular
        .module('crm.company')
        .factory('companyService', companyService);

    /** @ngInject */
    function companyService($http, $q) {

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
            getStaticData: getStaticData
        };

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

        function getCompanyTypes(staticData) {
            return $http.get('rest/companies/company_types', {cache: true}).then(function (response) {
                staticData.companyTypes = response.data;
            });
        }

        function getBusinessSpheres(staticData) {
            return $http.get('rest/companies/business_spheres', {cache: true}).then(function (response) {
                staticData.businessSpheres = response.data;
            });
        }

        function getEmployeeCategories(staticData) {
            return $http.get('rest/companies/employee_number_categories', {cache: true}).then(function (response) {
                staticData.employeeNumberCategories = response.data;
            });
        }

        function getStaticData() {
            var staticData = {
                companyTypes: [],
                businessSpheres: [],
                employeeNumberCategories: []
            };
            return $q.all([
                getCompanyTypes(staticData),
                getBusinessSpheres(staticData),
                getEmployeeCategories(staticData)
            ]).then(function () {
                return $q.resolve(staticData);
            });
        }

    }

})();
