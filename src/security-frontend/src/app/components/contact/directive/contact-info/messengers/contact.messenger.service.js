/**
 * Created by maksim.kalenik on 06.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactMessengerService', contactMessengerService);

    /** @ngInject */
    function contactMessengerService(contactService, dialogService, contactCommonService) {

        var detailsUrl = 'app/components/contact/directive/contact-info/messengers/contact.messenger.details.view.html';
        var propertiesToCheck = ['messenger', 'username'];
        var existenceErrorMessage = 'Messenger account already exists';
        var otherMessengerTypeId = 6;

        return {
            add: add,
            edit: edit,
            remove: remove,
            getTypeName: getTypeName
        };

        function add(scope) {
            openAddDialog(scope).then(function (model) {
                if (contactCommonService.infoItemCanBeAdded(model.account, scope.contact.messengers,
                        propertiesToCheck, existenceErrorMessage)){
                    removeUnusedDescription(model);
                    scope.contact.messengers.push(model.account);
                }
            });
        }

        function edit(account, scope) {
            openEditDialog(account, scope).then(function (model) {
                if (contactCommonService.infoItemCanBeAdded(model.account, scope.contact.messengers,
                        propertiesToCheck, existenceErrorMessage)) {
                    removeUnusedDescription(model);
                    angular.copy(model.account, account);
                }
            });
        }

        function removeUnusedDescription(model) {
            if (model.account.messenger != otherMessengerTypeId) {
                delete model.account.description;
            }
        }

        function remove(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.messengers, contactService.removeMessengerAccount);
        }

        function getTypeName(id, description, types) {
            var result = null;
            types.forEach(function (o) {
                if (o.id == id) {
                    result = o.name;
                }
            });
            if (id == otherMessengerTypeId) {
                result += ' (' + description + ')';
            }
            return result;
        }

        function openAddDialog(scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Add Messenger',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                account: {},
                messengers: scope.dictionary.messengers
            }).result;
        }

        function openEditDialog(account, scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Update Messenger',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                account: angular.copy(account),
                messengers: scope.dictionary.messengers
            }).result;
        }
    }
})();
