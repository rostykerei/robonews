/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2014 Webapplayers.com
 *
 */


/**
 * MainCtrl - controller
 */
function MainCtrl($scope) {

    this.userName = 'Rosty Kerei';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';
    this.currentYear = new Date().getFullYear();
};

function ChannelsDatatable($scope, $timeout, DTOptionsBuilder, DTColumnBuilder) {
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            dataSrc: 'data',
            url: 'rest/channel/list'
        })
        .withOption('serverSide', true)
        .withOption('processing', true)
        .withPaginationType('full_numbers');

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('url').withTitle('URL'),
        DTColumnBuilder.newColumn('name').withTitle('Name'),
        DTColumnBuilder.newColumn('id').withTitle('ID').notVisible()
    ];
}

angular
    .module('inspinia')
    .controller('MainCtrl ', MainCtrl)
    .controller('ChannelsDatatable ', ChannelsDatatable)

