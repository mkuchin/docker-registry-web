<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Roles</title>
</head>

<body>
<div class="row">
    <ol class="breadcrumb">
        <li><g:link uri="/">Home</g:link></li>
        <li class="active">Roles</li>
    </ol>

    <div class="page-header"><h3>Roles</h3></div>

    <div class="col-md-8 col-lg-offset-2">
        <div class="list-group">

            <g:each in="${roles}" var="role">

                <g:link class="list-group-item" action="show" id="${role.id}">${role.authority}</g:link>

            </g:each>
        </div>
        <g:link class="btn btn-primary" action="add">Add</g:link>
    </div>
</div>
</body>
</html>