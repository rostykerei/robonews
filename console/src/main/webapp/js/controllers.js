var app = angular.module('robonews-console');

app.controller('MainCtrl', function MainCtrl($scope) {
    this.userName = 'Rosty Kerei';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';
    this.currentYear = new Date().getFullYear();
});

app.controller('ChannelsDatatable', function ($scope, $timeout, DTOptionsBuilder, DTColumnBuilder, $compile) {
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            dataSrc: 'data',
            url: 'rest/channel/list'
        })
        .withOption('serverSide', true)
        .withOption('processing', true)
        .withOption('columnDefs', [
            {
                "targets": 0,
                "data": null,
                "render": function ( data, type, row ) {
                    var label = row['active'] ? '' : ' <span class="label">Inactive</span>';
                    var stars = '';
                    for (var star = 1; star <= 3; star++) {
                        stars += '<i class="fa fa-star text-' + (row['scale'] >= star ? 'warning' : 'muted') + '"></i>';
                    }
                    return stars + label + ' <a ui-sref="channel.details.edit({id: ' + row['id'] +'})">' + row['title'] + '</a>';
                }
            },
            {
                "targets": 2,
                "data": null,
                "render": function ( data, type, row ) {
                    return '<a href="' + row['url'] + '" target="_blank"><i class="fa fa-external-link"></i> ' + row['url'] + '</a>';
                }
            }

        ])
        .withOption('fnRowCallback', function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $compile(nRow)($scope);
            }
        )
        .withPaginationType('full_numbers');

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('title').withTitle('Title'),
        DTColumnBuilder.newColumn('canonicalName').withTitle('Canonical name'),
        DTColumnBuilder.newColumn('url').withTitle('URL'),
        DTColumnBuilder.newColumn('feedsCount').withTitle('Feeds count')
    ];
});

app.controller('ChannelDetails', function ($scope, channel) {

    $scope.tabs = [
        { action : 'edit', label : 'Edit', icon: 'fa-edit' },
        { action : 'icon', label : 'Icon', icon: 'fa-picture-o' },
        { action : 'feeds', label : 'Feeds', icon: 'fa-rss' }
    ];

    $scope.channel = channel;
});

app.controller('ChannelCreateController', function ($scope, $http, ChannelService, $state) {

    $scope.cnForm = {};
    ChannelService.channelForm = {};

    $scope.prefillDetailsForm = function() {
        ChannelService.channelForm = {};

        $http({
            method : 'POST',
            url : 'rest/channel/new/prefill',
            data : $.param($scope.cnForm),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
        .success(function(data) {
            if (!data.error) {
                $scope.showPrefillError = false;
                ChannelService.channelForm = data.data;
                $scope.channel = ChannelService.channelForm;
                $state.go('channel.new.details');

            } else {
                $scope.showPrefillError = true;
                $scope.err = {
                    exceptionName: data.exceptionName,
                    exceptionMessage: data.exceptionMessage,
                    stackTrace: data.stackTrace
                }
            }
        });

    };

});


app.controller('RetryController', function($scope, $modalInstance, rejection) {

    $scope.rejection = rejection;

    $scope.retry = function() {
        // Will resolve the promise
        $modalInstance.close();
    };

    $scope.cancel = function() {
        // Will reject the promise
        $modalInstance.dismiss();
    }
});

