angular.module('taskTrackerApp', [])
  .controller('SignInController', function($scope, $http, $window) {
    $scope.submitForm = function() {
      const loginData = {
        email: $scope.credentials.email,
        password: $scope.credentials.password
      };

      // Debugging the data you're sending
      console.log("Login Data:", loginData);

      // Make sure data is in the correct format
      $http.post('http://localhost:8080/api/accounts/login', loginData)
        .then(function(response) {
          console.log("Login successful:", response.data);
          localStorage.setItem('accountId', response.data.accountId);
          console.log("account ID : ", response.data.accountId);
          // If successful, redirect to the account page or handle further logic
          $window.location.href = "/account.html";  // Assuming you have a separate account page
        })
        .catch(function(error) {
          console.error("Login failed:", error);
          alert('Login failed: ' + error.data.message);
        });
    };
  });
