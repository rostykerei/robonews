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
            templateUrl: "views/channel/new.details.html"
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
                        function(errorData) { deferred.reject();});

                    return deferred.promise;
                }
            },
            templateUrl: "views/channel/details.html"
        })
        .state('channel.details.edit', {
            url: "/edit",
            templateUrl: "views/channel/edit.html",
            data: { pageTitle: 'Channel | Edit', subpageTitle: 'Edit' }
        })
        .state('channel.details.icon', {
            url: "/icon",
            templateUrl: "views/channel/icon.html",
            data: { pageTitle: 'Channel | Icon', subpageTitle: 'Icon' }
        })
        .state('channel.details.feeds', {
            url: "/feeds",
            templateUrl: "views/channel/feeds.html",
            data: { pageTitle: 'Channel | Feeds', subpageTitle: 'Feeds' }
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