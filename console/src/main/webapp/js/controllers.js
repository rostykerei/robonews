/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2014 Webapplayers.com
 *
 */


/**
 * MainCtrl - controller
 */
function MainCtrl() {

    this.userName = 'Rosty Kerei';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';
    this.currentYear = new Date().getFullYear();


    this.persons = [
        {
            id: '1',
            firstName: 'Monica',
            lastName: 'Smith'
        },
        {
            id: '2',
            firstName: 'Sandra',
            lastName: 'Jackson'
        },
        {
            id: '3',
            firstName: 'John',
            lastName: 'Underwood'
        },
        {
            id: '4',
            firstName: 'Chris',
            lastName: 'Johnatan'
        },
        {
            id: '5',
            firstName: 'Kim',
            lastName: 'Rosowski'
        }
    ];
};


angular
    .module('inspinia')
    .controller('MainCtrl ', MainCtrl)
