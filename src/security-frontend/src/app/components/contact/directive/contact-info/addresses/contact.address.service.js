/**
 * Created by maksim.kalenik on 06.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactAddressService', contactAddressService);

    /** @ngInject */
    function contactAddressService(contactService, dialogService, contactCommonService) {

        var detailsUrl = 'app/components/contact/directive/contact-info/addresses/contact.address.details.view.html';

        return {
            add: addAddress,
            edit: editAddress,
            remove: removeAddresses
        };

        function addAddress(scope) {
            openAddAttachmentDialog(scope).then(function (model) {
                scope.contact.addresses.push(model.address);
            });
        }

        function editAddress(address, scope) {
            openEditAttachmentDialog(address, scope).then(function (model) {
                angular.copy(model.address, address);
            });
        }

        function removeAddresses(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.addresses, contactService.removeAddress);
        }

        function openAddAttachmentDialog(scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Add Address',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                countries: scope.dictionary.countries
            }).result;
        }

        function openEditAttachmentDialog(address, scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Update Address',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                address: angular.copy(address),
                countries: scope.dictionary.countries
            }).result;
        }
    }
})();
