function fixWrapperHeight() {
    var headerH = 62;
    var navigationH = $("#navigation").height();
    var contentH = $(".content").height();

    navigationH > contentH && $("#wrapper").css("min-height", navigationH + "px"), navigationH > contentH && navigationH < $(window).height() && $("#wrapper").css("min-height", $(window).height() - headerH + "px"), contentH > navigationH && contentH < $(window).height() && $("#wrapper").css("min-height", $(window).height() - headerH + "px")
}

function setBodySmall() {
    $(this).width() < 769 ? $("body").addClass("page-small") : ($("body").removeClass("page-small"), $("body").removeClass("show-sidebar"))
}

function propsFilter() {
    return function (items, props) {
        var out = [];
        return angular.isArray(items) ? items.forEach(function (item) {
            for (var itemMatches = !1, keys = Object.keys(props), i = 0; i < keys.length; i++) {
                var prop = keys[i],
                        text = props[prop].toLowerCase();
                if (-1 !== item[prop].toString().toLowerCase().indexOf(text)) {
                    itemMatches = !0;
                    break
                }
            }
            itemMatches && out.push(item)
        }) : out = items, out
    }
}

function pageTitle($rootScope, $timeout) {
    return {
        link: function (scope, element) {
            var listener = function (event, toState, toParams, fromState, fromParams) {
                var title = "HOMER | AngularJS Responsive WebApp";
                toState.data && toState.data.pageTitle && (title = "HOMER | " + toState.data.pageTitle), $timeout(function () {
                    element.text(title)
                })
            };
            $rootScope.$on("$stateChangeStart", listener)
        }
    }
}

function sideNavigation($timeout) {
    return {
        restrict: "A",
        link: function (scope, element) {
            element.metisMenu();
            var menuElement = $('#side-menu a:not([href$="\\#"])');
            menuElement.click(function () {
                $(window).width() < 769 && $("body").toggleClass("show-sidebar")
            })
        }
    }
}

function minimalizaMenu($rootScope) {
    return {
        restrict: "EA",
        template: '<div class="header-link hide-menu" ng-click="minimalize()"><i class="fa fa-bars"></i></div>',
        controller: function ($scope, $element) {
            $scope.minimalize = function () {
                $(window).width() < 769 ? $("body").toggleClass("show-sidebar") : $("body").toggleClass("hide-sidebar")
            }
        }
    }
}

function sparkline() {
    return {
        restrict: "A",
        scope: {
            sparkData: "=",
            sparkOptions: "="
        },
        link: function (scope, element, attrs) {
            scope.$watch(scope.sparkData, function () {
                render()
            }), scope.$watch(scope.sparkOptions, function () {
                render()
            });
            var render = function () {
                $(element).sparkline(scope.sparkData, scope.sparkOptions)
            }
        }
    }
}

function icheck($timeout) {
    return {
        restrict: "A",
        require: "ngModel",
        link: function ($scope, element, $attrs, ngModel) {
            return $timeout(function () {
                var value;
                return value = $attrs.value, $scope.$watch($attrs.ngModel, function (newValue) {
                    $(element).iCheck("update")
                }), $(element).iCheck({
                    checkboxClass: "icheckbox_square-green",
                    radioClass: "iradio_square-green"
                }).on("ifChanged", function (event) {
                    return "checkbox" === $(element).attr("type") && $attrs.ngModel && $scope.$apply(function () {
                        return ngModel.$setViewValue(event.target.checked)
                    }), "radio" === $(element).attr("type") && $attrs.ngModel ? $scope.$apply(function () {
                        return ngModel.$setViewValue(value)
                    }) : void 0
                })
            })
        }
    }
}

function panelTools($timeout) {
    return {
        restrict: "A",
        scope: !0,
        templateUrl: "views/common/panel_tools.html",
        controller: function ($scope, $element) {
            $scope.showhide = function () {
                var hpanel = $element.closest("div.hpanel"),
                        icon = $element.find("i:first"),
                        body = hpanel.find("div.panel-body"),
                        footer = hpanel.find("div.panel-footer");
                body.slideToggle(300), footer.slideToggle(200), icon.toggleClass("fa-chevron-up").toggleClass("fa-chevron-down"), hpanel.toggleClass("").toggleClass("panel-collapse"), $timeout(function () {
                    hpanel.resize(), hpanel.find("[id^=map-]").resize()
                }, 50)
            }, $scope.closebox = function () {
                var hpanel = $element.closest("div.hpanel");
                hpanel.remove()
            }
        }
    }
}

