var app = angular.module('taskTrackerApp', []);

app.controller('SignInController', function ($scope, $http) {
    $scope.credentials = {};

    $scope.submitForm = function () {
        // Simulating sign-in process for now
        alert('Sign in successful!');
        window.location.href = 'signup.html';
    };
});
