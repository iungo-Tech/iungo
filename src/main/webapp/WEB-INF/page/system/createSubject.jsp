<%@ page import="com.model.enums.Department" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Iungo - New Subject</title>

    <link rel="stylesheet" href="/resource/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resource/css/creation/creationStyle.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="/resource/bootstrap/js/bootstrap.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css">


</head>

<body>
<%@ include file="../navbar.jsp" %>
<div class="row creation-form">
    <div class="col-md-8 offset-md-2">
        <c:url value="/subject/creation" var="url"></c:url>
        <form:form class="custom-form" method="post" action="/subject/creation" modelAttribute="subject" commandName="subject">
            <h1>Create Subject</h1>
            <div class="form-row form-group">
                <div class="col-sm-3 label-column"><form:label path="name"
                                                               class="col-form-label">Name </form:label></div>
                <div class="col-sm-8 input-column"><form:input path="name" class="form-control" id="title-input"
                                                               type="text"></form:input></div>
            </div>
            <div class="form-row form-group">
                <div class="col-sm-3 label-column">
                    <form:label path="subjectGroup" class="col-form-label">Group </form:label></div>
                <div class="col-sm-3 input-column">
                    <form:select class="selectpicker" data-width="100%" path="subjectGroup.groupId" id="group-input" name="groupSelect">
                        <form:option disabled="disabled" selected="selected" value="">Select a group</form:option>
                        <c:forEach items="${groups}" var="group">
                            <form:option value="${group.groupId}">${group.stage} ${group.level} ${group.group}</form:option>
                        </c:forEach>
                    </form:select>
                </div>
                <c:set var="enumValues" value="<%=Department.values()%>"/>
                <div class="col-sm-2 label-column">
                    <form:label path="department" class="col-form-label">Department </form:label></div>
                <div class="col-sm-3 input-column">
                    <form:select class="selectpicker" data-width="100%" path="department" id="department-input">
                        <form:option disabled="disabled" id="selected-default" selected="selected" value="">Select a Department</form:option>
                        <c:forEach items="${enumValues}" var="enumValue">
                            <form:option value="${enumValue}"></form:option>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
            <button class="btn btn-light submit-button" type="submit" onclick="return Validate()">Create</button>
        </form:form>
    </div>
</div>


<script type="text/javascript">

    function Validate() {
        var validated = true;
        var title = document.getElementById("title-input");
        var department = document.getElementById("department-input");
        var group = document.getElementById("group-input");
        if(title.value == "")  {
            title.style.backgroundColor = "#ffd6cc";
            validated = false;
        }else title.style.backgroundColor = "#ffffffff";
        if(department.value.toString() == "")  {
            $('*[data-id="department-input"]').css("background-color","#ffd6cc","important");
            validated = false;
        }else $('*[data-id="department-input"]').css("background-color","#ffffffff","important");
        if(group.value.toString() == "")  {
            $('*[data-id="group-input"]').css("background-color","#ffd6cc","important");
            validated = false;
        }else $('*[data-id="group-input"]').css("background-color","#ffffffff","important");
        return validated;
    }
</script>

</body>

</html>