(function () {
    'use strict';

    angular
        .module('crm.company')
        .factory('companyDetailsService', companyDetailsService);

    /** @ngInject */
    function companyDetailsService(companyService, aclServiceBuilder, $state, $q) {
        return {
            submit: submit,
            create: create,
            update: update,
            updateAcls: companyService.updateAcls,
            cancel: goToList,
            createAclHandler: createAclHandler,
            getStaticData: getStaticData
        };

        function create(company) {
            return companyService.create(company).then(function (response) {
                return response.data;
            });
        }

        function update(company) {
            return companyService.update(company).then(function () {
                return company.id;
            });
        }

        function submit(promise) {
            promise.then(function () {
                goToList();
            });
        }

        function goToList() {
            $state.go('companies.list');
        }

        function getCompanyTypes(staticData) {
            return companyService.getCompanyTypes().then(function (response) {
                staticData.companyTypes = response.data;
            });
        }

        function getBusinessSpheres(staticData) {
            return companyService.getBusinessSpheres().then(function (response) {
                staticData.businessSpheres = response.data;
            });
        }

        function getEmployeeCategories(staticData) {
            return companyService.getEmployeeCategories().then(function (response) {
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

        function createAclHandler(getId) {
            return {
                canEdit: false,
                acls: [],
                actions: aclServiceBuilder(getId, companyService)
            };
        }
    }
})();
