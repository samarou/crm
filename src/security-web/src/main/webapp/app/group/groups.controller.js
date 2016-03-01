/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("GroupsController", ["$uibModal", "$q", "GroupService", "DialogService",
    function ($uibModal, $q, GroupService, DialogService) {
        "use strict";

        var vm = this;

        vm.sortProperties = {
            name: {name: "name", asc: true, enabled: true},
            description: {name: "description", asc: true, enabled: false}
        };
        vm.searchText = "";
        vm.pageGroups = [];

        //todo: resolve problem with filtering
        vm.pagingFilterConfig = {
            currentPage: 1,
            itemsPerPage: 10,
            visiblePages: 5,
            totalItems: null,
            filterObject: {
                name: "",
                description: ""
            },
            sortProperty: vm.sortProperties.name.name,
            sortAsc: vm.sortProperties.name.asc
        };

        function fetchAllGroups() {
            GroupService.fetchAll().then(function (response) {
                vm.groupList = response.data;
                vm.pagingFilterConfig.totalItems = vm.groupList.length;
            });
        }

        fetchAllGroups();

        vm.sortBy = function (property) {
            angular.forEach(vm.sortProperties, function (sortProperty) {
                sortProperty.enabled = false;
            });
            property.enabled = true;
            property.asc = !property.asc;
            vm.pagingFilterConfig.sortProperty = property.name;
            vm.pagingFilterConfig.sortAsc = property.asc;
        };

        vm.selectAll = function (checked) {
            if (checked) {
                vm.pageGroups.forEach(function (group) {
                    group.checked = true;
                });
            } else {
                vm.groupList.forEach(function (group) {
                    group.checked = false;
                });
            }
        };

        vm.selectOne = function () {
            vm.isSelectedAll = vm.pageGroups.every(function (group) {
                return group.checked;
            });
        };

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

        vm.remove = function () {
            var tasks = [];
            vm.pageGroups.forEach(function (group) {
                if (group.checked) {
                    tasks.push(GroupService.remove(group.id))
                }
            });
            $q.all(tasks).then(fetchAllGroups);
            vm.isSelectedAll = false;
        };

        function update(group) {
            if (group.id) {
                var originGroup = vm.groupList.find(function (g) {
                    return g.id === group.id
                });
                angular.copy(group, originGroup);
                GroupService.update(group);
            } else {
                GroupService.create(group).then(function (response) {
                    group.id = response.data;
                    vm.groupList.push(group);
                    vm.pagingFilterConfig.totalItems = vm.groupList.length;
                });
            }
        }

        function showDialog(model) {
            DialogService
                .custom('app/group/group.modal.view.html', model)
                .result.then(function (model) {
                    update(model.group);
                });
        }
    }
]);