function panelToolsFullscreen($timeout) {
    return {
        restrict: "A",
        scope: !0,
        templateUrl: "views/common/panel_tools_fullscreen.html",
        controller: function ($scope, $element) {
            $scope.showhide = function () {
                var hpanel = $element.closest("div.hpanel"),
                        icon = $element.find("i:first"),
                        body = hpanel.find("div.panel-body"),
                        footer = hpanel.find("div.panel-footer");
                body.slideToggle(300), footer.slideToggle(200), icon.toggleClass("fa-chevron-up").toggleClass("fa-chevron-down"), hpanel.toggleClass("").toggleClass("panel-collapse"), $timeout(function () {
                    hpanel.resize(), hpanel.find("[id^=map-]").resize()
                }, 50)
            }, $scope.closebox = function () {
                var hpanel = $element.closest("div.hpanel");
                hpanel.remove()
            }, $scope.fullscreen = function () {
                var hpanel = $element.closest("div.hpanel"),
                        icon = $element.find("i:first");
                $("body").toggleClass("fullscreen-panel-mode"), icon.toggleClass("fa-expand").toggleClass("fa-compress"), hpanel.toggleClass("fullscreen"), setTimeout(function () {
                    $(window).trigger("resize")
                }, 100)
            }
        }
    }
}

function smallHeader() {
    return {
        restrict: "A",
        scope: !0,
        controller: function ($scope, $element) {
            $scope.small = function () {
                var icon = $element.find("i:first"),
                        breadcrumb = $element.find("#hbreadcrumb");
                $element.toggleClass("small-header"), breadcrumb.toggleClass("m-t-lg"), icon.toggleClass("fa-arrow-up").toggleClass("fa-arrow-down")
            }
        }
    }
}

function animatePanel($timeout, $state) {
    return {
        restrict: "A",
        link: function (scope, element, attrs) {
            var startAnimation = 0,
                    delay = .06,
                    start = Math.abs(delay) + startAnimation;
            $state.current.name;
            attrs.effect || (attrs.effect = "zoomIn"), delay = attrs.delay ? attrs.delay / 10 : .06, attrs.child ? attrs.child = "." + attrs.child : attrs.child = ".row > div";
            var panel = element.find(attrs.child);
            panel.addClass("opacity-0");
            panel.length * delay * 1e3 + 700;
            $timeout(function () {
                panel = element.find(attrs.child), panel.addClass("stagger").addClass("animated-panel").addClass(attrs.effect);
                var panelsCount = panel.length + 10,
                        animateTime = panelsCount * delay * 1e4 / 10;
                panel.each(function (i, elm) {
                    start += delay;
                    var rounded = Math.round(10 * start) / 10;
                    $(elm).css("animation-delay", rounded + "s"), $(elm).removeClass("opacity-0")
                }), $timeout(function () {
                    $(".stagger").css("animation", ""), $(".stagger").removeClass(attrs.effect).removeClass("animated-panel").removeClass("stagger")
                }, animateTime)
            })
        }
    }
}

function landingScrollspy() {
    return {
        restrict: "A",
        link: function (scope, element, attrs) {
            element.scrollspy({
                target: ".navbar-fixed-top",
                offset: 80
            })
        }
    }
}

