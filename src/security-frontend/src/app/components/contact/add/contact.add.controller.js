(function () {
    'use strict';

    angular
        .module('crm.contact')
        .controller('contactAddController', contactAddController);

    /** @ngInject */
    function contactAddController(contactDetailsService, userService) {
        var vm = this;

        vm.canEdit = true;
        vm.contact = contactDetailsService.getEmptyContact();
        vm.title = 'Add contact';
        vm.submitText = 'Add';
        vm.submit = submit;
        vm.contact.id = 0;
        vm.details = contactDetailsService;
        vm.cancel = contactDetailsService.cancel;
        vm.aclHandler = contactDetailsService.createAclHandler(function () {
            return vm.contact.id;
        });

        init();

        function init() {
            return initAcls().then(initDictionary).then(initNationality);
        }

        function initAcls() {
            return userService.getDefaultAcls().then(function (response) {
                vm.aclHandler.acls = response.data;
            });
        }

        function initDictionary() {
            return vm.details.getDictionary().then(function (response) {
                vm.dictionary = response;
            });
        }

        function initNationality() {
            return vm.details.getNationalities().then(function (response) {
                vm.nationalities = response;
            })
        }

        function submit() {
            contactDetailsService.submit(vm.contact, vm.aclHandler.acls, true);
        }
    }
})();
