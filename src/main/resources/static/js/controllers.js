app.controller('SignUpController', function ($scope, $http) {
    $scope.user = {};

    $scope.registerUser = function () {
        $http.post('/api/account', $scope.user)
            .then(function (response) {
                alert('User registered successfully!');
                $scope.user = {}; // Reset form fields
            })
            .catch(function (error) {
                console.error('Error registering user:', error);
                alert('Failed to register user.');
            });
    };
});

app.controller('SignInController', function ($scope, $http) {
    $scope.signIn = {};

    $scope.signInUser = function () {
        $http.post('/api/authenticate', $scope.signIn)
            .then(function (response) {
                alert('Sign in successful!');
            })
            .catch(function (error) {
                console.error('Error signing in:', error);
                alert('Failed to sign in.');
            });
    };
});
