(function () {
    'use strict';

    angular
        .module('crm.company')
        .controller('companyEditController', companyEditController);

    /** @ngInject */
    function companyEditController(companyService, companyDetailsService, companySecurityService, permissions,
                                   $stateParams, $q) {
        var vm = this;

        vm.canEdit = false;
        vm.company = {};
        vm.submitText = 'Edit';
        vm.title = 'Edit company';
        vm.submit = submit;
        vm.staticData = companyService.staticData;
        vm.cancel = companyDetailsService.cancel;
        vm.aclHandler = companyDetailsService.createAclHandler(function () {
            return vm.company.id;
        });

        init();

        function init() {
            $q.all(
                [
                    canEdit(),
                    canAdmin(),
                    getAcls()
                ]
            ).finally(getCompany);
        }

        function submit() {
            var promise = $q.all(
                [
                    companyDetailsService.update(vm.company),
                    updateAcls()
                ]
            );
            companyDetailsService.submit(promise);
        }

        function updateAcls() {
            return vm.aclHandler.canEdit ? companyDetailsService.updateAcls(vm.company.id, vm.aclHandler.acls)
                : $q.resolve();
        }

        function getAcls() {
            return companyService.getAcls($stateParams.id).then(function (response) {
                vm.aclHandler.acls = response.data;
            });
        }

        function canEdit() {
            return companySecurityService.checkPermission($stateParams.id, permissions.write).then(function () {
                vm.canEdit = true;
            }).catch(function () {
                vm.submitText = null;
                vm.cancelText = 'Ok';
            });
        }

        function canAdmin() {
            return companySecurityService.checkPermission($stateParams.id, permissions.admin).then(function () {
                vm.aclHandler.canEdit = true;
            });
        }

        function getCompany() {
            companyService.get($stateParams.id).then(function (response) {
                vm.company = response.data;
            });
        }
    }

})();
