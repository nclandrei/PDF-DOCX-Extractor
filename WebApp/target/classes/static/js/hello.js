angular.module('hello', []).controller('home', function($scope, $http) {
	$http.get('/response').success(function(data) {
		$scope.greeting = data;
	})
});
