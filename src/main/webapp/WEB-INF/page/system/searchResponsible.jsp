<%--
  Created by IntelliJ IDEA.
  User: maginbuu
  Date: 4/24/2020
  Time: 09:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Iungo - SearchResponsible</title>
    <link rel="stylesheet" href="/resource/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resource/css/base/baseStyle.css">

    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="/resource/bootstrap/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>


</head>
<body>
<%@ include file="../navbar.jsp" %>
<div class="row creation-form">
    <div class="col-md-8 offset-md-2">
        <div class="container custom-div">
            <h1>Search Responsible</h1>
            <div class="form-row form-group">
                <div class="col-sm-3 label-column"><label class="col-form-label">Name </label></div>
                <div class="col-sm-3 input-column"><input id="name" name="name" class="form-control"
                                                          type="text"></div>
                <div class="col-sm-2 label-column"><label class="col-form-label">Surname </label></div>
                <div class="col-sm-3 input-column"><input id="surname" name="name" class="form-control"
                                                          type="text"></div>
            </div>
            <div class="form-row form-group">
                <div class="col-sm-3 label-column"><label class="col-form-label">2nd Surname </label></div>
                <div class="col-sm-3 input-column"><input id="secondSurname" name="name" class="form-control"
                                                          type="text"></div>
                <div class="col-sm-2 label-column"></div>
                <div class="col-sm-2"><a class="btn btn-light" id="btn-ajax">Search</a></div>
            </div>

            <div class="form-row form-group">
                <div class="col-sm-3 label-column"><label path="name" class="col-form-label">Responsible </label></div>
                <div class="col-sm-8 input-column">
                    <select class="selectpicker" data-live-search="true" data-width="100%" multiple="false" id="select-responsible" name="select-responsible">
                    </select>
                </div>
            </div>

            <a class="btn btn-light submit-button" onclick="relate()">Accept</a>

        </div>
    </div>
</div>




<script>

    $("#btn-ajax").click(function () {
        $.ajax({

            type: "GET",
            url: "requestResponsibles",
            dataType: "json",
            contentType: 'application/json',
            data: {
                "name": $("#name").val(),
                "surname": $("#surname").val(),
                "secondSurname": $("#secondSurname").val()
            }, //aqui es passen els parametres
            success: function (data) {
                let options, select, selectFinish, i;

                // Get the raw DOM object for the select box
                select = document.getElementById('select-responsible');

                // Clear the old options
                select.options.length = 0;

                // Disable the booked options in both select, each one with its list
                $.each(data, function (index, current) {
                    select.options.add(new Option(current.name + " " + current.surname + " " + current.secondSurname, current.id));
                });

                // Selectpicker refresh
                $('#select-responsible').selectpicker('refresh');


            }
        }).done(function () {

        }).fail(function () {
            console.log("Error Ajax");
        });
    });

    function relate() {
        var select = jQuery("#select-responsible");
        var responsibles = select.val().toString();
        if (responsibles == "") {
            alert("Select a responsible");
        } else {
            window.location.href = '/user/creation/setParentalRelationship?responsibles=' + responsibles;
        }
    }


</script>

</body>
</html>
