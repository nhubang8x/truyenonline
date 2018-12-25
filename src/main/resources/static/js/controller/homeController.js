var app = angular.module('ngApp', ['ui.tinymce', 'ngSanitize']);

app.controller('storyCtrl', storyCtrl);

storyCtrl.$inject = ['HomeService', '$scope', '$http'];

function storyCtrl(HomeService, $scope, $http) {

}