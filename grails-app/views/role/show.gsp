<%@ page import="docker.registry.acl.AccessLevel" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Role ${role.authority}</title>
</head>

<body>
<div class="row">
    <g:set var="buttons">
        <span class="pull-right">
            <g:if test="${role.specialRole}">
                <small class="alert-warning">Special Role: ${role.specialRoleDescription}</small>
            </g:if>
            <g:else>
                <g:link class="btn btn-danger" action="delete" id="${role.id}">Delete Role</g:link>
            </g:else>
        </span>
    </g:set>
    <g:header title='Role: ${role.authority}${raw(buttons)}'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">Roles</g:link></li>
        <li class="active">${role.authority}</li>
    </g:header>
    <div class="col-md-8">
        <g:render template="role" model="[role: role]"/>

    </div>
</body>
</html>
