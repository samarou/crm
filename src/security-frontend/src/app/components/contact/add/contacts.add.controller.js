/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
    'use strict';

    angular
        .module('securityManagement')
        .controller('ContactsAddController', ContactsAddController);

    /** @ngInject */
    function ContactsAddController(ContactService, ContactPermissionsService, ContactAttachmentService, $state, FileUploader) {
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
        vm.uploader = new FileUploader();


        vm.submit = function () {
            ContactService.create(vm.contact).then(function (response) {
                var id = response.data;
                updateAdditionalValues(id).then(
                    function () {
                        $state.go('contacts.list');
                    }
                );
            });
        };


        vm.cancel = function () {
            $state.go('contacts.list');
        };

        function updateAdditionalValues(id) {
            return ContactService.updatePermissions(id, vm.permissions).then(function () {
                return ContactService.addAttachments(id, ContactAttachmentService.getNewAttachments(vm.attachments)).then(function () {
                    return ContactService.updateAttachments(id, ContactAttachmentService.getUpdatedAttachments(vm.attachments))
                });
            })
        }
    }
})();
