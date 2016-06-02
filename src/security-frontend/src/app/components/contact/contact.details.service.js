(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactDetailsService', contactDetailsService);
    /** @ngInject */
    function contactDetailsService(contactService, aclServiceBuilder, $state, contactAttachmentService, $q, $log, dialogService) {

        return {
            submit: submit,
            cancel: goToList,
            createAclHandler: createAclHandler,
            addEmail: addEmail,
            addAddress: addAddress,
            addTelephone: addTelephone,
            addMessengerAccount: addMessengerAccount,
            addSocialNetworkAccount: addSocialNetworkAccount,
            addWorkplace: addWorkplace,
            addAttachment: addAttachment,
            addSkill: addSkill,
            removeEmails: removeEmails,
            removeAddresses: removeAddresses,
            removeTelephones: removeTelephones,
            removeMessengerAccounts: removeMessengerAccounts,
            removeSocialNetworks: removeSocialNetworkAccounts,
            removeWorkplaces: removeWorkplaces,
            removeAttachments: removeAttachments,
            removeSkills: removeSkills,
            getEmptyContact: getEmptyContact,
            now: new Date(),
            getDictionary: getDictionary,
            parseProfile: parseProfile,
            isLinkedInUrl: isLinkedInUrl
        };

        function getDictionary() {
            return contactService.getDictionary().then(function (response) {
                return response.data;
            });
        }

        function addEmail(scope) {
            scope.contact.emails.push({});
        }

        function addAddress(scope) {
            scope.contact.addresses.push({});
        }

        function addTelephone(scope) {
            scope.contact.telephones.push({});
        }

        function addMessengerAccount(scope) {
            scope.contact.messengers.push({});
        }

        function addSocialNetworkAccount(scope) {
            scope.contact.socialNetworks.push({});
        }

        function addWorkplace(scope) {
            scope.contact.workplaces.push({});
        }

        function addAttachment(scope) {
            contactAttachmentService.addAttachment(scope);
        }

        function addSkill(scope) {
            scope.contact.skills.push({});
        }

        function removeEmails(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.emails, contactService.removeEmail);
        }

        function removeAddresses(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.addresses, contactService.removeAddress);
        }

        function removeTelephones(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.telephones, contactService.removeTelephone);
        }

        function removeMessengerAccounts(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.messengers, contactService.removeMessengerAccount);
        }

        function removeSocialNetworkAccounts(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.socialNetworks, contactService.removeSocialNetworkAccount);
        }

        function removeWorkplaces(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.workplaces, contactService.removeWorkplace);
        }

        function removeAttachments(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.attachments, contactService.removeAttachment);
        }

        function removeSkills(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.skills, contactService.removeSkill);
        }

        function getEmptyContact() {
            return {
                socialNetworks: [],
                addresses: [],
                telephones: [],
                emails: [],
                messengers: [],
                workplaces: [],
                skills: [],
                attachments: []
            };
        }

        function parseProfile(scope, profileUrl) {
            contactService.parseProfile(profileUrl).then(function (response) {
                $log.log(response.data);
                scope.contact.firstName = response.data.firstName;
                scope.contact.lastName = response.data.lastName;
                scope.contact.photoUrl = response.data.photoUrl;
                response.data.workplaces.forEach(function (workplace) {
                    scope.contact.workplaces.push(workplace);
                });
                response.data.addresses.forEach(function (address) {
                    scope.contact.addresses.push(address);
                });
            });
        }

        function isLinkedInUrl(url) {
            if (url) {
                return /.*linkedin.*/.test(url);
            }
        }

        function removeCheckedElementsFromList(contact, elements, removingFunction) {
            var tasks = [];
            var elementsForRemoving = getCheckedElements(elements);
            if (elementsForRemoving.length > 0) {
                dialogService.confirm('Do you really want to delete these fields?')
                    .result.then(function () {
                    elementsForRemoving.forEach(function (element) {
                        if (element.id) {
                            tasks.push(removeElementHandlingError(contact, element, elements, removingFunction));
                        } else {
                            removeElementFromArray(element, elements);
                        }
                    });
                });
            }
            return $q.all(tasks);

        }

        function removeElementHandlingError(contact, element, elements, removingFunction) {
            removingFunction(contact.id, element.id)
                .then(function () {
                    removeElementFromArray(element, elements);
                });
        }

        function removeElementFromArray(element, elements) {
            var index = elements.indexOf(element);
            elements.splice(index, 1);
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
            };
        }

        function submit(contact, acls, isNew) {
            if (isNew) {
                contactService.create(contact).then(function (response) {
                    var id = response.data;
                    updateAcls(id, acls).then(goToList);
                });
            } else {
                contactService.update(contact).then(function () {
                    updateAcls(contact.id, acls).then(goToList);
                });
            }
        }

        function updateAcls(contactId, acls) {
            return contactService.updateAcls(contactId, acls);
        }

        function goToList() {
            $state.go('contacts.list');
        }
    }
})();
