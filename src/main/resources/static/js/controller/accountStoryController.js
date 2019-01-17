var app = angular.module('ngApp', ['ngSanitize']);

app.controller('accountStoryController', accountStoryCtrl);

accountStoryCtrl.$inject = ['$scope', 'HomeService'];

function accountStoryCtrl($scope, HomeService) {

    $scope.commentText = '';
    $scope.listComment = [];
    $scope.totalCommentPages = 0;
    $scope.currentCommentPage = 1;
    $scope.pageComment = [];
    $scope.totalComment = 0;

    $scope.init = function () {
        $scope.getListStoryOnGoing(1);
    };

    $scope.getListStoryOnGoing = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/accountStory';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 1);
        HomeService.getData(url, data).then(function (response) {
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };

    $scope.getListStoryComplte = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/accountStory';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 3);
        HomeService.getData(url, data).then(function (response) {
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };
    $scope.getListStoryHidden = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/accountStory';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 0);
        HomeService.getData(url, data).then(function (response) {
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };

    $scope.getListStoryStop = function (pagenumber) {
        if (pagenumber === undefined) {
            pagenumber = 1;
        }
        var url = window.location.origin + '/api/accountStory';
        var data = new FormData();
        data.append('pagenumber', pagenumber);
        data.append("status", 2);
        HomeService.getData(url, data).then(function (response) {
        }, function errorCallback(errResponse) {
            swal({
                text: 'Bạn chưa đăng nhập',
                type: 'warning',
                confirmButtonText: 'Ok'
            }).then(function () {
                window.location.href = window.location.origin + '/tai-khoan/quan_ly_truyen';
            })
        })
    };
}