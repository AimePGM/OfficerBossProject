angular.module('starter.controllers')
.controller('OrgCtrl', function($scope, $ionicModal, $timeout, $state,$http,$window) {
    $scope.goto=function(toState,params){ 
     $state.go(toState,params);
    }

    $scope.graph = {
      "nodes": [
        {"name": "d3"},
        {"name": "d3.svg"},
        {"name": "d3.svg.area"},
        {"name": "d3.svg.line"},
        {"name": "d3.scale"},
        {"name": "d3.scale.linear"},
        {"name": "d3.scale.ordinal"}
      ],
      "links": [
        {"source": 0, "target": 1},
        {"source": 1, "target": 2},
        {"source": 1, "target": 3},
        {"source": 0, "target": 4},
        {"source": 4, "target": 5},
        {"source": 4, "target": 6}
      ]
    };


})