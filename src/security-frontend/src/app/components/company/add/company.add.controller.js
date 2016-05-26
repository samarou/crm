(function () {
    'use strict';

    angular
        .module('crm.company')
        .controller('companyAddController', companyAddController);

    /** @ngInject */
    function companyAddController(companyDetailsService, userService) {
        var vm = this;

        vm.canEdit = true;
        vm.company = {};
        vm.submitText = 'Add';
        vm.title = 'Add company';
        vm.submit = submit;
        vm.cancel = companyDetailsService.cancel;
        vm.aclHandler = companyDetailsService.createAclHandler(function () {
            return vm.company.id;
        });
        vm.aclHandler.canEdit = vm.canEdit;
        vm.staticData = companyDetailsService.staticData;

        init();

        function init() {
            userService.getDefaultAcls().then(function (response) {
                vm.aclHandler.acls = response.data;
            });
        }

        function submit() {
            var promise = companyDetailsService.create(vm.company).then(function (id) {
                companyDetailsService.updateAcls(id, vm.aclHandler.acls);
            });
            companyDetailsService.submit(promise);
        }
    }

})();
