(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactDetailsService', contactDetailsService);
    /** @ngInject */
    function contactDetailsService(contactService, aclServiceBuilder, $state, contactAttachmentService,
                                   contactAddressService, contactEmailService, contactMessengerService,
                                   contactTelephoneService, $log) {

        return {
            submit: submit,
            cancel: goToList,
            attachment: contactAttachmentService,
            address: contactAddressService,
            email: contactEmailService,
            messenger: contactMessengerService,
            telephone: contactTelephoneService,
            createAclHandler: createAclHandler,
            addSocialNetworkAccount: addSocialNetworkAccount,
            addWorkplace: addWorkplace,
            addSkill: addSkill,
            removeSocialNetworks: removeSocialNetworkAccounts,
            removeWorkplaces: removeWorkplaces,
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

        function addSocialNetworkAccount(scope) {
            scope.contact.socialNetworks.push({});
        }

        function addWorkplace(scope) {
            scope.contact.workplaces.push({});
        }

        function addSkill(scope) {
            scope.contact.skills.push({});
        }

        function removeSocialNetworkAccounts(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.socialNetworks, contactService.removeSocialNetworkAccount);
        }

        function removeWorkplaces(scope) {
            return removeCheckedElementsFromList(scope.contact, scope.contact.workplaces, contactService.removeWorkplace);
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
            dialogService.confirm('Data will be merged. Do you agree?')
                .result.then(function () {
                contactService.parseProfile(profileUrl).then(function (response) {
                    $log.log(response.data);
                    merge(scope.contact, response.data);
                });
            });

        }

        function merge(oldContact, newContact) {
            angular.forEach(newContact, function (value, key) {
                if (value) {
                    if (angular.isArray(value)) {
                        if (oldContact[key]) {
                            oldContact[key] = oldContact[key].concat(value);
                        } else {
                            oldContact[key] = value;
                        }
                    } else if (!isBlank(value)) {
                        oldContact[key] = value;
                    }
                }
            });
            return oldContact;
        }

        function isBlank(str) {
            return (!str || /^\s*$/.test(str));
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
