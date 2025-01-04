angular.module('taskTrackerApp', [])
  .controller('AccountController', function($scope, $http, $window) {

    const accountId = localStorage.getItem('accountId');

    if (accountId) {
      $http.get('http://localhost:8080/api/accounts/' + accountId + '/tasks')
        .then(function(response) {
          console.log("Account fetched successfully:", response.data);

          // Extract the task list from the account object
          $scope.tasks = response.data.taskList || [];  // Use an empty array if taskList is undefined
        })
        .catch(function(error) {
          console.error("Failed to fetch account with tasks:", error);
          alert('Failed to load tasks.');
        });
    } else {
      alert('Account ID not found. Please log in again.');
      $window.location.href = "/index.html";  // Redirect to login page
    }

  });
