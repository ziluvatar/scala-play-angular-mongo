var dependencies = [
  'ngRoute',
  'ngResource',
  'uiGmapgoogle-maps',
  'vesselsMng.views.vesselList',
  'vesselsMng.views.editVessel',
  'vesselsMng.views.newVessel'
];

var app = angular.module('vesselsMng', dependencies);

app.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({
      redirectTo: '/new-vessel'
  });
}]).config(function(uiGmapGoogleMapApiProvider) {
  uiGmapGoogleMapApiProvider.configure({
    //    key: 'your api key',
    v: '3.17',
    libraries: 'weather,geometry,visualization'
  });
});

app.controller('mainCtrl', ['$scope', 'Messages', function($scope, Messages) {
  $scope.messages = Messages.list;
  $scope.closeMessage = function(index) {
    Messages.remove(index);
  };

}]);

app.factory('Messages', function() {
  var messages = [];

  return {
    list: messages,
    add: function(type, text) {
      messages.push({
        type: type,
        text: text
      });
    },
    remove: function(index) {
      messages.splice(index, 1);
    }
  };
});

app.factory('Vessels', ['$resource', function($resource) {
  return $resource('vessels/:id', { id: '@id'}, {
    update: { method:'PUT' }
  });
}]);