function appCtrl($http, $scope, $timeout) {
    $scope.checkOne = !0, $scope.barProfileData = [5, 6, 7, 2, 0, 4, 2, 4, 5, 7, 2, 4, 12, 11, 4], $scope.barProfileOptions = {
        type: "bar",
        barWidth: 7,
        height: "30px",
        barColor: "#62cb31",
        negBarColor: "#53ac2a"
    }, $scope.chartIncomeData = [{
            label: "line",
            data: [
                [1, 10],
                [2, 26],
                [3, 16],
                [4, 36],
                [5, 32],
                [6, 51]
            ]
        }], $scope.chartIncomeOptions = {
        series: {
            lines: {
                show: !0,
                lineWidth: 0,
                fill: !0,
                fillColor: "#64cc34"
            }
        },
        colors: ["#62cb31"],
        grid: {
            show: !1
        },
        legend: {
            show: !1
        }
    }, $scope.dynamicTooltip = "Hello, World!", $scope.htmlTooltip = "I've been made <b>bold</b>!", $scope.dynamicTooltipText = "Dynamic", $scope.dynamicPopover = "Hello, World!", $scope.dynamicPopoverTitle = "Title", $scope.totalItems = 64, $scope.currentPage = 4, $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo
    }, $scope.states = ["Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Dakota", "North Carolina", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"], $scope.getLocation = function (val) {
        return $http.get("http://maps.googleapis.com/maps/api/geocode/json", {
            params: {
                address: val,
                sensor: !1
            }
        }).then(function (response) {
            return response.data.results.map(function (item) {
                return item.formatted_address
            })
        })
    }, $scope.rate = 7, $scope.max = 10, $scope.hoveringOver = function (value) {
        $scope.overStar = value, $scope.percent = 100 * (value / this.max)
    }, $scope.groups = [{
            title: "Dynamic Group Header - 1",
            content: "A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. "
        }, {
            title: "Dynamic Group Header - 2",
            content: "A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. "
        }], $scope.oneAtATime = !0;
    var data1 = [
        [0, 55],
        [1, 48],
        [2, 40],
        [3, 36],
        [4, 40],
        [5, 60],
        [6, 50],
        [7, 51]
    ],
            data2 = [
                [0, 56],
                [1, 49],
                [2, 41],
                [3, 38],
                [4, 46],
                [5, 67],
                [6, 57],
                [7, 59]
            ];
    $scope.chartUsersData = [data1, data2], $scope.chartUsersOptions = {
        series: {
            splines: {
                show: !0,
                tension: .4,
                lineWidth: 1,
                fill: .4
            }
        },
        grid: {
            tickColor: "#f0f0f0",
            borderWidth: 1,
            borderColor: "f0f0f0",
            color: "#6a6c6f"
        },
        colors: ["#62cb31", "#efefef"]
    }, $scope.PieChart = {
        data: [1, 5],
        options: {
            fill: ["#62cb31", "#edf0f5"]
        }
    }, $scope.PieChart2 = {
        data: [226, 360],
        options: {
            fill: ["#62cb31", "#edf0f5"]
        }
    }, $scope.PieChart3 = {
        data: [.52, 1.561],
        options: {
            fill: ["#62cb31", "#edf0f5"]
        }
    }, $scope.PieChart4 = {
        data: [1, 4],
        options: {
            fill: ["#62cb31", "#edf0f5"]
        }
    }, $scope.PieChart5 = {
        data: [226, 134],
        options: {
            fill: ["#62cb31", "#edf0f5"]
        }
    }, $scope.PieChart6 = {
        data: [.52, 1.041],
        options: {
            fill: ["#62cb31", "#edf0f5"]
        }
    }, $scope.BarChart = {
        data: [5, 3, 9, 6, 5, 9, 7, 3, 5, 2],
        options: {
            fill: ["#dbdbdb", "#62cb31"]
        }
    }, $scope.LineChart = {
        data: [5, 9, 7, 3, 5, 2, 5, 3, 9, 6, 5, 9, 4, 7, 3, 2, 9, 8, 7, 4, 5, 1, 2, 9, 5, 4, 7],
        options: {
            fill: "#62cb31",
            stroke: "#62cb31",
            width: 64
        }
    }, $scope.stanimation = "bounceIn", $scope.runIt = !0, $scope.runAnimation = function () {
        $scope.runIt = !1, $timeout(function () {
            $scope.runIt = !0
        }, 100)
    }
}

function sweetAlert($timeout, $window) {
    var swal = $window.swal;
    return {
        swal: function (arg1, arg2, arg3) {
            $timeout(function () {
                "function" == typeof arg2 ? swal(arg1, function (isConfirm) {
                    $timeout(function () {
                        arg2(isConfirm)
                    })
                }, arg3) : swal(arg1, arg2, arg3)
            }, 200)
        },
        success: function (title, message) {
            $timeout(function () {
                swal(title, message, "success")
            }, 200)
        },
        error: function (title, message) {
            $timeout(function () {
                swal(title, message, "error")
            }, 200)
        },
        warning: function (title, message) {
            $timeout(function () {
                swal(title, message, "warning")
            }, 200)
        },
        info: function (title, message) {
            $timeout(function () {
                swal(title, message, "info")
            }, 200)
        }
    }
}

function touchSpin() {
    return {
        restrict: "A",
        scope: {
            spinOptions: "="
        },
        link: function (scope, element, attrs) {
            scope.$watch(scope.spinOptions, function () {
                render()
            });
            var render = function () {
                $(element).TouchSpin(scope.spinOptions)
            }
        }
    }
}