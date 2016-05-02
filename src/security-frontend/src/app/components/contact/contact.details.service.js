(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactDetailsService', contactDetailsService);

    /** @ngInject */
    function contactDetailsService(contactService, $state, contactAttachmentService, $q) {
        return {
            submit: submit,
            cancel: goToList
        };

        function submit(contact, permissions, attachments, isNew) {
            if (isNew) {
                contactService.create(contact).then(function (response) {
                    var id = response.data;
                    updatePermissionsAndAttachments(id, permissions, attachments).then(goToList);
                });
            } else {
                contactService.update(contact).then(function () {
                    updatePermissionsAndAttachments(contact.id, permissions, attachments).then(goToList);
                });
            }
        }

        function updatePermissionsAndAttachments(contactId, permissions, attachments) {
            return contactService.updatePermissions(contactId, permissions).then(function () {
                var tasks = [];
                var newAttachments = contactAttachmentService.getNewAttachments(attachments);
                newAttachments.forEach(function (attachment) {
                    tasks.push(contactService.addAttachment(contactId, attachment));
                });
                return $q.all(tasks);
            })
        }

        function goToList() {
            $state.go('contacts.list');
        }
    }
})();
