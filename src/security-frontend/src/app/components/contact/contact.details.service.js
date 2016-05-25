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
            createAclHandler: createAclHandler,
            addEmail: addEmail,
            addAddress: addAddress,
            addTelephone: addTelephone,
            addMessengerAccount: addMessengerAccount,
            addSocialNetworkAccount: addSocialNetworkAccount,
            addWorkplace: addWorkplace,
            removeEmails: removeEmails,
            removeAddresses: removeAddresses,
            removeTelephones: removeTelephones,
            removeMessengerAccounts: removeMessengerAccounts,
            removeSocialNetworks: removeSocialNetworkAccounts,
            removeWorkplaces: removeWorkplaces,
            getEmptyContact: getEmptyContact,
            now: new Date(),
            getDictionary: getDictionary
        };

        function getDictionary() {
            return contactService.getDictionary().then(function (response) {
                return response.data;
            })
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

        function getEmptyContact() {
            return {
                socialNetworks: [],
                addresses: [],
                telephones: [],
                emails: [],
                messengers: [],
                workplaces: [],
                attachments: []
            }
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
			};
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
			});
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
