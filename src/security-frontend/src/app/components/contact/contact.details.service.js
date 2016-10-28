(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactDetailsService', contactDetailsService);
    /** @ngInject */
    function contactDetailsService(contactService, aclServiceBuilder, $state, contactAttachmentService,
                                   contactAddressService, contactEmailService, contactMessengerService,
                                   contactTelephoneService, contactSocialNetworkService, contactWorkplaceService,
                                   contactSkillService, dialogService, contactEducationService, $log) {

        return {
            submit: submit,
            cancel: goToList,
            attachment: contactAttachmentService,
            address: contactAddressService,
            email: contactEmailService,
            messenger: contactMessengerService,
            telephone: contactTelephoneService,
            socialNetwork: contactSocialNetworkService,
            workplace: contactWorkplaceService,
            skill: contactSkillService,
            education: contactEducationService,
            createAclHandler: createAclHandler,
            getEmptyContact: getEmptyContact,
            now: new Date(),
            checkAll: checkAll,
            getDictionary: getDictionary,
            getNationalities: getNationalities,
            parseProfile: parseProfile,
            isLinkedInUrl: isLinkedInUrl,
            isAllSelected: isAllSelected
        };

        function getDictionary() {
            return contactService.getDictionary().then(function (response) {
                return response.data;
            });
        }

        function getNationalities() {
            return contactService.getNationalityCategories().then(function (response) {
                return response.data;
            });
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
                educations: [],
                attachments: []
            };
        }

        function checkAll(array, checked) {
            if (checked) {
                array.forEach(function (element) {
                    element.checked = true;
                });
            } else {
                array.forEach(function (element) {
                    element.checked = false;
                });
            }
        }

        function isAllSelected(array) {
            array.isSelectedAll = array.every(function (element) {
                return element.checked;
            });
        }

        function parseProfile(scope, profileUrl) {
            dialogService.confirm('Data will be merged. Do you agree?')
                .result.then(function () {
                contactService.parseProfile(profileUrl).then(function (response) {
                    merge(scope.contact, response.data);
                });
            });

        }

        function merge(oldContact, newContact) {
            angular.forEach(newContact, function (value, key) {
                if (value) {
                    if (angular.isArray(value)) {
                        if (oldContact[key]) {
                            angular.forEach(value, function(object){
                                if (!arrayHasObject(oldContact[key], object)){
                                    oldContact[key] = oldContact[key].concat(object);
                                }
                            });

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

        function arrayHasObject(array, object){
            for(var i = 0; i < array.length; i++){
                if (equals(array[i], object)){
                    return true;
                }
            }
            return false;
        }

        function equals(obj1, obj2){
            for(var key in obj1){
                if (key === "id" || key === "$$hashKey"){continue;}
                if(obj1[key] !== obj2[key]){
                    return false; }
            }
            return true;
        }

        function isBlank(str) {
            return (!str || /^\s*$/.test(str));
        }

        function isLinkedInUrl(url) {
            if (url) {
                return new RegExp("/.*linkedin.*/").test(url);
            }
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
