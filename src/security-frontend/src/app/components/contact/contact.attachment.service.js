(function () {
    'use strict';

    angular
        .module('securityManagement')
        .service('ContactAttachmentService', ContactAttachmentService);

    /** @ngInject */
    function ContactAttachmentService(ContactService, $q, DialogService) {
        var vm = this;

        vm.addAttachments = function (scope) {
            console.log(scope.uploader);
            console.log(typeof scope.uploader);
            DialogService.custom('app/components/contact/attachment/add/contact.attachments.add.view.html', {
                title: 'Add Attachments',
                size: 'modal--group-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                uploader: scope.uploader,
                attachment: scope.attachment
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
            console.log('new attachments', newAttachments);
            return newAttachments;
        };

        vm.getUpdatedAttachments = function (attachments) {
            var updatedAttachments = [];
            attachments.forEach(function (attachment) {
                if (attachment.updated) {
                    updatedAttachments.push(attachment);
                }
            });
            console.log('updated attachments', updatedAttachments);
            return updatedAttachments;
        };
    }
})();
