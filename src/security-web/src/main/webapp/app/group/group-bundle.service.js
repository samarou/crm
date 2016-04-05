/**
 * @author yauheni.putsykovich
 */
angular.module("app").service("GroupBundle", ["GroupService", function (GroupService) {
    "use strict";

    function createBundle(){
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
                name: ""
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

        return bundle;
    }

    return {
        publicMode: function () {
            var bundle = createBundle();
            bundle.performSearch = GroupService.getPublicGroups;
            return bundle;
        },
        securedMode: function () {
            var bundle = createBundle();
            bundle.performSearch = GroupService.getAll;
            return bundle;
        }
    };
}]);