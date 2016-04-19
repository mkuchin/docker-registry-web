<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User ${user.username}</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li><g:link action="index">users</g:link></li>
            <li class="active">${user.username} roles</li>
        </ol>

        <div class="page-header"><h1>User: ${user.username}</h1></div>
        <g:link class="btn btn-default" action="edit" id="${user.id}">Edit</g:link> <g:link class="btn btn-default"
                                                                                            action="delete">Delete</g:link>
        <h4>Roles:</h4>
        <ul>
            <g:each in="${user.authorities}" var="role">
                <li>${role.authority} [<g:link action="deleteRole" id="${role.id}"
                                               params="[userId: user.id]">x</g:link>]</li>
            </g:each>
        </ul>
        <g:form action="addRole">
            <g:hiddenField name="userId" value="${user.id}"/>
            <div class="form-group col-md-6">
                <g:select class="form-control" from="${roles}" name="roleId" optionKey="id" optionValue="authority"/>
                <g:submitButton name="Add" class="btn btn-default"/>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>
