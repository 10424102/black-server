var blackserverweb = angular.module("blackserverweb", ["mgcrea.ngStrap", "ngRoute"])
  .config(function($routeProvider, $httpProvider) {

    $routeProvider.when("/home", {
      templateUrl: "/partials/home",
      controller: "home"
    }).when("/users", {
      templateUrl: "/partials/users",
      controller: "users"
    }).when("/activities", {
      templateUrl: "/partials/activities",
      controller: "activities"
    }).when("/chatroom", {
      templateUrl: "/partials/chatroom",
      controller: "chatroom"
    }).when("/login", {
      templateUrl: "/partials/login",
      controller: "login"
    }).otherwise("/");

  })
  .controller("navigation", function($rootScope, $scope, $http, $location) {

  })
  .controller("users", function($scope, $http, $location, $log, authService, userService) {
    function notifyUsersData(index, data) {
      $scope.users[index] = data;
    }
    if (authService.getXToken()) {
      $scope.users = [];
      userService.registerCallback(notifyUsersData);
      userService.requestAllUsers();
    } else {
      $location.path("/login");
    }
  })
  .controller("activities", function($rootScope, $scope, $http, $location) {
    if ($rootScope.xtoken) {
      var index = 0;

      function getAllActivities() {
        $http({
          url: "/admin/activities",
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "X-Token": $rootScope.xtoken
          }
        }).success(function(data, status) {
          $scope.activities = data;
          getNextCoverData();
        });
      }

      function getNextCoverData() {
        if ($scope.xtoken && $scope.activities) {
          if (index < $scope.activities.length) {
            var coverUrl = "/api/image?q=" + $scope.activities[index].cover.split("~")[0];
            $http({
              url: coverUrl,
              method: "GET",
              headers: {
                "Content-Type": "application/json",
                "X-Token": $rootScope.xtoken
              },
              responseType: "blob"
            }).success(function(data, status) {
              var fileReader = new FileReader();
              fileReader.readAsDataURL(data);
              fileReader.onload = function() {
                $scope.activities[index].coverData = fileReader.result;
                index++;
                getNextCoverData();
              }
            });
          }
        }
      }
      getAllActivities();
    } else {
      $location.path("/login");
    }
  })
  .controller("login", function($scope, $location, $log, authService) {
    $scope.userLogin = function() {
      var promise = authService.auth($scope.login);
      promise.success(function(data, status) {
        authService.setXToken(data.token);
        $log.log("login success");
      }).error(function(data, status) {
        $scope.alert = {
          "title": "message",
          "content": "something went wrong",
          "type": "danger"
        };
        $log.log($scope.alert);
      });
    }
  })
  .controller("chatroom", function($scope, $location, $log, authService) {
    if (authService.getXToken()) {
      $log.log(authService.getXToken());
    } else {
      $location.path("/login");
    }
  })
  .factory("authService", function($http, $log) {
    var xToken;
    var serviceInstance = {
      auth: function(login) {
        var authUrl = "/api/users/token?phone=" + login.phone + "&vcode=" + login.vcode;
        return $http.get(authUrl);
      },
      setXToken: function(token) {
        xToken = token;
      },
      getXToken: function() {
        return xToken;
      }
    };
    return serviceInstance;
  })
  .factory("imageService", function($http, $log, authService) {
    var serviceInstance = {
      requestImage: function(url, /*opetional index */ index, /* opetional cakllback function */ receiver) {
        index = index || undefined;
        receiver = receiver || undefined;
        $http({
          url: url,
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "X-Token": authService.getXToken()
          },
          responseType: "blob"
        }).success(function(data, status) {
          var fileReader = new FileReader();
          fileReader.readAsDataURL(data);
          fileReader.onload = function() {
            if (receiver === undefined) {
              return data;
            } else {
              if (index === undefined) {
                receiver.call(this, data);
              } else {
                receiver.call(this, index, data);
              }
            }
          }
        });
      }
    }
    return serviceInstance;
  })
  .factory("userService", function($http, $log, authService, imageService) {
    var users;
    var registerCallbackFunction;

    function setUserAvatarData(index, data) {
      $log.log("index" + index + " data" + data);
      users[index].avatarData = data;
      registerCallbackFunction(index, users[index]);
    }
    var serviceInstance = {
      requestAllUsers: function() {
        if (authService.getXToken()) {
          $http({
            url: "/admin/users",
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              "X-Token": authService.getXToken()
            }
          }).success(function(data, status) {
            // 注意所有http服务器的callback都是异步的，而为了浏览器能响应，异步操作不会block而是直接继续执行
            users = data;
            for (var index = 0; index < users.length; index++) {
              avatarUrl = "/api/image?q=" + users[index].avatar.split("~")[0];
              imageService.requestImage(avatarUrl, index, setUserAvatarData);
            }
          });
        }
      },
      registerCallback: function(receiver) {
        registerCallbackFunction = receiver;
      }
    }
    return serviceInstance;
  })
