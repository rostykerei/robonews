/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2014 Webapplayers.com
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written stat for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/home");

    $stateProvider
        .state('home', {
            url: "/home",
            templateUrl: "views/home.html"
        })
        .state('channel', {
            abstract: true,
            url: "/channel",
            templateUrl: "views/common/content.html"
        })
        .state('channel.list', {
            url: "/list",
            templateUrl: "views/channel/list.html",
            data: { pageTitle: 'Channels' }
        })
        .state('channel.new', {
            abstract: true,
            url: "/new",
            templateUrl: "views/channel/new.html",
            data: { pageTitle: 'Channel | New' }
        })
        .state('channel.new.cn', {
            url: "",
            templateUrl: "views/channel/new.cn.html"
        })
        .state('channel.new.details', {
            url: "/details",
            controller: "ChannelEditController",
            templateUrl: "views/channel/edit.html",
            resolve: {
                countries:  function($http) {
                    return $http
                        .get('rest/country/getAll')
                        .then(
                        function success(response) { return response.data; },
                        function error() { return []; }
                    );
                }
            }
        })
        .state('channel.details', {
            abstract: true,
            url: "/{id:[0-9]+}",
            controller: 'ChannelDetails',
            resolve: {
                channel: function(ChannelService, $stateParams, $q) {
                    var deferred = $q.defer();

                    ChannelService.resource.get({id: $stateParams.id},
                        function(successData) { deferred.resolve(successData); },
                        function() { deferred.reject();});

                    return deferred.promise;
                }
            },
            templateUrl: "views/channel/details.html"
        })
        .state('channel.details.edit', {
            url: "/edit",
            templateUrl: "views/channel/edit.html",
            controller: 'ChannelEditController',
            data: { pageTitle: 'Channel | Edit', subpageTitle: 'Edit' },
            resolve: {
                countries:  function($http) {
                    return $http
                        .get('rest/country/getAll')
                        .then(
                        function success(response) { return response.data; },
                        function error(reason) { return []; }
                    );
                }
            }
        })
        .state('channel.details.icon', {
            url: "/icon",
            templateUrl: "views/channel/icon.html",
            data: { pageTitle: 'Channel | Icon', subpageTitle: 'Icon' },
            controller: 'ChannelIconController',
            resolve: {
                channelImageOptions: function($http, $stateParams) {
                    return $http
                        .get('rest/channel/image-options/' + $stateParams.id)
                        .then(
                            function success(response) { return response.data; },
                            function error() { return false; }
                        );
                }
            }
        })
        .state('channel.details.feeds', {
            url: "/feeds",
            templateUrl: "views/channel/feeds.html",
            data: { pageTitle: 'Channel | Feeds', subpageTitle: 'Feeds' }
        })
        .state('masterdata', {
            abstract: true,
            url: "/masterdata",
            templateUrl: "views/common/content.html"
        })
        .state('masterdata.area', {
            abstract: true,
            url: "/area",
            templateUrl: "views/common/content.html",
            resolve: {
                areas: function($http) {
                    return $http
                        .get('rest/area/list')
                        .then(function success(response) { return response.data; });
                }
            }
        })
        .state('masterdata.area.list', {
            url: "/list",
            controller: "MasterdataAreaController",
            templateUrl: "views/masterdata/area/list.html",
            data: { pageTitle: 'Master data | Area' }
        })
        .state('masterdata.area.new', {
            url: "/new",
            templateUrl: "views/masterdata/area/new.html",
            controller: "MastedataAreaNewController",
            data: { pageTitle: 'Master data | Area | New' }
        })
        .state('masterdata.topic', {
            abstract: true,
            url: "/topic",
            templateUrl: "views/common/content.html",
            resolve: {
                topics: function($http) {
                    return $http
                        .get('rest/topic/list')
                        .then(function success(response) { return response.data; });
                }
            }
        })
        .state('masterdata.topic.list', {
            url: "/list",
            controller: "MasterdataTopicController",
            templateUrl: "views/masterdata/topic/list.html",
            data: { pageTitle: 'Master data | Topics' }
        })
        .state('masterdata.topic.new', {
            url: "/new",
            templateUrl: "views/masterdata/topic/new.html",
            controller: "MastedataTopicNewController",
            data: { pageTitle: 'Master data | Topic | New' }
        })
        .state('feed', {
            abstract: true,
            url: "/feed",
            templateUrl: "views/common/content.html"
        })
        .state('feed.list', {
            url: "/list",
            templateUrl: "views/feed/list.html",
            data: { pageTitle: 'Feeds' }
        })
        .state('feed.new', {
            abstract: true,
            url: "/new",
            templateUrl: "views/feed/new.html",
            data: { pageTitle: 'Feed | New' }
        })
        .state('feed.new.url', {
            url: "",
            templateUrl: "views/feed/new.url.html"
        })
        .state('feed.new.details', {
            url: "/details",
            controller: "FeedEditController",
            templateUrl: "views/feed/edit.html",
            resolve: {
                topics: function($http) {
                    return $http
                        .get('rest/topic/list')
                        .then(function success(response) { return response.data; });
                },
                areas: function($http) {
                    return $http
                        .get('rest/area/list')
                        .then(function success(response) { return response.data; });
                }
            }
        })
}
angular
    .module('robonews-console')
    .config(config)
    .run(function($rootScope, $state) {
        $rootScope.$state = $state;
    });