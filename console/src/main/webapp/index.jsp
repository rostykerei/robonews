<%--

    Robonews.io

    Copyright (c) 2013-2015 Rosty Kerei.
    All rights reserved.

--%>
<!DOCTYPE html>
<html ng-app="robonews-console">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Page title set in pageTitle directive -->
    <title page-title></title>

    <!-- Bootstrap and Fonts -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Full Calendar -->
    <link href="css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">

    <!-- Morris -->
    <link href="css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">

    <!-- Summernote -->
    <link href="css/plugins/summernote/summernote.css" rel="stylesheet">
    <link href="css/plugins/summernote/summernote-bs3.css" rel="stylesheet">

    <!-- Style for wizard - based on Steps plugin-->
    <link href="css/plugins/steps/jquery.steps.css" rel="stylesheet">

    <!-- FancyBox -->
    <link href="js/plugins/fancybox/jquery.fancybox.css" rel="stylesheet">

    <!-- Data Tables -->
    <link href="css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">

    <!-- Chosen Plugin -->
    <link href="css/plugins/chosen/chosen.css" rel="stylesheet">

    <!-- Dropzone style -->
    <link href="css/plugins/dropzone/basic.css" rel="stylesheet">
    <link href="css/plugins/dropzone/dropzone.css" rel="stylesheet">

    <!-- Switchery iOS style -->
    <link href="css/plugins/switchery/switchery.css" rel="stylesheet">

    <!-- noUiSlider for Range Slider-->
    <link href="css/plugins/nouslider/jquery.nouislider.css" rel="stylesheet">

    <!-- DataPicker -->
    <link href="css/plugins/datapicker/angular-datapicker.css" rel="stylesheet">

    <!-- Ion Range Slider -->
    <link href="css/plugins/ionRangeSlider/ion.rangeSlider.css" rel="stylesheet">
    <link href="css/plugins/ionRangeSlider/ion.rangeSlider.skinFlat.css" rel="stylesheet">

    <!-- Main Inspinia CSS files -->
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body ng-controller="MainCtrl as main">

<!-- Wrapper-->
<div id="wrapper">

    <!-- Navigation -->
    <div ng-include="'views/common/navigation.html'"></div>

    <!-- Page wraper -->
    <!-- ng-class with current state name give you the ability to extended customization your view -->
    <div id="page-wrapper" class="gray-bg {{$state.current.name}}">

        <!-- Page wrapper -->
        <div ng-include="'views/common/topnavbar.html'"></div>

        <!-- Main view  -->
        <div ui-view></div>

        <!-- Footer -->
        <div ng-include="'views/common/footer.html'"></div>

    </div>
    <!-- End page wrapper-->

</div>
<!-- End wrapper-->


<!-- jQuery and Bootstrap -->
<script src="js/jquery/jquery-2.1.1.min.js"></script>
<script src="js/plugins/jquery-ui/jquery-ui.js"></script>
<script src="js/bootstrap/bootstrap.min.js"></script>

<!-- MetsiMenu -->
<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>

<!-- Peace JS -->
<script src="js/plugins/pace/pace.min.js"></script>

<!-- SlimScroll -->
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Data Tables -->
<script src="js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="js/plugins/dataTables/dataTables.bootstrap.js"></script>

<!-- Custom and plugin javascript -->
<script src="js/inspinia.js"></script>

<!-- Angular scripts-->
<script src="js/angular/angular.min.js"></script>
<script src="js/ui-router/angular-ui-router.min.js"></script>
<script src="js/angular/angular-resource.min.js"></script>
<script src="js/bootstrap/ui-bootstrap-tpls-0.11.0.min.js"></script>
<script src="js/plugins/dataTables/angular-datatables.min.js"></script>

<!-- Angular Dependiences -->

<!-- Anglar App Script -->
<script src="js/app.js"></script>
<script src="js/config.js"></script>
<script src="js/directives.js"></script>
<script src="js/services.js"></script>
<script src="js/controllers.js"></script>


</body>
</html>