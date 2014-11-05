(function () {
    var app = angular.module('robonews-console', [
        'ui.router',
        'ui.bootstrap',
        'datatables',
        'ngResource'
    ]);

    app.config(function($httpProvider) {
        $httpProvider.interceptors.push('RetryInterceptor');
    });

})();