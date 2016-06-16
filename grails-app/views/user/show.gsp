<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User ${user.username}</title>
</head>

<body>
<g:set var="buttons">
    <span class="pull-right">
        <g:if test="${user.accountLocked}">
            <span class="label label-warning">
                <span class="glyphicon glyphicon-lock" aria-label="Locked" title="Account Locked"></span></span>
        </g:if>
        <g:link class="btn btn-primary" action="edit" id="${user.id}">Modify</g:link>
        <a class="btn btn-danger"
           href="${g.createLink(action: 'delete', id: user.id)}" ${current ? 'disabled' : ''}>Delete</a>
    </span>
</g:set>
<div class="row">
    <g:header title='User: ${user.username}${raw(buttons)}'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">Users</g:link></li>
        <li class="active">${user.username}</li>
    </g:header>
    <g:if test='${flash.message}'>
        <p class="alert bg-success">${flash.message}</p>
    </g:if>
    <div class="col-md-6">
        <h4>Roles:</h4>
        <ul>
            <g:each in="${user.authorities}" var="role">
                <li>${role.authority} <g:link action="deleteRole" id="${role.id}"
                                              params="[userId: user.id]">
                    <span class="glyphicon glyphicon-remove text-danger" aria-label="remove"></span></g:link></li>
            </g:each>

            <g:if test="${roles}">
                <g:form action="addRole" class="form-inline">

                    <g:hiddenField name="userId" value="${user.id}"/>
                    <li>
                        <g:select class="form-control" from="${roles}" name="roleId" optionKey="id"
                                  optionValue="authority"/>

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
