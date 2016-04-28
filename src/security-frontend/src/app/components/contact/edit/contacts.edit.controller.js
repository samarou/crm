/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
    'use strict';

    angular
        .module('securityManagement')
        .controller('ContactsEditController', ContactsEditController);

    /** @ngInject */
    function ContactsEditController($q, $state, AuthService, ContactService, ContactPermissionsService, ContactAttachmentService, $stateParams, FileUploader) {
        'use strict';
        var vm = this;
        vm.canEdit = false;
        vm.contact = {};
        vm.attachment = {};
        vm.permissions = [];
        vm.attachments = [];
        vm.actions = ContactPermissionsService;
        vm.attachmentService = ContactAttachmentService;
        vm.uploader = new FileUploader(
            {
                onBeforeUploadItem: function (item) {
                    item.url = vm.uploader.url;
                },
                onAfterAddingFile: function (item) {
                    vm.attachment.name = vm.uploader.queue[vm.uploader.queue.length - 1]._file.name;
                },
                withCredentials: true
            });
        vm.isManager = AuthService.isManager();

        var permissions = {
            read: 'read',
            write: 'write',
            create: 'create',
            delete: 'delete',
            admin: 'admin'
        };

        vm.submitText = 'Save';
        vm.cancelText = 'Cancel';
        vm.title = 'Edit contact';
        $q.all(
            [
                ContactService.isAllowed($stateParams.id, permissions.write).then(function (response) {
                    vm.canEdit = !!response.data;
                    if (!vm.canEdit) {
                        vm.submitText = null;
                        vm.cancelText = 'Ok'
                    }

                }),
                ContactService.getPermissions($stateParams.id).then(function (response) {
                    vm.permissions = response.data;
                }),
                ContactService.getAttachments($stateParams.id).then(function (response) {
                    vm.attachments = response.data;
                })
            ]
        ).then(function () {
            ContactService.get($stateParams.id).then(function (response) {
                vm.contact = response.data;
                vm.uploader.url = '/rest/contacts/' + vm.contact.id + '/attachments';
            })
        });

        vm.submit = function () {
            ContactService.update(vm.contact).then(function () {
                updateAdditionalValues(vm.contact.id).then(
                    function () {
                        $state.go('contacts.list');
                    }
                )
            });
        };

        vm.cancel = function () {
            $state.go('contacts.list');
        };


        function updateAdditionalValues(id) {
            return ContactService.updatePermissions(id, vm.permissions).then(function () {
                var newAttachments = ContactAttachmentService.getNewAttachments(vm.attachments);
                newAttachments.forEach(function (attachment) {
                    console.log('attachment', attachment);
                    ContactService.addAttachment(id, attachment);
                });
                var updatedAttachments = ContactAttachmentService.getUpdatedAttachments(vm.attachments);
                if (updatedAttachments.length > 0) {
                    return ContactService.updateAttachments(id, updatedAttachments);
                };
                // vm.uploader.formData.push({attachments: vm.attachments});
                /* Array.prototype.push.apply(vm.uploader.queue[0].formData, [{}]);
                 var fd = vm.uploader.queue[0].formData[0];
                 console.log('attach', vm.attachments[0]);
                 fd.attachment = vm.attachments[0];

                 vm.uploader.queue[0].formData[0] = fd;
                 console.log('upload files at ', vm.uploader);
                 vm.uploader.uploadAll();*/
                // ContactService.addAttachment(id, vm.attachments[0], vm.uploader.queue[0]);

                /*var updatedAttachments = ContactAttachmentService.getUpdatedAttachments(vm.attachments);
                 if (updatedAttachments.length > 0) {
                 return ContactService.updateAttachments(id, updatedAttachments);
                 }*/
            })
        }
    }
})();
