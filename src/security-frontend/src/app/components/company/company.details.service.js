(function () {
    'use strict';

    angular
        .module('crm.company')
        .factory('companyDetailsService', companyDetailsService);

    /** @ngInject */
    function companyDetailsService(companyService, aclServiceBuilder, $state) {
        return {
            submit: submit,
            create: create,
            update: update,
            updateAcls: companyService.updateAcls,
            cancel: goToList,
            createAclHandler: createAclHandler,
            staticData: companyService.staticData
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
            })
        }

        function goToList() {
            $state.go('companies.list');
        }

        function createAclHandler(getId) {
            return {
                canEdit: true,
                acls: [],
                actions: aclServiceBuilder(getId, companyService)
            }
        }
    }
})();
