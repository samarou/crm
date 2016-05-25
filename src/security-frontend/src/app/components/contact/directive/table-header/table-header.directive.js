/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactTableHeader', crmContactTableHeader);

    /** @ngInject */
    function crmContactTableHeader() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/table-header/table-header.html',
            scope: {
                addingFunction: '=',
                removingFunction: '=',
                vm: '=',
                title: '@'
            }
        };
    }
})();
