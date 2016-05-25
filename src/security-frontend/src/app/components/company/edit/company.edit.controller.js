(function () {
    'use strict';

    angular
        .module('crm.company')
        .controller('companyEditController', companyEditController);

    /** @ngInject */
    function companyEditController(companyService, companyDetailsService, companySecurityService, $stateParams, $q) {
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
            ).then(getCompany)
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
            return companySecurityService.checkEditPermission($stateParams.id).then(function (canEdit) {
                vm.canEdit = canEdit;
                if (!canEdit) {
                    vm.submitText = null;
                    vm.cancelText = 'Ok';
                }
            })
        }

        function canAdmin() {
            return companySecurityService.checkAdminPermission($stateParams.id).then(function (canAdmin) {
                vm.aclHandler.canEdit = canAdmin;

            })
        }

        function getCompany() {
            companyService.get($stateParams.id).then(function (response) {
                vm.company = response.data;
            });
        }
    }

})();
