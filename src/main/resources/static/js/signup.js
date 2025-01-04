var app = angular.module('taskTrackerApp', []);

app.controller('SignUpController', function ($scope, $http) {
    $scope.account = {};

    $scope.submitForm = function () {
        $http.post('http://localhost:8080/api/accounts', $scope.account)
            .then(function (response) {
                alert('Account created successfully!');
                window.location.href = 'index.html'; // Redirect to login page
            })
            .catch(function (error) {
                alert('Error creating account: ' + error.data.message);
            });
    };
});
