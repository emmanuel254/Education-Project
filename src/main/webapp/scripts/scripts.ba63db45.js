function configState($stateProvider, $urlRouterProvider, $httpProvider) {
    $httpProvider.defaults.withCredentials = true;
    $urlRouterProvider.when("/", "/h").otherwise("/h/global");

    $stateProvider.state("home", {
        url: "/h",
        templateUrl: "./views/home.html",
        controller: function ($scope) {
            // Variables for sorting data on a table
            $scope.propertyName = '';
            $scope.reverse = true;  
            $scope.sortBy = function(propertyName) {
                $scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse : false;
                $scope.propertyName = propertyName;
              };
            // End of sorting variables and functions 

            $scope.loading = false; //Show or hide loading spinner

            // Receives a timestamp and then returns a datetime string
            $scope.timestampToDate = function (timestamp) {
                var ts = new Date(timestamp);
                return ts.toLocaleString()
            }

            //Variable and a function for nummbering items in a table
            $scope.serial = 1;
            $scope.indexCount = function(newPageNumber){
                $scope.serial = newPageNumber * 10 - 9;
            }
        }
    }).state("home.global", {
        url: "/global",
        templateUrl: "./views/education_partials/global.html",
        resolve: {
            institutions_list: function (appFactory) {
                return appFactory.getAllInstitutions();
            }
        },
        controller: function ($scope, $window, liteService, appFactory, institutions_list) {
            // Variable Initialization
            var swal = $window.swal; //Initializing sweet alert
            // Local variables
            $scope.institutions = institutions_list;
            $scope.showform = false;
            $scope.new_institution = {};

            $scope.showAddForm = function(){
                $scope.showform = true;
                $scope.new_institution = {};
            }

            //Takes care of edit functionality by assigning default cliked item
            $scope.updateInstitution = function(institution){
                $scope.showform = true;

                $scope.new_institution.name = institution.name;
                $scope.new_institution.institutionid = institution.institutionid;
            }

            //Function for sending data to the database
            $scope.manageInstitution = function () {
                $scope.loading = true;
                console.log($scope.new_institution);

                if($scope.new_institution.institutionid){//Means that the current mode is editing institution

                    liteService.put($scope.new_institution, "api/records/institution?institutionId="+$scope.new_institution.institutionid)
                    .then(function () {
                        $scope.loading = false; $scope.showform = false;

                        $scope.getUniversities();
                    }).catch(function () {
                        $scope.loading = false
                    });

                }else {

                    liteService.set($scope.new_institution, "api/records/institution").then(function () {
                        $scope.loading = false; $scope.showform = false

                        $scope.getUniversities();
                    }).catch(function () {
                        $scope.loading = false
                    });
                }
                
            };

            // Fetches the list of all universities from the db
            $scope.getUniversities = function () {
                appFactory.getAllInstitutions().then(function (resp) {
                    $scope.institutions = resp;
                });
            };

            //Returns the number of students per institution
            $scope.countStudents = function (courses){
                let total_students = 0;
                if(courses.length > 0){
                    courses.forEach(item => {
                        total_students += item.students.length
                    });
                }
                
                return total_students;
            }

            //Deletes an institution, a user must confirm first via a sweet alert dialog.
            $scope.deleteInstitution = function(institution){
                $scope.showConfirmDialog("api/records/institution?institutionId="+institution.institutionid, institution)
            }

            // Confirmation dialog before deleting an institution
            $scope.showConfirmDialog = function(url, institution) {
                swal({
                   title: "Delete "+institution.name,
                   text: "Proceeding with this action means that "+institution.name+" will be removed permanently.",
                   type: "warning",
                   showCancelButton: true,
                   confirmButtonColor: "#e74c3c",confirmButtonText: "Yes, proceed!",
                   cancelButtonText: "No, cancel",
                   closeOnConfirm: true,
                   closeOnCancel: true }, 
                   function(isConfirm){ 
                       if (isConfirm) {
                            //Sending the delete request to the endpoint
                            liteService.deleteObject(url).then(() => {
                            $scope.getUniversities()
                           })
                       }
                   });
           }

        //Variable and a function for nummbering items in a table, normal $index numbering is affected by pagination
        $scope.serial = 1;
        $scope.indexCount = function(newPageNumber){
            $scope.serial = newPageNumber * 10 - 9;
        }
        }
    }).state("home.global.singleinstitution", {
        url: "/singleinstitution/:index",
        templateUrl: "./views/education_partials/singleinst.html",
        resolve: {
            // Before anything else, fetch list of courses per institutionid
            courses_list: function (appFactory, $stateParams) {
                let institutionid = $stateParams.index
                return appFactory.getCoursesByInstitutionId(institutionid);
            }
        },
        controller: function ($scope, $stateParams, $window, liteService, appFactory, courses_list) {
            // Initializing local variables
            $scope.passed_index = $stateParams.index;
            $scope.courses_list = courses_list;
            var swal = $window.swal; //Initializing sweet alert
            
            // Retrieve a single institution from the list of allinstitutions, this is according to the route params passed via the url
            const [institution] = $scope.institutions.filter(item =>  item.institutionid == $scope.passed_index)
            $scope.institution = institution;

            $scope.showform = false; //Show or hide form
            $scope.mode = 'add'; // Change form mode to either add a course or edit a course,
            // Default values to be displayed in a form, using ng-model (courseData below)
            $scope.courseData = {
                institution : institution
            };

            // If a user wants to edit, fill in the form with the default values of the course in question
            // This is achieved by setting up courseData with default course values
            $scope.manageCourse = function(courseid){
                if(courseid){
                    // Get course clicked to be edited
                    const [course] = $scope.courses_list.filter(item => item.courseid == courseid)

                    $scope.courseData.name = course.name;
                    $scope.courseData.courseid = course.courseid;
                    $scope.courseData.institution = institution
                    $scope.mode = 'edit'
                }
            }
            
            //submitCourseData function sends either put or post request to the database, depending on the mode
            $scope.submitCourseData = function(){
                
                $scope.loading = true
                const institutionid = $scope.courseData.institution.institutionid;
                const payload = {//Data to be sent to the database
                    name : $scope.courseData.name,
                    institutionId: institutionid
                }

                if($scope.mode == 'edit'){
                    // Submitting to edit course endpoint
                    liteService.put(payload, "api/records/course?courseId="+$scope.courseData.courseid)
                           .then(() => {
                                $scope.loading = false
                                $scope.getAllCourses(institutionid)

                                $scope._resetBackToDefault()
                           })
                           .catch(err => {
                               $scope.loading = false
                               console.log(err.response)
                           })
                }
                else{
                    // Submitting to add course endpoint
                    liteService.set(payload, "api/records/course?institutionId="+institutionid)
                            .then(() => {
                                $scope.loading = false
                                $scope.getAllCourses(institutionid)

                                $scope._resetBackToDefault()
                            })
                            .catch(err => {
                                $scope.loading = false
                                console.log(err.response)
                            })
                }
                
            }

            // getAllCourses => a function that retrieves all the courses as per the institution
            $scope.getAllCourses =function (institutionid){
                appFactory.getCoursesByInstitutionId(institutionid)
                          .then(resp => {
                            //   List of courses returned is then assigned to courses_list variable
                              $scope.courses_list = resp;
                          })
            }

            // Before deleting a course, a user is first prompted to confirm using sweet alert
            $scope.deleteCourse = function(course){
                $scope.showConfirmationDialog("api/records/course?courseId="+course.courseid, course)
            }

            // Displaying sweet alert dialog
            $scope.showConfirmationDialog = function(url, course) {
                swal({
                   title: "Delete "+course.name,
                   text: "Proceeding with this action means that "+course.name+" will be removed permanently.",
                   type: "warning",
                   showCancelButton: true,
                   confirmButtonColor: "#e74c3c",confirmButtonText: "Yes, proceed!",
                   cancelButtonText: "No, cancel",
                   closeOnConfirm: true,
                   closeOnCancel: true }, 
                   function(isConfirm){ 
                        //If a user clicks 'OK', delete the course
                       if (isConfirm) {
                        //  Call delete request endpoint from liteservice
                           liteService.deleteObject(url).then(() => {
                            $scope.getAllCourses($scope.passed_index)
                           })
                       }
                   });
           }

           //A function for reseting variables back to their default values
            $scope._resetBackToDefault = function (){
                $scope.mode = 'add'; // Change form mode to either add or edit
                $scope.courseData = {
                    institution : institution
                };
            }

            //Variable and a function for nummbering items in a table, normal $index numbering is affected by pagination
            $scope.serial = 1;
            $scope.indexCount = function(newPageNumber){
                $scope.serial = newPageNumber * 10 - 9;
            }
        }
    }).state("home.global.singleinstitution.coursedetails", {
        url: "/coursedetails/:courseid",
        templateUrl: "./views/education_partials/coursedetails.html",
        controller: function ($scope, $stateParams, $window, liteService, appFactory) {
            // Initializing variables
            $scope.courseid = $stateParams.courseid;
            var swal = $window.swal; //Initializing sweet alert

            const [course] = $scope.courses_list.filter(item =>  item.courseid == $scope.courseid)
            $scope.course = course;
            $scope.students = course.students //List of all students per course

            console.log($scope.course)
            
            $scope.studentmode = 'add'; // Change form mode to either add or edit
            $scope.studentData = {
                course : course
            };

            //Function for either adding or updating student data
            $scope.manageStudent = function(studentid){
                if(studentid){
                    // Get student clicked to be edited
                    const [student] = $scope.students.filter(item => item.studentid == studentid)

                    $scope.studentData.name = student.name;
                    $scope.studentData.course = course;
                    $scope.studentData.studentid = studentid

                    $scope.studentmode = 'edit'
                    console.log($scope.studentData)
                }
            }
            
            //Submitting formdata to student endpoint for either updating or editing
            $scope.submitStudentData = function(){
                $scope.loading = true
                const courseId = $scope.studentData.course.courseid
                
                // Data to be submitted to the backend
                const payload = {
                    courseId: courseId,
                    name : $scope.studentData.name
                }

                if($scope.studentmode == 'edit'){
                    // Sending a put request to update student details
                    liteService.put(payload, "api/records/student?studentId="+$scope.studentData.studentid)
                           .then(() => {
                                $scope.loading = false
                                
                                $scope.getUpdatedCoursesData($scope.institution.institutionid);
                                $scope._resetToDefault();
                           })
                           .catch(err => {
                                $scope.loading = false
                                console.log(err.response)
                           })
                }else {
                    // Sending a post request to add/register new student
                    liteService.set(payload, "api/records/student")
                           .then(() => {
                                $scope.loading = false

                                $scope.getUpdatedCoursesData($scope.institution.institutionid);
                                $scope._resetToDefault();
                           })
                           .catch(err => {
                                $scope.loading = false
                                console.log(err.response)
                           })
                }
            }

            // A function for showing a confirmational dialog before proceeding with the action of deleting a student
            $scope.deleteStudent = function(student){
                $scope.showDeleteAlert("api/records/student?studentId="+student.studentid, student)
            }

            // Sweet alert dialog, promting the user to either continue or cancel delete action
            $scope.showDeleteAlert = function(url, student) {
                swal({
                   title: "Delete "+student.name,
                   text: "Proceeding with this action means that "+student.name+"'s record will be removed permanently.",
                   type: "warning",
                   showCancelButton: true,
                   confirmButtonColor: "#e74c3c",confirmButtonText: "Yes, proceed!",
                   cancelButtonText: "No, cancel",
                   closeOnConfirm: true,
                   closeOnCancel: true }, 
                   function(isConfirm){ 
                        //Check if delete action has been confirmed
                       if (isConfirm) {
                           //Send a delete request to the backend
                           liteService.deleteObject(url).then(() => {
                            $scope.getUpdatedCoursesData($scope.institution.institutionid)
                           })
                       }
                   });
           }

           //A function that returns updated list of courses, called upon successfull edit or addition of a course
            $scope.getUpdatedCoursesData =function (institutionid){
                appFactory.getCoursesByInstitutionId(institutionid)
                          .then(resp => {
                            let [updated_course] = resp.filter(item =>  item.courseid == $scope.courseid)
                            $scope.course = updated_course;
                            $scope.students = updated_course.students
                          })
            }

            // Resets local variables back to their default values
            $scope._resetToDefault = function (){
                $scope.studentmode = 'add'
                $scope.studentData = {
                    course : course
                };
            }

            //Variable and a function for nummbering items in a table, normal $index numbering is affected by pagination
            $scope.serial = 1;
            $scope.indexCount = function(newPageNumber){
                $scope.serial = newPageNumber * 10 - 9;
            }
        }
    }).state("home.global.student", {
        url: "/studentprofile/:studentid",
        templateUrl: "./views/education_partials/studentprofile.html",
        resolve: {
            // Before anything, fetch student according to the supplied id
            studentprofile: function (appFactory, $stateParams) {
                let studentid = $stateParams.studentid
                return appFactory.getStudentById(studentid);
            }
        },
        controller: function ($scope, $stateParams,appFactory, studentprofile, liteService) {
            // Local variables initialization
            $scope.studentid = $stateParams.studentid;
            $scope.studentprofile = studentprofile;
            $scope.errors = '';
            $scope.showform = false

            // Form data variables, to be used in ng-models. For easier management we assing profileUpdate variable
            // with default course, institution and name values
            $scope.profileUpdate = {
                institution : {},
                course: {},
                name: studentprofile.name
            }

            // Submitting profileUpdate data to the backend for processing
            $scope.updateInfo = function () {
                // First check for validation errors
                if(angular.equals({}, $scope.profileUpdate.institution) || angular.equals({}, $scope.profileUpdate.course)){
                    // True if a user has not selected any course or institution
                    $scope.show = true;
                    $scope.errors = 'You must select an institution and course before submitting'
                }else if($scope.profileUpdate.name == ''){
                    // True is a user supplied a blank name
                    $scope.show = true;
                    $scope.errors = 'Student name is required' 
                }else if($scope.studentprofile.institution.institutionid != $scope.profileUpdate.institution.institutionid && $scope.profileUpdate.course.name == $scope.studentprofile.course.name){
                    // True if a user is transfering a student to the same course that s/he was before, not logic
                    $scope.show = true;
                    $scope.errors = 'You are transfering '+studentprofile.name+' to the same course as s/he was before.'
                }else {
                    //If all conditions are met, submit data to the database

                    // data to be sumbitted for processing
                    const payload = {
                        courseId : $scope.profileUpdate.course.courseid,
                        name : $scope.profileUpdate.name
                    }

                    //Show loading spinner
                    $scope.loading = true;

                    //Call the put request endpoint and supply the necessary data
                    liteService.put(payload, "api/records/student?studentId="+$scope.studentid)
                           .then(() => {
                                $scope.loading = false
                                $scope.getUpdatedStudentProfile();
                           })
                           .catch(err => {
                                $scope.loading = false
                               console.log(err.response)
                           })
                }
            }

            // After successfull update, fetch the student's updated details/profile
            $scope.getUpdatedStudentProfile = function () {
                appFactory.getStudentById($scope.studentid)
                        .then(resp => {
                            $scope.studentprofile = resp;
                            $scope.showform = false;
                        })
            }

            //closeAlert => this function closes boostrap alert after 4 seconds of display
            $scope.closeAlert = function(index) {
                $scope.show = false;
            };
        }
    })
    .state("home.courses", {
        url: "/courses",
        templateUrl: "./views/education_partials/courses.html",
        resolve: {
            // Fetch courses before doing anything in the controller
            courses_list: function (appFactory) {
                return appFactory.getAllCourses();
            }
        },
        controller: function ($scope, courses_list) {
            // Assign fetched courses to courses_list $scope variable
            $scope.courses_list = courses_list;

            //Variable and a function for nummbering items in a table, normal $index numbering is affected by pagination
            $scope.serial = 1;
            $scope.indexCount = function(newPageNumber){
                $scope.serial = newPageNumber * 10 - 9;
            }
        }
    }).state("home.students", {
        url: "/students",
        templateUrl: "./views/education_partials/students.html",
        resolve: {
            // Fetch all students from the db
            students_list: function (appFactory) {
                return appFactory.getAllStudents();
            }
        },
        controller: function ($scope, students_list) {
            // Assign fetched students to students_list $scope variable
            $scope.students_list = students_list;

            //Variable and a function for nummbering items in a table, normal $index numbering is affected by pagination
            $scope.serial = 1;
            $scope.indexCount = function(newPageNumber){
                $scope.serial = newPageNumber * 10 - 9;
            }
        }
    });
}

