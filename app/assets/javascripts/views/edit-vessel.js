angular.module('vesselsMng.views.editVessel',['ngRoute', 'uiGmapgoogle-maps', 'vesselsMng'])

  .config(['$routeProvider', function($routeProvider) {
      $routeProvider.when('/vessels/:vesselId/:friendlyUrl', {
          templateUrl: 'assets/partials/edit-vessel.html',
          controller: 'editVesselCtrl'
      });
    }
  ])

  .controller('editVesselCtrl', ['$scope', '$rootScope', '$location', '$routeParams', 'Vessels', 'Messages', function($scope, $rootScope, $location, $routeParams, Vessels, Messages) {
    $scope.initialVessel = {};

    Vessels.get({ id: $routeParams.vesselId }, function(vessel) {
      $scope.vessel = vessel;
      $scope.initialVessel = angular.copy(vessel);

      $scope.map.clickedMarker = {
        id: 0,
        latitude: $scope.vessel.lastLocation.latitude,
        longitude: $scope.vessel.lastLocation.longitude
      };
      $scope.map.center = {
        latitude: $scope.vessel.lastLocation.latitude,
        longitude: $scope.vessel.lastLocation.longitude
      };
    }, function () {
      Messages.add("danger", "Sorry, we couldn't get the vessel.");
      $location.path( "/" );
    });

    $scope.saveVessel = function() {
      if ($scope.form.$valid) {
        var updatedVessel = angular.copy($scope.vessel);
        Vessels.update(updatedVessel, function() {
          $rootScope.$broadcast('vessels:changed');
          Messages.add('success', updatedVessel.name + ' updated!');
        }, function() {
          Messages.add('danger', 'Sorry, something went wrong, please try again.');
        });
      }
    };

    $scope.resetForm = function(){
      $scope.vessel = angular.copy($scope.initialVessel);
      $scope.form.$submitted = false;
    };

    $scope.deleteVessel = function(){
      var vesselName = $scope.vessel.name;
      if (confirm('Are you sure to delete ' + vesselName + '?')) {
        Vessels.delete($scope.vessel, function() {
          $rootScope.$broadcast('vessels:changed');
          Messages.add('success', vesselName + ' deleted!');

          $location.path( "/" );
        }, function () {
          Messages.add('danger', 'Sorry, something went wrong, please try again.');
        });
      }
    };

    $scope.map = {
      center: { latitude: 53.349629099276434, longitude: -6.20303750038147 },
      zoom: 8,
      clickedMarker: {
        id: 0,
        options:{}
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

