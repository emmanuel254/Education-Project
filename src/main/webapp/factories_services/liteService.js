angular.module("homer").service("liteService", function ($http, utilityService) {
    var base_url = utilityService.getBaseUrl();

    this.updateBaseUrl = function () {
        base_url = utilityService.getBaseUrl();
    };

    this.set = function (params, url, alert) {
        return $http({
            headers: {'Content-Type': 'application/json'},
            method: 'POST',
            url: base_url + url,
            data: angular.toJson(params),
            dataType: 'json'
        }).success(function (data) {
            if (data.response_code == "200" || data.response_code == "201") {
                utilityService.notifySuccess(data.response_msg);
            } else {
                utilityService.notifyWarning(data.response_msg);
            }
        }).error(function (data) {
            utilityService.notifyDanger(data.response_msg);
        });
    };

    this.setNoParams = function (url, alert) {
        return $http({
            headers: {'Content-Type': 'application/json'},
            method: 'POST',
            url: base_url + url,
            dataType: 'json'
        }).success(function (data) {
            if (alert) {
                if (data.responseCode == "200") {
                    utilityService.notifySuccess(data.message);
                } else {
                    utilityService.notifyWarning(data.message);
                }
            }
        }).error(function (data) {
            if (alert) {
                utilityService.notifyWarning(data.message);
            }
        });
    };

    this.get = function (url, alert) {
        return $http({
            headers: {'Content-Type': 'application/json'},
            method: 'GET',
            url: base_url + url,
            dataType: 'json'
        }).success(function (data) {

        }).error(function (data) {
            if (alert) {
                utilityService.notifyWarning(data.message);
            }
        });
    };
    this.deleteObject = function (url) {
        return $http({
            headers: {'Content-Type': 'application/json'},
            method: 'DELETE',
            url: base_url + url,
            dataType: 'json'
        }).success(function (data) {
            if (data.response_code == "200") {
                utilityService.notifySuccess(data.response_msg);
            } else {
                utilityService.notifyWarning(data.response_msg);
            }
        }).error(function (data) {
            utilityService.notifyWarning(data.response_msg);
        });
    };
    this.put = function (params, url) {
        return $http({
            headers: {'Content-Type': 'application/json'},
            method: 'PUT',
            url: base_url + url,
            data: angular.toJson(params),
            dataType: 'json'
        }).success(function (data) {
            if (data.response_code == "200" || data.response_code == "201") {
                utilityService.notifySuccess(data.response_msg);
            } else {
                utilityService.notifyWarning(data.response_msg);
            }
        }).error(function (data) {
            utilityService.notifyDanger(data.response_msg);
        });
    };
    this.putnoParams = function (url, alert) {
        return $http({
            headers: {'Content-Type': 'application/json'},
            method: 'PUT',
            url: base_url + url,
            dataType: 'json'
        }).success(function (data) {
            if (data.responseCode == "200") {
                if (alert) {
                    utilityService.notifySuccess(data.message);
                }
            } else {
                if (alert) {
                    utilityService.notifyWarning(data.message);
                }
            }
        }).error(function (data) {
            if (alert) {
                utilityService.notifyWarning(data.message);
            }
        });
    };
});


