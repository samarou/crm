/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("GroupsController", ["GroupService", "Collections",
    function (GroupService, Collections) {
        "use strict";
        var vm = this;
        vm.group = {};
        GroupService.fetchAll().then(function (response) {
            vm.groupList = response.data;
        });
        vm.setEditableGroup = function (group) {
            angular.copy(group, vm.group);
        };
        vm.update = function (group) {
            var originGroup = vm.groupList.find(function (g) { return g.id === group.id });
            angular.copy(group, originGroup);
            $("#modal-close-button").trigger("click");//todo: need improve(find other solution)
        }
    }]);