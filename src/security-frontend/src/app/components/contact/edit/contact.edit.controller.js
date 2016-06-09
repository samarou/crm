(function () {
    'use strict';

    angular
        .module('crm.contact')
        .controller('contactEditController', contactEditController);

    /** @ngInject */
    function contactEditController($q, contactDetailsService, contactSecurityService, authService, contactService, $stateParams) {
        var vm = this;

        vm.canEdit = false;
        vm.contact = contactDetailsService.getEmptyContact();
        vm.title = 'Edit contact';
        vm.submitText = 'Save';
        vm.submit = submit;
        vm.details = contactDetailsService;
        vm.cancel = contactDetailsService.cancel;
        vm.isManager = authService.isManager();
        vm.aclHandler = contactDetailsService.createAclHandler(function () {
            return vm.contact.id;
        });

        init();

        function init() {
            $q.all(
                [
                    initDictionary(),
                    isEditable(),
                    getAcls()
                ]
            ).then(getContact);
        }

        function initDictionary() {
            return vm.details.getDictionary().then(function (response) {
                vm.dictionary = response;
            });
        }

        function submit() {
            contactDetailsService.submit(vm.contact, vm.aclHandler.acls, false);
        }

        function getAcls() {
            return contactService.getAcls($stateParams.id).then(function (response) {
                vm.aclHandler.acls = response.data;
            });
        }

        function isEditable() {
            return contactSecurityService.checkEditPermission($stateParams.id).then(function (canEdit) {
                vm.canEdit = canEdit;
                vm.aclHandler.canEdit = canEdit;
                if (!canEdit) {
                    vm.submitText = null;
                    vm.cancelText = 'Ok';
                }
            });
        }

        function getContact() {
            return contactService.get($stateParams.id).then(function (response) {
                vm.contact = response.data;
            });
        }
    }
})();
