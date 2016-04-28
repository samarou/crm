(function () {
    'use strict';

    angular
        .module('securityManagement')
        .service('ContactAttachmentService', ContactAttachmentService);

    /** @ngInject */
    function ContactAttachmentService(ContactService, $q, DialogService, FileUploader) {
        var vm = this;
        vm.tempAttachment = {};

        vm.getUploader = function () {
            return new FileUploader(
                {
                    onBeforeUploadItem: function (item) {
                        item.url = vm.uploader.url;
                    },
                    onAfterAddingFile: function (item) {
                        vm.tempAttachment.name = item._file.name;
                    },
                    withCredentials: true
                });
        };

        vm.addAttachments = function (scope) {
            DialogService.custom('app/components/contact/attachment/add/contact.attachments.add.view.html', {
                title: 'Add Attachments',
                size: 'modal--group-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                uploader: scope.uploader,
                attachment: vm.tempAttachment
            }).result.then(function (model) {
                model.attachment.file = scope.uploader.queue[scope.uploader.queue.length - 1]._file;
                model.attachment.uploadDate = new Date();
                scope.attachments.push(model.attachment);
                scope.attachment = {};
            });
        };

        vm.updateAttachment = function (scope, attachment) {
            DialogService.custom('app/components/contact/attachment/edit/contact.attachments.edit.view.html', {
                title: 'Update Attachment',
                size: 'modal--group-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                attachment: attachment
            }).result.then(function (model) {
                attachment = model.attachment;
                attachment.updated = true;
            });
        };

        vm.removeAttachments = function (scope) {
            var tasks = [];
            scope.attachments.forEach(function (attachment) {
                if (attachment.checked) {
                    DialogService.confirm('Do you really want to delete attachment ' + attachment.name + '?')
                        .result.then(function () {
                        if (attachment.id) {
                            tasks.push(ContactService.removeAttachment(attachment.contactId, attachment.id));
                        }
                        var index = scope.attachments.indexOf(attachment);
                        scope.attachments.splice(index, 1);
                    })
                }
            });
            return $q.all(tasks);
        };
        vm.getNewAttachments = function (attachments) {
            var newAttachments = [];
            attachments.forEach(function (attachment) {
                if (!attachment.contactId) {
                    newAttachments.push(attachment);
                }
            });
            return newAttachments;
        };

        vm.getUpdatedAttachments = function (attachments) {
            var updatedAttachments = [];
            attachments.forEach(function (attachment) {
                if (attachment.updated) {
                    updatedAttachments.push(attachment);
                }
            });
            return updatedAttachments;
        };

        vm.updateAdditionalValues = function (contactId, scope) {
            return ContactService.updatePermissions(contactId, scope.permissions).then(function () {
                var tasks = [];
                var newAttachments = vm.getNewAttachments(scope.attachments);
                var updatedAttachments = vm.getUpdatedAttachments(scope.attachments);
                newAttachments.forEach(function (attachment) {
                    tasks.push(ContactService.addAttachment(contactId, attachment));
                });
                if (updatedAttachments.length > 0) {
                    tasks.push(ContactService.updateAttachments(contactId, updatedAttachments));
                }
                return $q.all(tasks);
            })
        }
    }
})();
