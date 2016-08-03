/**
 * Created by maksim.kalenik on 06.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactCommonService', contactCommonService);
    /** @ngInject */
    function contactCommonService($q, dialogService, collections, toastr) {

        return {
            remove: removeCheckedElementsFromList,
            removeOne: removeOneElementFromArray,
            infoItemCanBeAdded: infoItemCanBeAdded
        };

        function removeCheckedElementsFromList(contact, elements, removingFunction) {
            var tasks = [];
            var elementsForRemoving = getCheckedElements(elements);
            if (elementsForRemoving.length > 0) {
                dialogService.confirm('Do you really want to delete these fields?')
                    .result.then(function () {
                    elementsForRemoving.forEach(function (element) {
                        tasks.push(removeOneElementFromArray(contact, element, elements, removingFunction));
                    });
                });
            }
            return $q.all(tasks);
        }

        function removeOneElementFromArray(contact, element, elements, removingFunction) {
            if (element.id) {
                removeElementHandlingError(contact, element, elements, removingFunction);
            } else {
                removeElementFromArray(element, elements);
            }
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

        function infoItemCanBeAdded(element, elements, propertyList, toastrMessage) {
            if (collections.exists(element, elements, collections.propertyListComparator(propertyList))) {
                toastr.error(toastrMessage, 'Error');
                return false;
            }
            return true;
        }
    }
})();
