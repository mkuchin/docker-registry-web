<%@ page import="docker.registry.acl.AccessLevel" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Role ${role.authority}</title>
</head>

<body>
<g:modal id="deleteRole" title="Confirm Delete">
    <p>You are about to delete role <strong>${role.authority}</strong>.</p>

    <p>It will also revoke this role from <strong>${users.size()} users</strong>.</p>

    <p>Do you want to proceed?</p>
</g:modal>
<div class="row">
    <g:set var="buttons">
        <span class="pull-right">
        <g:if test="${role.specialRole}">
            <small class="alert-warning">Special Role: <g:message code="${role.code}"/></small>
        </g:if>
        <g:else>
            <a href="#" class="btn btn-danger"
               data-href="${g.createLink(action: 'delete', params: [id: role.id])}"
               data-toggle="modal" data-target="#deleteRole">Delete</a></td>
        </g:else>
        </span>
    </g:set>
    <g:header title='Role: ${role.authority}${raw(buttons)}'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">Roles</g:link></li>
        <li class="active">${role.authority}</li>
    </g:header>
    <g:if test='${flash.message}'>
        <p class="alert bg-success"><g:message code="${flash.message}" args="[role.authority]"/></p>
    </g:if>
    <div class="col-md-8">
        <g:render template="role" model="[role: role]"/>
    </div>

    <div class="col-md-3 col-lg-offset-1">
        <g:if test="${users}">
            <h4>Granted to:</h4>
            <ul>
                <g:each in="${users}" var="user">
                    <li>${user.username}</li>
                </g:each>
            </ul>
        </g:if>
    </div>
</body>
</html>
