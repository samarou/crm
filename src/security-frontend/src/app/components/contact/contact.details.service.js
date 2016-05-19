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
            addAddress: addAddress,
            removeAddresses: removeAddresses,
            now: new Date(),
            init: init
        };

        function init() {
            return $q.all(
                [
                    getEmailTypes(),
                    getTelephoneTypes(),
                    getCountries()
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

        function getCountries() {
            return contactService.getCountries().then(function (response) {
                vm.options.countries = response.data;
            })
        }

        function addEmail(scope) {
            scope.contact.emails.push({});
        }

        function removeEmails(scope) {
            var contact = scope.contact;
            return removeCheckedElementsFromList(contact, contact.emails, contactService.removeEmails);
        }

        function addAddress(scope) {
            scope.contact.addresses.push({});
        }

        function removeAddresses(scope) {
            var contact = scope.contact;
            return removeCheckedElementsFromList(contact, contact.addresses, contactService.removeAddress);
        }

        function removeCheckedElementsFromList(contact, elements, removingFunction) {
            var tasks = [];
            getCheckedElements(elements)
                .forEach(function (element) {
                    if (element.id) {
                        tasks.push(removingFunction(contact.id, element.id));
                    }
                    var index = elements.indexOf(element);
                    elements.splice(index, 1);
                });
            return $q.all(tasks);
        }

        function getCheckedElements(elements) {
            var checkedElements = [];
            elements.forEach(function (element) {
                if (element.checked) {
                    checkedElements.push(element);
                }
            });
            return checkedElements;
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
