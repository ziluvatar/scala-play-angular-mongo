angular.module('vesselsMng.views.newVessel',['ngRoute', 'uiGmapgoogle-maps', 'vesselsMng'])

  .config(['$routeProvider', function($routeProvider) {
      $routeProvider.when('/new-vessel', {
          templateUrl: 'assets/partials/new-vessel.html',
          controller: 'createVesselCtrl'
        });
    }
  ])

  .controller('createVesselCtrl', ['$scope', '$rootScope', '$location', 'Vessels', 'Messages', function($scope, $rootScope, $location, Vessels, Messages) {
    $scope.initialVessel = { "id": "" };
    $scope.vessel = angular.copy($scope.initialVessel);

    $scope.createVessel = function() {
      if ($scope.form.$valid) {
        var newVessel = angular.copy($scope.vessel);
        Vessels.save(newVessel, function() {
          $rootScope.$broadcast('vessels:changed');
          Messages.add('success', newVessel.name + ' created!');
          $location.path('/');
        }, function(error) {
          Messages.add('danger', 'Sorry, something went wrong, please try again.');
        });
      }
    };

    $scope.resetForm = function(){
      $scope.vessel = angular.copy($scope.initialVessel);
      $scope.form.$submitted = false;
    };

    $scope.map = {
      center: { latitude: 53.349629099276434, longitude: -6.20303750038147 },
      zoom: 8,
      clickedMarker: {
        id: 0,
        options:{
        }
      },
      events: {
        click: function (mapModel, eventName, originalEventArgs) {
          var e = originalEventArgs[0];
          var lat = e.latLng.lat(),
              lon = e.latLng.lng();
          $scope.map.clickedMarker = {
            id: 0,
            latitude: lat,
            longitude: lon
          };
          $scope.vessel.lastLocation = {
            longitude: lon,
            latitude: lat
          };
          $scope.$apply();
        }
      }
    };

  }]);