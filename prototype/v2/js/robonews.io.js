var app = angular.module('robonews.io', ['ngMaterial', 'ngRoute']);

app.controller('TopToolbarCtrl', function($scope, $location, $routeParams) {

    var tabs = [
        { title: 'Top', path: '' },
        { title: 'U.S.', path: 'us' },
        { title: 'World', path: 'world' },
        { title: 'Money', path: 'money' },
        { title: 'Tech', path: 'tech' },
        { title: 'Entertainment', path: 'entertainment' },
        { title: 'Health', path: 'health' },
        { title: 'Sports', path: 'sports' }
    ];

    $scope.tabs = tabs;
    $scope.selectedTab;

    $scope.go = function(tab) {
        $location.url('/' + tab.path)
    };

    $scope.setTab = function() {
        if (typeof $routeParams.category === 'undefined') {
            $scope.selectedTab = 0;
            return;
        }

        for (var i = 1; i < tabs.length; i++) {
            if ($routeParams.category == tabs[i].path) {
                if ($scope.selectedTab != i) $scope.selectedTab = i;
                return;
            }
        }

        $location.url('/');
    };

    $scope.$on('$routeChangeSuccess', function() {
        $scope.setTab();

    });

    $scope.setTab();
});

app.controller('PageCtrl', function($scope, $routeParams, page) {
    var $p = $('#p');

    function genContent() {
        var height = ~~(Math.random() * 200) + 50;
        var id = ~~(Math.random() * 10000);
        return  '<img src="http://lorempixel.com/280/' + height + '/?' + id +'"/>';
    }

    for (var i = 0; i < 20; i++) {
        $p.append("<div class=\"card\"><md-card style='background-color: #" + Math.random().toString(16).slice(2, 8) + ";'>" + genContent() + "</md-card></div>")
    }

    $p.imagesLoaded( function(){
        $p.masonry({
            itemSelector : '.card'
        });

        $('#loader').hide();
        $p.addClass('showPage');

        showFn();

        setTimeout(function () {
            $p.css('opacity', '1');
            $p.removeClass('showPage');
        }, 350);

    });


    $scope.greeting = page.title;
    $scope.cards = page.cards;
});


app.factory("dataService", function($q, $timeout, $http){
    return {
        getListing: function(category){
            var deferred = $q.defer();

            $http.get('/data/' + category + '.json')
                .success(function(data, status, headers, config) {
                    deferred.resolve(data);
                })
                .error(function(data, status, headers, config) {
                    deferred.reject();
                });
            return deferred.promise;
        }
    }
});

app.config(['$routeProvider', '$locationProvider',
    function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        var routing = {
            controller: 'PageCtrl',
            template: "page.html",
            resolve: {
                page: function($route, dataService){

                    var $p = $('#p');
                    $p.html('');
                    $p.masonry('destroy');
                    $p.css('opacity', '0');
                    $('#loader').show();

                    var category = $route.current.params.category || 'top',
                        storyId = $route.current.params.story;

                    return dataService.getListing(category);
                }
            }
        };

        $routeProvider.
            when('/', routing).
            when('/:category',routing).
            otherwise({redirectTo: '/'})
        ;
    }]);
