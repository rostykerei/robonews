var app = angular.module('robonews-console');

app.controller('MainCtrl', function MainCtrl($scope) {
    this.userName = 'Rosty Kerei';
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

app.controller('ChannelCreateController', function ($scope, $http, $state) {

    $scope.cnForm = {};
    $scope.channel = {
        active: true
    };

    $scope.prefillDetailsForm = function() {
        $http({
            method : 'POST',
            url : 'rest/channel/new/prefill',
            data : $.param($scope.cnForm),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
        .success(function(data) {
            if (!data.error) {
                $scope.showPrefillError = false;
                $scope.channel = data.data;
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

    $scope.saveForm = function () {
        $('#channel-form .help-block').empty();
        $('#channel-form .form-group').removeClass('has-error');

        $http({
            method : 'POST',
            url : 'rest/channel/new/save',
            data : $scope.channel
        })
        .success(function(data) {
            if (!data.error) {
                alert('ok!');
            }
            else {
                for (var key in data.errors) {
                    if (data.errors.hasOwnProperty(key)) {
                        $('#f-' + key).addClass('has-error');
                        var msg = "<ul>";
                        for (var m in data.errors[key]) {
                            msg += "<li>" + data.errors[key][m] + "</li>";
                        }
                        msg += "</ul>";
                        $('#f-' + key + ' .help-block').html( msg );
                    }
                }
            }
        });
    };

    $scope.fbTypeAhead = function(val) {
        return $http.get('rest/channel/fb-typeahead', {
            params: {
                query: val
            }
        }).then(function(response){
            return response.data;
        });
    };

    $scope.twitterTypeAhead = function(val) {
        return $http.get('rest/channel/twitter-typeahead', {
            params: {
                query: val
            }
        }).then(function(response){
            return response.data;
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

