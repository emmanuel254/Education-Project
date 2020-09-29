angular.module("homer").service("utilityService", function (notify) {
    var base_url = "";

    this.getBaseUrl = function () {
        return base_url;
    };

    var homerTemplate = "views/notification/notify.html";

    this.showNotification = function (text, notify_class) {
        notify({
            message: text,
            classes: notify_class,
            templateUrl: homerTemplate
        });
    };

    this.notifyInfo = function (text) {
        notify.closeAll();
        var notify_class = "alert-info";
        this.showNotification(text, notify_class);
    };
    this.notifySuccess = function (text) {
        notify.closeAll();
        var notify_class = "alert-success";
        this.showNotification(text, notify_class);
    };
    this.notifyWarning = function (text) {
        notify.closeAll();
        var notify_class = "alert-warning";
        this.showNotification(text, notify_class);
    };
    this.notifyDanger = function (text) {
        notify.closeAll();
        var notify_class = "alert-danger";
        this.showNotification(text, notify_class);
    };
});
