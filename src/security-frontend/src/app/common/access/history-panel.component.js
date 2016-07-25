/**
 * @author yauheni.putsykovich
 */

(function () {
    'use strict';

    angular
        .module('crm.acl')
        .component('historyPanel', changesHistoryPanel());

    /** @ngInject */
    function changesHistoryPanel() {
        return {
            templateUrl: 'app/common/access/history-panel.html',
            bindings: {
                info: '<'
            }
        };
    }
})();
