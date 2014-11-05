var app = angular.module('robonews-console');

app.service("ChannelService", function( $http, $resource ) {
        return({
            resource: $resource('rest/channel/:id'),
            createForm: $createForm
        });

        var $createForm = {};
    }
);

app.factory('RetryInterceptor', function($injector, $timeout, $q) {
    return {
        'responseError': function(rejection) {
            // Avoid circular dependency issues
            var Retry = $injector.get('Retry');
            var $http = $injector.get('$http');

            if (rejection.config.url == 'views/common/http_error_window.html') {
                // avoid cirular retries
                return $q.reject(rejection);
            }

            // Timeout is just to keep UI from changing too quickly
            return $timeout(angular.noop, 100).then(function() {
                return Retry.show(rejection);
            }).then(function() {
                    return $http(rejection.config);
                }, function() {
                    return $q.reject(rejection);
                });
        }
    }
});

app.service('Retry', function Retry($window, $modal) {
    this.show  = function(rejection) {
        return $modal.open({
            templateUrl: 'views/common/http_error_window.html',
            controller: 'RetryController',
            resolve: {
                rejection: function() {
                    return rejection;
                }
            }
        }).result;
    }
});

