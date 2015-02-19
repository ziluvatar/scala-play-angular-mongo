angular.module('vesselsMng.views.vesselList', ['vesselsMng'])

  .controller('vesselListCtrl', ['$scope', '$rootScope', '$location', 'Vessels', 'Messages', function($scope, $rootScope, $location, Vessels, Messages) {
    var fetchVessels = function() {
      Vessels.query(function(vessels) {
        $scope.vessels = vessels;
      }, function() {
        Messages.add("danger", "Sorry, we couldn't load the list of vessels, try to refresh the page.");
      });
    };
    fetchVessels();

    $scope.isActive = function (viewLocation) {
      return viewLocation === $location.path();
    };

    $scope.friendlyUrl = function (name) {
      return name.toLowerCase().split(/[^\w]/).join("-");
    };

    var vesselsChangedListener = $rootScope.$on('vessels:changed', function() {
      fetchVessels();
    });

    $scope.$on('$destroy', vesselsChangedListener);
  }]);

