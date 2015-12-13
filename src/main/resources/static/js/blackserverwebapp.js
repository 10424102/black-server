var blackserverweb = angular.module('blackserverweb', ['ui.bootstrap', 'ngRoute'])
 .config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/home', {
		templateUrl : '/partials/home',
		controller : 'home'
	}).when('/users', {
       		templateUrl : '/partials/users',
       		controller : 'users'
    }).otherwise('/users');

  })
 .controller('navigation', function($rootScope, $scope, $http, $location) {

 })
 .controller('users', function($rootScope, $scope, $http, $location) {
   var index = 0;
   function getAllUsers(){
     $http.get('/admin/users').success(function(data, status){
          // 注意所有http服务器的callback都是异步的，而为了浏览器能响应，异步操作不会block而是直接继续执行
          $scope.users = data;
          getNextAvatarData();
        });
   }
   function getNextAvatarData()
   {
     if($scope.users){
       if(index < $scope.users.length){
         var avatarUrl = '/api/image?q='+$scope.users[index].avatar;
         $http.get(avatarUrl).success(function(data, status){
           $scope.users[index].avatarData = data;
           index++;
           //console.log(index);
           getNextAvatarData();
         });
       }
     }
   }
   getAllUsers();
 })