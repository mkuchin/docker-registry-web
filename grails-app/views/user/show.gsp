<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User ${user.username}</title>
</head>

<body>
<g:modal id="deleteUser" title="Confirm Delete" fields="['user']">
    <p>You are about to delete user <strong id="user"></strong>.</p>

    <p>Do you want to proceed?</p>
</g:modal>
<g:set var="buttons">
    <span class="pull-right">
        <g:if test="${user.accountLocked}">
            <span class="label label-warning">
                <span class="glyphicon glyphicon-lock" aria-label="Locked" title="Account Locked"></span></span>
        </g:if>
        <g:link class="btn btn-primary" action="edit" id="${user.id}">Modify</g:link>
    <a href="#" data-user="${user.username}" class="btn btn-danger"
       data-href="${g.createLink(action: 'delete', params: [id: user.id])}"
       data-toggle="modal" data-target="#deleteUser" ${current ? 'disabled' : ''}>Delete</a></td>
    </span>
</g:set>
<div class="row">
    <g:header title='User: ${user.username}${raw(buttons)}'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">Users</g:link></li>
        <li class="active">${user.username}</li>
    </g:header>
    <g:if test='${flash.message}'>
        <p class="alert bg-${flash.error ? 'danger' : 'success'}"><g:message code="${flash.message}"
                                                                             args="[user.username, flash.role]"/></p>
    </g:if>
    <div class="col-md-6">
        <h4>Roles:</h4>
        <ul>
            <g:each in="${user.authorities}" var="role">
                <li><g:link controller="role" action="show" id="${role.id}">${role.authority}</g:link> <g:link
                        action="deleteRole" id="${role.id}"
                        params="[userId: user.id]">
                    <span class="glyphicon glyphicon-remove text-danger" aria-label="remove"></span></g:link></li>
            </g:each>

            <g:if test="${roles}">
                <g:form action="addRole" class="form-inline">

                    <g:hiddenField name="userId" value="${user.id}"/>
                    <li>
                        <g:select class="form-control" from="${roles}" name="roleId" optionKey="id"
                                  optionValue="authority" noSelection="['': '']"/>

                        <g:submitButton name="Add" class="btn btn-primary"/>
                    </li>

                </g:form>
            </g:if>
        </ul>
    </div>
    <g:if test="${events}">
        <div class="col-md-8">
            <hr/>
            <h4>Latest events:</h4>
            <g:render template="/event/events" model="[list: events]"/>
        </div>
    </g:if>
</div>
</body>
</html>
