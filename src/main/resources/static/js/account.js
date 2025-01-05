angular.module('taskTrackerApp', [])
  .controller('AccountController', function($scope, $http, $window) {
    const accountId = localStorage.getItem('accountId');
    if (accountId) {
      $http.get('http://localhost:8080/api/accounts/' + accountId + '/tasks')
        .then(function(response) {
          console.log("Tasks fetched successfully:", response.data);
          $scope.account = response.data;

          // Convert dateCreated strings to Date objects
          $scope.tasks = response.data.taskList.map(task => {
            task.dateCreated = new Date(task.dateCreated); // Convert to Date object
            return task;
          });
        })
        .catch(function(error) {
          console.error("Failed to fetch tasks:", error);
          alert('Failed to load tasks.');
        });
    } else {
      alert('Account ID not found. Please log in again.');
      $window.location.href = "/index.html";  // Redirect to login
    }


    $scope.logout = function() {
      localStorage.removeItem('accountId');
      $window.location.href = "/index.html";
    };
  });
