angular.module('taskTrackerApp', [])
    .controller('ResetPasswordController', function($scope, $http, $window, $location) {
        const queryParams = new URLSearchParams($location.absUrl().split('?')[1]);
        const token = queryParams.get('token');

        if (!token) {
            alert('Invalid or expired reset token.');
            $window.location.href = "/index.html";
            return;
        }

        $scope.resetPassword = function() {
            if ($scope.newPassword !== $scope.confirmPassword) {
                alert("Passwords do not match!");
                return;
            }

            $http.post('http://localhost:8080/api/accounts/reset-password', {
                resetToken: token,
                newPassword: $scope.newPassword
            }).then(function(response) {
                alert(response.data.message);
                $window.location.href = "/index.html";
            }).catch(function(error) {
                alert('Error: ' + (error.data ? error.data.error : error.message));
            });
        };
    });
