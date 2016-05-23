(function () {
    'use strict';

    angular
        .module('crm.contact')
        .controller('contactEditController', contactEditController);

    /** @ngInject */
    function contactEditController($q, contactDetailsService, contactSecurityService, authService, contactService, contactAttachmentService, $stateParams) {
        var vm = this;

        vm.canEdit = false;
        vm.contact = contactDetailsService.getEmptyContact();
        vm.attachments = [];
        vm.attachmentService = contactAttachmentService;
        vm.isManager = authService.isManager();
        vm.submitText = 'Save';
        vm.title = 'Edit contact';
        vm.submit = submit;
        vm.details = contactDetailsService;
        vm.cancel = contactDetailsService.cancel;
        vm.aclHandler = contactDetailsService.createAclHandler(function () {
            return vm.contact.id;
        });

        init();

        function init() {
            $q.all(
                [
                    initDictionary(),
                    isEditable(),
                    getAcls(),
                    getAttachments()
                ]
            ).then(getContact);
        }

        function initDictionary() {
            return vm.details.getDictionary().then(function (response) {
                vm.dictionary = response;
            });
        }

        function submit() {
            contactDetailsService.submit(vm.contact, vm.aclHandler.acls, vm.attachments, false);
        }

        function getAcls() {
            return contactService.getAcls($stateParams.id).then(function (response) {
                vm.aclHandler.acls = response.data;
            })
        }

        function getAttachments() {
            return contactService.getAttachments($stateParams.id).then(function (response) {
                vm.attachments = response.data;
            })
        }

        function isEditable() {
            return contactSecurityService.checkEditPermission($stateParams.id).then(function (canEdit) {
                vm.canEdit = canEdit;
                vm.aclHandler.canEdit = canEdit;
                if (!canEdit) {
                    vm.submitText = null;
                    vm.cancelText = 'Ok'
                }
            })
        }

        function getContact() {
            return contactService.get($stateParams.id).then(function (response) {
                vm.contact = response.data;
            });
        }
    }
})();
