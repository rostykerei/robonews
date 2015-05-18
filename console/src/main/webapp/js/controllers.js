var app = angular.module('robonews-console');

app.controller('MainCtrl', function MainCtrl($scope) {
    this.userName = 'Rosty Kerei';
    this.currentYear = new Date().getFullYear();
});

app.controller('ChannelsDatatable', function ($scope, $timeout, DTOptionsBuilder, DTColumnBuilder, $compile) {

    $scope.reloadData = function() {
        $scope.dtOptions.reloadData();
    }

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

app.controller('ChannelCreatePrefillController', function ($scope, $http, $state, ChannelService) {
    $scope.cnForm = {};

    ChannelService.form = {};

    $scope.prefillDetailsForm = function() {
        $scope.showPrefillError = false;

        $http({
            method : 'POST',
            url : 'rest/channel/new/prefill',
            data : $.param($scope.cnForm),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
        .success(function(data) {
            if (!data.error) {
                ChannelService.form = data.data;
                ChannelService.form.id = 0;
                ChannelService.form.active = true;

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

app.controller('ChannelEditController', function ($scope, $http, $state, ChannelService, countries) {

    if (!$scope.channel) {
        $scope.channel = ChannelService.form;
    }

    $scope.backup = angular.copy($scope.channel);

    $scope.showError = false;
    $('#channel-form .help-block').empty();
    $('#channel-form .form-group').removeClass('has-error');

    $scope.countries = countries;
    $scope.states = [];

    $scope.loadStates = function () {
        if ($scope.channel.country) {
            $http
                .get('rest/country/getStates/' + $scope.channel.country)
                .then(function(response){
                    $scope.states = response.data;
                });
        }
        else {
            $scope.states = [];
        }
    };

    if ($scope.channel.country) {
        $scope.loadStates();
    }


    $scope.saveForm = function () {
        $http({
            method : 'POST',
            url : 'rest/channel/save',
            data : $scope.channel
        })
            .success(function(data) {
                if (!data.error) {
                    if ($scope.channel.id > 0) {
                        $state.go('channel.list');
                    }
                    else {
                        ChannelService.form = {};
                        $state.go('channel.details.icon', { 'id': data.data });
                    }
                }
                else {
                    if (data.exceptionName) {
                        $scope.showError = true;
                        $scope.err = {
                            exceptionName: data.exceptionName,
                            exceptionMessage: data.exceptionMessage,
                            stackTrace: data.stackTrace
                        }
                    }

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

    $scope.reset = function() {
        $scope.channel = angular.copy($scope.backup);
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

    $scope.googlePlusTypeAhead = function(val) {
        return $http.get('rest/channel/google-plus-typeahead', {
            params: {
                query: val
            }
        }).then(function(response){
            return response.data;
        });
    };
});

app.controller('ChannelIconController', function ($scope, $state, $http, channelImageOptions) {
    $scope.imageOptions = channelImageOptions;

    $scope.submit = function() {
        var data = $('#crop').cropper('getDataURL');

        if (data) {
            var type = data.match(/^data:image\/(png|jpeg|jpg|gif);base64,/);

            $http({
                method : 'POST',
                url : 'rest/channel/image-update/' + $scope.channel.id,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data : $.param({
                    type : type[1],
                    data : data.replace(/^data:image\/(png|jpeg|jpg|gif);base64,/, "")
                })
            })
            .success(function() {
                $state.go('channel.list');
            });
        }
    };
});

app.controller('MasterdataAreaController', function ($scope, $http, $state, areas) {
    $scope.areas = areas;

    $scope.reloadData = function() {
        $state.go($state.current, {}, {reload: true});
    };

    $scope.rename = function(area) {
        var newName = prompt("Rename '" + area.name + "':", area.name);

        if (newName != null && newName != "" && newName != area.name) {
            $http({
                method : 'POST',
                url: 'rest/area/rename/' + area.id,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
                    newName: newName
                })
            })
            .success(function() {
                area.name = newName;
            });
        }
    };

    $scope.delete = function(area) {
        if (confirm("Are you sure to delete '" + area.name + "' area?")) {
            $http({
                method : 'DELETE',
                url : 'rest/area/delete/' + area.id
            })
            .success(function() {
                $state.go($state.current, {}, {reload: true});
            });
        }
    };
});

app.controller('MastedataAreaNewController', function ($scope, $state, $http, areas) {

    $scope.parents = areas;
    $scope.form = {
        parentId: 1,
        name: ""
    };

    $scope.addSpaces= function(area){
        var result = "";
        for(var i=0; i < area.level; i++){
            result += String.fromCharCode(160) +
                String.fromCharCode(160) + String.fromCharCode(160) + String.fromCharCode(160);
        }
        return result;
    };

    $scope.submit = function() {
        $scope.showErrors = false;

        $http({
            method : 'POST',
            url : 'rest/area/save',
            data : $.param($scope.form),
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
            .success(function(data) {
                if (!data.error) {
                    $state.go('masterdata.area.list');

                } else {
                    $scope.showErrors = true;
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

