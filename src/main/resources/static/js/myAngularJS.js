angular.module("ngApp", [])
    .controller("myVipChapter", ['$compile', '$http', '$scope', function ($compile, $http, $scope) {
        $scope.chapterVip = function (chID) {
            var data = new FormData();
            data.append('chID', chID);
            var url = window.location.origin + '/api/chapterVip';
            var config = {
                headers: {
                    'Content-Type': undefined
                },
                transformResponse: function (data, headers, status) {
                    var ret = {messageError: data, status: status};
                    return ret;
                }
            };
            $http.post(url, data, config).then(function successCallback(response) {
                swal({
                    text: 'Thanh toán thành công',
                    type: 'success',
                    confirmButtonText: 'Ok'
                }).then(
                    function () {
                        window.location.reload();
                    }
                );
            }, function errorCallback(errResponse) {
                callWarningSweetalert(errResponse.data.messageError);
            });
        }
    }]);
angular.module('ngApp', ['ui.tinymce', 'ngSanitize'])
    .controller('storyController', ['$http', '$scope', function ($http, $scope) {

        $scope.listChapter = [];
        $scope.totalPages = 0;
        $scope.currentPage = 1;
        $scope.sid = 0;
        $scope.page = [];
        $scope.commentText = '';
        $scope.listComment = [];
        $scope.totalCommentPages = 0;
        $scope.currentCommentPage = 1;
        $scope.pageComment = [];
        $scope.totalComment = 0;
        $scope.noImage = 'https://res.cloudinary.com/thang1988/image/upload/v1544258290/truyenmvc/noImages.png';

        //Lấy danh sách Chapter Theo sID, pagenumber, size
        $scope.getListChapter = function (pagenumber, size) {
            if (pagenumber === undefined) {
                pagenumber = 1;
            }
            if (size === undefined) {
                size = 1;
            }
            var data = new FormData();
            data.append('sID', $scope.sid);
            data.append('pagenumber', pagenumber);
            data.append('size', size);
            var url = window.location.origin + '/api/chapterOfStory';
            var config = {
                headers: {
                    'Content-Type': undefined
                }
            };
            $http.post(url, data, config).then(function (response) {
                $scope.listChapter = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.number + 1;
                var startPage = Math.max(1, $scope.currentPage - 2);
                var endPage = Math.min(startPage + 4, $scope.totalPages);
                var pages = [];
                for (var i = startPage; i <= endPage; i++) {
                    pages.push(i);
                }
                $scope.page = pages;
            })
        };

        $scope.getListComment = function (pagenumber, size) {

            if (pagenumber === undefined) {
                pagenumber = 1;
            }
            if (size === undefined) {
                size = 1;
            }
            var data = new FormData();
            data.append('sID', $scope.sid);
            data.append('pagenumber', pagenumber);
            data.append('size', size);
            var url = window.location.origin + '/api/commentOfStory';
            var config = {
                headers: {
                    'Content-Type': undefined
                }
            };
            $http.post(url, data, config).then(function (response) {
                $scope.totalComment = response.data.totalElements;
                $scope.listComment = response.data.content;
                $scope.totalCommentPages = response.data.totalPages;
                $scope.currentCommentPage = response.data.number + 1;
                var startPage = Math.max(1, $scope.currentCommentPage - 2);
                var endPage = Math.min(startPage + 4, $scope.totalCommentPages);
                var pages = [];
                for (var i = startPage; i <= endPage; i++) {
                    pages.push(i);
                }
                $scope.pageComment = pages;
            })
        };

        $scope.addComment = function () {
            if ($scope.commentText.trim().length !== 0) {
                var data = new FormData();
                data.append('sID', $scope.sid);
                data.append('commentText', encryptText($scope.commentText));
                var url = window.location.origin + '/api/add/commentOfStory';
                var config = {
                    headers: {
                        'Content-Type': undefined
                    },
                    transformResponse: function (data, headers, status) {
                        var ret = {messageError: data, status: status};
                        return ret;
                    }
                };
                $http.post(url, data, config).then(function (response) {
                    $scope.commentText = '';
                    $scope.getListComment(1, 1);
                }, function errorCallback(errResponse) {
                    callWarningSweetalert(errResponse.data.messageError);
                })
            } else {
                callWarningSweetalert('Nội dung bình luận không được để trống!');
            }
        };
        $scope.tinymceOptions = {
            plugins: ' emoticons image link lists textcolor placeholder ',
            external_plugins: {
                'placeholder': '/bower/tinymce-placeholder/dist/placeholder/plugin.min.js'
            },
            placeholder: "Vui lòng bình luận bằng tiếng việt có dấu. Spam, chửi bậy, đưa link web khác sẽ bị ban nick",
            menubar: false,
            skin: 'lightgray',
            theme: 'modern',
            relative_urls: false,
            toolbar1: ' formatselect fontselect fontsizeselect | numlist bullist outdent indent | removeformat',
            toolbar2: 'undo redo | bold italic underline | alignleft aligncenter alignright alignjustify | forecolor  emoticons | link image',
            automatic_uploads: true,
            image_description: false,
            file_picker_types: 'image',
            file_picker_callback: function (cb, value, meta) {
                var input = document.createElement('input');
                input.setAttribute('type', 'file');
                input.setAttribute('accept', 'image/*');
                input.onchange = function () {
                    var file = this.files[0];
                    var reader = new FileReader();

                    reader.onload = function () {
                        var id = 'blobid' + (new Date()).getTime();
                        var blobCache = tinymce.activeEditor.editorUpload.blobCache;
                        var base64 = reader.result.split(',')[1];
                        var blobInfo = blobCache.create(id, file, base64);
                        blobCache.add(blobInfo);
                        cb(blobInfo.blobUri(), {title: file.name});
                    };
                    reader.readAsDataURL(file);
                };
                input.click();
            }
        };
    }]);
