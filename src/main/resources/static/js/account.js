angular.module('taskTrackerApp', [])
  .controller('AccountController', function($scope, $http, $window) {
    const accountId = localStorage.getItem('accountId');

    if (accountId) {
      // Fetch the tasks for the account
      $http.get('http://localhost:8080/api/accounts/' + accountId + '/tasks')
        .then(function(response) {
          console.log("Tasks fetched successfully:", response.data);
          $scope.account = response.data;
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

    // Function to handle the task completion change (checkbox clicked)
   $scope.updateTask = function(task) {
       let taskIdsToDelete = [];
       console.log('Task ID:', task.id);
       console.log('Task isCompleted:', task.isCompleted);

       // Check if the task is completed and add its ID to the list
       if (task.isCompleted) {
           taskIdsToDelete.push(task.id);  // Assuming task.id contains the real task ID
       }
       console.log('taskIdsToDelete:', taskIdsToDelete);

       // If there are task IDs to delete, send a DELETE request
       if (taskIdsToDelete.length > 0) {
           $http({
               method: 'DELETE',
               url: 'http://localhost:8080/api/accounts/' + accountId + '/tasks',
               data: taskIdsToDelete,  // Send the task IDs in the body
               headers: {
                   'Content-Type': 'application/json'  // Ensure the body is sent as JSON
               }
           })
           .then(function(response) {
               console.log("Task removed successfully:", response.data);
               // Optionally, refresh the task list or reload the page
               //alert(response.data.message);  // Show the success message from the backend
               $window.location.reload();  // Refresh to reflect the deletion
           })
           .catch(function(error) {
               console.error("Error removing task:", error);
               alert('Error removing task: ' + (error.data ? error.data.error : error.message));
           });
       }
   };

    // Add Task button functionality
    $scope.addTask = function() {
        $window.location.href = '/addTask.html';
    };

    // Logout button functionality
    $scope.logout = function() {
        localStorage.removeItem('accountId');
        $window.location.href = "/index.html";
    };
  });