$(document).ready(function () {
    fixWrapperHeight(), setBodySmall()
});
$(window).bind("load", function () {
    $(".splash").css("display", "none")
});
$(window).bind("resize click", function () {
    setBodySmall(), setTimeout(function () {
        fixWrapperHeight()
    }, 300)
}), function () {
    angular.module("homer", ["ui.router",'angularUtils.directives.dirPagination', "ngSanitize", "ui.bootstrap", "angular-flot",  "angles", "angular-peity", "cgNotify", "angles", "ngAnimate", "ui.map", "ui.calendar", "summernote", "ngGrid", "ui.tree", "bm.bsTour", "datatables", "xeditable", "ui.select", "ui.sortable", "ui.footable", "angular-chartist", "ui.codemirror"])
}();

angular.module("homer").config(configState);
angular.module("homer").run(function ($rootScope, $state, editableOptions) {
    $rootScope.$state = $state;
    editableOptions.theme = "bs3";
});
angular.module("homer").filter("propsFilter", propsFilter);
angular.module("homer").directive("pageTitle", pageTitle);
angular.module("homer").directive("sideNavigation", sideNavigation);
angular.module("homer").directive("minimalizaMenu", minimalizaMenu);
angular.module("homer").directive("sparkline", sparkline);
angular.module("homer").directive("icheck", icheck);
angular.module("homer").directive("panelTools", panelTools);
angular.module("homer").directive("panelToolsFullscreen", panelToolsFullscreen);
angular.module("homer").directive("smallHeader", smallHeader);
angular.module("homer").directive("animatePanel", animatePanel);
angular.module("homer").directive("landingScrollspy", landingScrollspy);
angular.module("homer").controller("appCtrl", appCtrl);
angular.module("homer").factory("sweetAlert", sweetAlert);
angular.module("homer").directive("touchSpin", touchSpin);