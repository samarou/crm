/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
    'use strict';

    angular
        .module('securityManagement')
        .controller('ContactsAddController', ContactsAddController);

    /** @ngInject */
    function ContactsAddController(ContactService, ContactPermissionsService, ContactAttachmentService, $state) {
        'use strict';
        var vm = this;
        vm.canEdit = true;
        vm.contact = {};
        vm.permissions = [];
        vm.attachments = [];
        vm.actions = ContactPermissionsService;
        vm.attachmentService = ContactAttachmentService;
        vm.attachments = [];

        vm.submitText = 'Add';
        vm.cancelText = 'Cancel';
        vm.title = 'Add contact';
        vm.uploader = ContactAttachmentService.getUploader();
        vm.getAttachment = ContactService.getAttachment;

        vm.submit = function () {
            ContactService.create(vm.contact).then(function (response) {
                var id = response.data;
                ContactAttachmentService.updateAdditionalValues(id, vm).then(
                    function () {
                        $state.go('contacts.list');
                    }
                );
            });
        };


        vm.cancel = function () {
            $state.go('contacts.list');
        };
    }
})();
