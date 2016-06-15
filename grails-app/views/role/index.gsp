<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Roles</title>
</head>

<body>
<div class="row">
    <g:header title='Roles'>
        <li><g:link uri="/">Home</g:link></li>
        <li class="active">Roles</li>
    </g:header>
    <div class="col-md-6">
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