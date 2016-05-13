(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactDetailsService', contactDetailsService);
    /** @ngInject */
    function contactDetailsService(contactService, aclServiceBuilder, $state, contactAttachmentService, $q) {
        var vm = this;
        vm.options = {};

        return {
            submit: submit,
            cancel: goToList,
            createAclHandler: createAclHandler,
            addEmail: addEmail,
            removeEmails: removeEmails,
            now: new Date(),
            init: init
        };

        function init() {
            return $q.all(
                [
                    getEmailTypes(),
                    getTelephoneTypes()
                ]
            ).then(function () {
                return vm.options;
            })
        }

        function getEmailTypes() {
            return contactService.getEmailTypes().then(function (response) {
                vm.options.emailTypes = response.data;
            })
        }

        function getTelephoneTypes() {
            return contactService.getTelephoneTypes().then(function (response) {
                vm.options.telephoneTypes = response.data;
            })
        }

        function addEmail(scope) {
            scope.contact.emails.push({});
        }

        function removeEmails(scope) {
            var tasks = [];
            var emails = scope.contact.emails;
            getCheckedEmails(emails)
                .forEach(function (email) {
                    if (email.id) {
                        tasks.push(contactService.removeEmail(scope.contact.id, email.id));
                    }
                    var index = emails.indexOf(email);
                    emails.splice(index, 1);
                });
            return $q.all(tasks);
        }

        function getCheckedEmails(emails) {
            var checkedEmails = [];
            emails.forEach(function (email) {
                if (email.checked) {
                    checkedEmails.push(email);
                }
            })
            return checkedEmails;
        }

        function createAclHandler(getId) {
            return {
                canEdit: true,
                acls: [],
                actions: aclServiceBuilder(getId, contactService)
            }
        }

        function submit(contact, acls, attachments, isNew) {
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
