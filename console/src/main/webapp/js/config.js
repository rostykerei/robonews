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
        .state('categories', {
            abstract: true,
            url: "/categories",
            templateUrl: "views/common/content.html"
        })
        .state('categories.geo', {
            abstract: true,
            url: "/geo",
            templateUrl: "views/common/content.html"
        })
        .state('categories.geo.list', {
            url: "/list",
            controller: "CategoryGeoController",
            templateUrl: "views/categories/geo/list.html",
            data: { pageTitle: 'Categories | Geo' }
        })
        .state('categories.geo.new', {
            url: "/new",
            templateUrl: "views/categories/geo/new.html",
            controller: "CategoryGeoNewController",
            data: { pageTitle: 'Categories | Geo | New' },
            resolve: {
                parents: function($http) {
                    return $http
                        .get('rest/geo_category/list')
                        .then(function success(response) { return response.data; });
                }
            }

        })
        .state('categories.news', {
            url: "/news",
            templateUrl: "views/categories/news.html",
            data: { pageTitle: 'News categories' }
        })
        .state('minor', {
            url: "/minor",
            templateUrl: "views/minor.html",
            data: { pageTitle: 'Example view' }
        })
}
angular
    .module('robonews-console')
    .config(config)
    .run(function($rootScope, $state) {
        $rootScope.$state = $state;
    });