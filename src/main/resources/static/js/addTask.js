angular.module('taskTrackerApp', [])
  .controller('AddTaskController', function($scope, $http, $window) {
    // Initialize an empty array for descriptions
    $scope.descriptions = [''];

    // Add a new description input field
    $scope.addDescription = function() {
      $scope.descriptions.push('');
    };

    // Remove a description input field
    $scope.removeDescription = function(index) {
      $scope.descriptions.splice(index, 1);
    };

    // Handle form submission (save tasks)
    $scope.submitForm = function() {
      const accountId = localStorage.getItem('accountId');
      if (!accountId) {
        alert('Please log in again.');
        $window.location.href = "/index.html";  // Redirect to login
        return;
      }

      // Clean up descriptions to remove any empty or whitespace-only descriptions
      const taskDescriptions = $scope.descriptions
        .map(function(description) { return description.trim(); })
        .filter(function(description) { return description.length > 0; });

      if (taskDescriptions.length === 0) {
        alert('Please enter at least one task description.');
        return;
      }

      // Send the data to the API
      $http.post('http://localhost:8080/api/accounts/' + accountId + '/tasks', taskDescriptions)
        .then(function(response) {
          console.log("Tasks added successfully:", response.data);
          $window.location.href = "/account.html";  // Redirect to account page
        })
        .catch(function(error) {
          console.error("Failed to add tasks:", error);
          alert('Failed to save tasks.');
        });
    };

    // Back to Account function
        $scope.goBack = function() {
          $window.location.href = "/account.html";  // Redirect to account page
        };
  });
