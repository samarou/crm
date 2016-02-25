/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("GroupsController", ['$scope', "$uibModal", "GroupService",
    function ($scope, $uibModal, GroupService) {
        "use strict";

        var vm = this;

        GroupService.fetchAll().then(function (response) {
            vm.groupList = response.data;
        });

        vm.edit = function (group) {
            showDialog({
                title: "Editing Group",
                okTitle: "Update",
                group: angular.copy(group)
            });
        };

        vm.create = function () {
            showDialog({
                title: "Create a New Group",
                okTitle: "Add",
                group: {}
            });
        };

        function update(group) {
            if (group.id) {
                var originGroup = vm.groupList.find(function (g) {
                    return g.id === group.id
                });
                angular.copy(group, originGroup);
                GroupService.update(group);
            } else {
                GroupService.create(group, function (response) {
                    group.id = response.data;
                    vm.groupList.push(group);
                });
            }
        }

        function showDialog(model) {
            var modalInstance = $uibModal.open({
                windowTemplateUrl: '/app/common/modal.dialog.template.html',
                templateUrl: '/app/group/group.modal.view.html',
                controller: 'ModalDialogController',
                resolve: {model: model}
            });
            modalInstance.result.then(function (model) {
                update(model.group);
            });
        }
    }
]);