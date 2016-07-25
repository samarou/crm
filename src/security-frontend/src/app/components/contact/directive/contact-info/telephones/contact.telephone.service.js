/**
 * Created by maksim.kalenik on 06.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactTelephoneService', contactTelephoneService);

    /** @ngInject */
    function contactTelephoneService(contactService, dialogService, contactCommonService, collections) {

        var detailsUrl = 'app/components/contact/directive/contact-info/telephones/contact.telephone.details.view.html';

        return {
            add: add,
            edit: edit,
            remove: remove,
            getTypeName: getTypeName
        };

        function add(scope) {
            openAddDialog(scope).then(function (model) {
                if (!collections.exists(model.telephone, scope.contact.telephones,
                        collections.propertyListComparator(['number']))) {
                    scope.contact.telephones.push(model.telephone);
                }
            });
        }

        function edit(telephone, scope) {
            openEditDialog(telephone, scope).then(function (model) {
                if (!collections.exists(model.telephone, scope.contact.telephones, collections.propertyListComparator(['number']))) {
                    angular.copy(model.telephone, telephone);
                }
            });
        }

        function remove(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.telephones, contactService.removeTelephone);
        }

        function getTypeName(type, types) {
            var result = null;
            if (types) {
                types.forEach(function (o) {
                    if (o.name == type) {
                        result = o.value;
                    }
                });
            }
            return result;
        }

        function openAddDialog(scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Add Telephone',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                telephone: {},
                telephoneTypes: scope.dictionary.telephoneTypes
            }).result;
        }

        function openEditDialog(telephone, scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Update Telephone',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                telephone: angular.copy(telephone),
                telephoneTypes: scope.dictionary.telephoneTypes
            }).result;
        }
    }
})();
