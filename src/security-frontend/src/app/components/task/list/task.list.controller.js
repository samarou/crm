/**
 * Created by yauheni.putsykovich on 31.05.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskListController', TaskListController)
        .directive('taskChart', taskChart);

    /** @ngInject */
    function TaskListController(taskService, taskSecurityService, dialogService, collections, searchService, $state, $q, $scope) {
        var vm = this;

        vm.bundle = searchService.getTaskBundle();
        vm.statistics = searchService.getStatisticsBundle();
        vm.add = add;
        vm.edit = edit;
        vm.remove = remove;

        function add() {
            $state.go('tasks.add');
        }

        function edit(task) {
            taskSecurityService.checkEditPermission(task.id).then(function () {
                $state.go('tasks.edit', {id: task.id});
            });
        }

        function remove() {
            dialogService.confirm('Do you want to delete the selected task(s)?').result.then(function () {
                var checked = vm.bundle.itemsList.filter(collections.getChecked);
                taskSecurityService.checkDeletePermissionForList(checked).then(function () {
                    $q.all(checked.map(collections.getId).map(taskService.remove)).then(vm.bundle.find);
                });
            });
        }

        (function () {
            vm.bundle.find();
            vm.statistics.find();
        })();
    }

    /** @ngInject */
    function taskChart() {

        function getUniqueStatuses(statuses) {
            var uniqueStatusArray = [];
            for (var status in statuses){
                if (uniqueStatusArray.length == 0){
                    uniqueStatusArray.push(statuses[status])
                } else if (uniqueStatusArray.indexOf(statuses[status])==-1){
                    uniqueStatusArray.push(statuses[status]);
                }
            }
            return uniqueStatusArray;
        }

        function getAllStatuses(tasks) {
            var statusArray = [];
            for (var task in tasks){
                statusArray.push(tasks[task].status.name);
            }
            return statusArray;
        }

        function selectCounterStatuses(tasks) {

            var allStatuses = getAllStatuses(tasks);
            var uniqueStatuses = getUniqueStatuses(allStatuses);
            var counterArray = [];

            for (var unqStatus in uniqueStatuses){
                var counter = 0;
                for (var status in allStatuses){
                    if (uniqueStatuses[unqStatus] == allStatuses[status]){
                        counter++;
                    }
                }
                counterArray.push(counter);
            }
            return counterArray;
        }

        return {
            restrict: 'E',
            template: '<div></div>',
            scope: {
                tasks: '='
            },
            link: function (scope, element) {
                var chartOptions = {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Tasks statistics'
                    },
                    xAxis: {
                        categories: []
                    },

                    series: [{
                        name: 'Tasks',
                        data: []
                    }],

                    yAxis: {
                        min: 0,
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        }
                    },
                    legend: {
                        align: 'right',
                        x: -30,
                        verticalAlign: 'top',
                        y: 25,
                        floating: true,
                        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
                        borderColor: '#CCC',
                        borderWidth: 1,
                        shadow: false
                    },
                    tooltip: {
                        headerFormat: '<b>{point.x}</b><br/>',
                        pointFormat: '{series.name}: {point.y}<br/>'
                    },
                    plotOptions: {
                        column: {
                            stacking: 'normal',
                            enableMouseTracking: false
                        }
                    }
                };
                scope.$watch('tasks', function(tasks) {
                    if(tasks) {
                        chartOptions.xAxis.categories = getUniqueStatuses(getAllStatuses(tasks));
                        chartOptions.series[0].data = selectCounterStatuses(tasks);
                        Highcharts.chart(element[0], chartOptions);
                    }
                });


            }
        };
    }
})();
