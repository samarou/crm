(function () {
    'use strict';

    angular
        .module('crm.company')
        .factory('companyService', companyService);

    /** @ngInject */
    function companyService($http) {

        var staticData = {};
        staticData.companyTypes = [];
        staticData.businessSpheres = [];
        staticData.employeeNumberCategories = [];

        init();

        return {
            find: find,
            create: create,
            get: get,
            update: update,
            remove: remove,
            staticData: staticData
        };

        function init() {
            $http.get('rest/companies/company_types').then(function (response) {
                staticData.companyTypes = response.data;
            });
            $http.get('rest/companies/business_spheres').then(function (response) {
                staticData.businessSpheres = response.data;
            });
            $http.get('rest/companies/employee_number_categories').then(function (response) {
                staticData.employeeNumberCategories = response.data;
            });
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

    }

})();