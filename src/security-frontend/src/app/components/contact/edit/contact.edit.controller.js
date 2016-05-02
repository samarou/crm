(function () {
    'use strict';

    angular
        .module('crm.contact')
        .controller('contactEditController', contactEditController);

    /** @ngInject */
    function contactEditController($q, contactDetailsService, contactSecurityService, authService, contactService, contactPermissionsService, contactAttachmentService, $stateParams) {
        var vm = this;

        vm.canEdit = false;
        vm.contact = {};
        vm.permissions = [];
        vm.attachments = [];
        vm.actions = contactPermissionsService;
        vm.attachmentService = contactAttachmentService;
        vm.isManager = authService.isManager();
        vm.submitText = 'Save';
        vm.title = 'Edit contact';
        vm.submit = submit;
        vm.cancel = contactDetailsService.cancel;

        init();

        function init() {
            $q.all(
                [
                    isEditable(),
                    getPermissions(),
                    getAttachments()
                ]
            ).then(getContact);
        }

        function submit() {
            contactDetailsService.submit(vm.contact, vm.permissions, vm.attachments, false);
        }

        function getPermissions() {
            return contactService.getPermissions($stateParams.id).then(function (response) {
                vm.permissions = response.data;
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
                if (!vm.canEdit) {
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
