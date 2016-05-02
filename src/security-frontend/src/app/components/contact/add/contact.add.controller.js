(function () {
    'use strict';

    angular
        .module('crm.contact')
        .controller('contactAddController', contactAddController);

    /** @ngInject */
    function contactAddController(contactDetailsService, contactPermissionsService, contactAttachmentService) {
        var vm = this;

        vm.canEdit = true;
        vm.contact = {};
        vm.permissions = [];
        vm.attachments = [];
        vm.actions = contactPermissionsService;
        vm.title = 'Add contact';
        vm.submitText = 'Add';
        vm.submit = submit;
        vm.cancel = contactDetailsService.cancel;
        vm.attachmentService = contactAttachmentService;

        function submit() {
            contactDetailsService.submit(vm.contact, vm.permissions, vm.attachments, true);
        }
    }
})();
