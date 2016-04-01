/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("GroupBundle", ["GroupService", function (GroupService) {
    "use strict";

    var bundle = {};

    bundle.searchText = "";
    bundle.pageGroups = [];

    //todo: resolve problem with filtering
    bundle.pagingFilterConfig = {
        currentPage: 1,
        itemsPerPage: 10,
        visiblePages: 5,
        totalCount: null,
        filterObject: {
            name: "",
            description: ""
        },
        sortProperty: "name",
        sortAsc: true
    };

    bundle.performSearch = GroupService.getPublicGroups;
    bundle.find = function () {
        bundle.performSearch().then(function (response) {
            bundle.groupList = response.data;
            bundle.pagingFilterConfig.totalCount = bundle.groupList.length;
        });
    };

    bundle.selectAll = function (checked) {
        if (checked) {
            bundle.pageGroups.forEach(function (group) {
                group.checked = true;
            });
        } else {
            bundle.groupList.forEach(function (group) {
                group.checked = false;
            });
        }
    };

    bundle.selectOne = function () {
        bundle.isSelectedAll = bundle.pageGroups.every(function (group) {
            return group.checked;
        });
    };

    return {
        publicMode: function () {
            bundle.performSearch = GroupService.getPublicGroups;
            return bundle;
        },
        securedMode: function () {
            bundle.performSearch = GroupService.getAll;
            return bundle;
        }
    };
}]);