(function () {
    'use strict';

    angular
        .module('crm.contact')
        .controller('contactAddController', contactAddController);

    /** @ngInject */
    function contactAddController(contactDetailsService, userService, contactAttachmentService) {
        var vm = this;

        vm.canEdit = true;
        vm.contact = {};

        vm.attachments = [];
        vm.title = 'Add contact';
        vm.submitText = 'Add';
        vm.submit = submit;
        vm.cancel = contactDetailsService.cancel;
        vm.attachmentService = contactAttachmentService;
        vm.details = contactDetailsService;
        vm.aclHandler = contactDetailsService.createAclHandler(function () {
            return vm.contact.id;
        });

        init();

        function init() {
            return initAcls().then(initOptions);
        }

        function initAcls() {
            return userService.getDefaultAcls().then(function (response) {
                vm.aclHandler.acls = response.data;
            });
        }

        function initOptions() {
            return vm.details.init().then(function (response) {
                vm.options = response;
            });
        }

        function submit() {
            contactDetailsService.submit(vm.contact, vm.aclHandler.acls, vm.attachments, true);
        }
    }
})();
