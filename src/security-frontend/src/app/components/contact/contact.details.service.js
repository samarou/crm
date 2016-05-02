(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactDetailsService', contactDetailsService);
    /** @ngInject */
    function contactDetailsService(contactService, aclServiceBuilder, $state, contactAttachmentService, $q) {
        return {
            submit: submit,
            cancel: goToList,
            createAclHandler: createAclHandler
        };

        function createAclHandler(getId) {
            return {
                canEdit: true,
                acls: [],
                actions: aclServiceBuilder(getId, contactService)
            }
        }

        function submit(contact, acls, isNew) {
            if (isNew) {
                contactService.create(contact).then(function (response) {
                    var id = response.data;
                    updateAclsAndAttachments(id, acls, attachments).then(goToList);
                });
            } else {
                contactService.update(contact).then(function () {
                    updateAclsAndAttachments(contact.id, acls, attachments).then(goToList);
                });
            }
        }

        function updateAclsAndAttachments(contactId, acls, attachments) {
            return contactService.updateAcls(contactId, acls).then(function () {
                return updateAttachments(contactId, attachments);
            })
        }

        function updateAttachments(contactId, attachments) {
            var tasks = [];
            var newAttachments = contactAttachmentService.getNewAttachments(attachments);
            newAttachments.forEach(function (attachment) {
                tasks.push(contactService.addAttachment(contactId, attachment));
            });
            return $q.all(tasks);
        }

        function goToList() {
            $state.go('contacts.list');
        }
    }
})();
