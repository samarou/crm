/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("GroupsController", ["GroupService", function (GroupService) {
    "use strict";
    var vm = this;
    GroupService.fetchAll().then(function (response) { vm.groupList = response.data; });
}]);