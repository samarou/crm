(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactAttachmentService', contactAttachmentService);

    /** @ngInject */
    function contactAttachmentService(contactService, $q, dialogService, FileUploader, MAX_FILE_SIZE, authService) {

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
                    tempFile: {},
                    withCredentials: true,
                    url: 'rest/files',
                    onAfterAddingFile: function (item) {
                        if (item._file.size < MAX_FILE_SIZE) {
                            item.headers['X-Auth-Token'] = authService.getAuthentication().token;
                            this.tempFile.name = item._file.name;
                            this.uploadItem(item);
                        } else {
                            dialogService.notify('File size is too large. It should not exceed 100MB');
                            this.clearQueue();
                        }
                    },
                    onErrorItem: function (item, response) {
                        dialogService.error('File hasn\'t been uploaded because error happened: ' + response);
                        this.clearQueue();
                    },
                    onSuccessItem: function (item, response) {
                        this.tempFile.filePath = response;
                    }
                });
        }

        function addAttachments(scope) {
            var uploader = getFileUploader();
            openAddAttachmentDialog(uploader).then(function (model) {
                model.attachment.dateUpload = new Date();
                scope.attachments.push(model.attachment);
            });
        }

        function openAddAttachmentDialog(uploader) {
            return dialogService.custom('app/components/contact/attachment/add/contact.attachment.add.view.html', {
                title: 'Add Attachments',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                uploader: uploader,
                attachment: uploader.tempFile
            }).result;
        }

        function updateAttachment(contactId, attachment) {
            openUpdateAttachmentDialog(attachment).then(function (model) {
                attachment.name = model.attachment.name;
                attachment.comment = model.attachment.comment;
                if (attachment.id) {
                    contactService.updateAttachment(contactId, attachment);
                }
            });
        }

        function getAttachment(contactId, attachment) {
            return contactService.getAttachment(contactId, attachment);
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
                            tasks.push(contactService.removeAttachment(scope.contact.id, attachment.id));
                        }
                        var index = scope.attachments.indexOf(attachment);
                        scope.attachments.splice(index, 1);
                    });
                }
            });
            return $q.all(tasks);
        }

        function getNewAttachments(attachments) {
            var newAttachments = [];
            attachments.forEach(function (attachment) {
                if (!attachment.id) {
                    newAttachments.push(attachment);
                }
            });
            return newAttachments;
        }
    }
})();
