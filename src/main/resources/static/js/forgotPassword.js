angular.module('taskTrackerApp', [])
    .controller('ForgotPasswordController', function($scope, $http, $window) {
        $scope.forgotPassword = function() {
            $http.post('http://localhost:8080/api/accounts/forgot-password', { email: $scope.email })
                .then(function(response) {
                    alert(response.data.message);
                    $window.location.href = "/index.html";
                })
                .catch(function(error) {
                    alert('Error: ' + (error.data ? error.data.error : error.message));
                });
        };
    });
