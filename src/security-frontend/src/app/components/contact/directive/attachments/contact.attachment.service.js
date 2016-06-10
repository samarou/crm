(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactAttachmentService', contactAttachmentService);

    /** @ngInject */
    function contactAttachmentService(contactService, dialogService, FileUploader, MAX_FILE_SIZE, contactCommonService) {

        return {
            add: addAttachment,
            get: getAttachment,
            edit: editAttachment,
            remove: removeAttachments
        };

        function getFileUploader() {
            return new FileUploader(
                {
                    tempFile: {},
                    withCredentials: true,
                    url: 'rest/files',
                    onAfterAddingFile: function (item) {
                        if (item._file.size < MAX_FILE_SIZE) {
                            this.tempFile.name = item._file.name;
                            this.uploadItem(item);
                        } else {
                            dialogService.notify('File size is too large. It should not exceed 100MB');
                            this.clearQueue();
                        }
                    },
                    onErrorItem: function (item, response) {
                        dialogService.error('File hasn\'t been uploaded because error happened: ' + response.message);
                        this.clearQueue();
                    },
                    onSuccessItem: function (item, response) {
                        this.tempFile.filePath = response;
                    }
                });
        }

        function addAttachment(scope) {
            var uploader = getFileUploader();
            openAddAttachmentDialog(uploader).then(function (model) {
                model.attachment.dateUpload = new Date();
                scope.contact.attachments.push(model.attachment);
            });
        }

        function getAttachment(contactId, attachment) {
            return contactService.getAttachment(contactId, attachment);
        }

        function editAttachment(attachment) {
            openUpdateAttachmentDialog(attachment).then(function (model) {
                attachment.name = model.attachment.name;
                attachment.comment = model.attachment.comment;
            });
        }

        function removeAttachments(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.attachments, contactService.removeAttachment);
        }

        function openAddAttachmentDialog(uploader) {
            return dialogService.custom('app/components/contact/directive/attachments/add/contact.attachment.add.view.html', {
                title: 'Add Attachment',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                uploader: uploader,
                attachment: uploader.tempFile
            }).result;
        }

        function openUpdateAttachmentDialog(attachment) {
            return dialogService.custom('app/components/contact/directive/attachments/edit/contact.attachment.edit.view.html', {
                title: 'Update Attachment',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                attachment: {name: attachment.name, comment: attachment.comment}
            }).result;
        }
    }
})();
