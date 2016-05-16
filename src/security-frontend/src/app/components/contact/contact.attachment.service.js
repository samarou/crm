(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactAttachmentService', contactAttachmentService);

    /** @ngInject */
    function contactAttachmentService(contactService, $q, dialogService, FileUploader, MAX_FILE_SIZE) {
        var service = this;
        service.tempAttachment = {};

        return {
            getAttachment: getAttachment,
            addAttachments: addAttachments,
            updateAttachment: updateAttachment,
            removeAttachments: removeAttachments,
            getNewAttachments: getNewAttachments
        };

        function getFileUploader() {
            return new FileUploader(
                {
                    onAfterAddingFile: function (item) {
                        service.tempAttachment.name = item._file.name;
                        service.tempAttachment.file = item._file;
                    }
                });
        }

        function addAttachments(scope) {
            openAddAttachmentDialog().then(function (model) {
                if (service.tempAttachment.file.size < MAX_FILE_SIZE) {
                    model.attachment.file = service.tempAttachment.file;
                    model.attachment.dateUpload = new Date();
                    scope.attachments.push(model.attachment);
                } else {
                    dialogService.notify('File size is too large. It should not exceed 100MB');
                }
                service.tempAttachment = {};
            });
        }

        function openAddAttachmentDialog() {
            var uploader = getFileUploader();
            return dialogService.custom('app/components/contact/attachment/add/contact.attachment.add.view.html', {
                title: 'Add Attachments',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                uploader: uploader,
                attachment: service.tempAttachment
            }).result;
        }

        function updateAttachment(attachment) {
            openUpdateAttachmentDialog(attachment).then(function (model) {
                attachment.name = model.attachment.name;
                attachment.comment = model.attachment.comment;
                if (attachment.contactId) {
                    contactService.updateAttachment(attachment.contactId, attachment)
                }
            });
        }

        function getAttachment(attachment) {
            return contactService.getAttachment(attachment);
        }

        function openUpdateAttachmentDialog(attachment) {
            return dialogService.custom('app/components/contact/attachment/edit/contact.attachment.edit.view.html', {
                title: 'Update Attachment',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                attachment: {name: attachment.name, comment: attachment.comment}
            }).result;
        }

        function removeAttachments(scope) {
            var tasks = [];
            scope.attachments.forEach(function (attachment) {
                if (attachment.checked) {
                    dialogService.confirm('Do you really want to delete attachment ' + attachment.name + '?')
                        .result.then(function () {
                        if (attachment.id) {
                            tasks.push(contactService.removeAttachment(attachment.contactId, attachment.id));
                        }
                        var index = scope.attachments.indexOf(attachment);
                        scope.attachments.splice(index, 1);
                    })
                }
            });
            return $q.all(tasks);
        }

        function getNewAttachments(attachments) {
            var newAttachments = [];
            attachments.forEach(function (attachment) {
                if (!attachment.contactId) {
                    newAttachments.push(attachment);
                }
            });
            return newAttachments;
        }
    }
})();
