<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>My Account</title>
</head>

<body>
<div class="row">
    <g:set var="buttons">
        <span class="pull-right"><g:link class="btn btn-primary" action="changePassword">Change password</g:link></span>
    </g:set>
    <g:header title='User: ${user.username}${raw(buttons)}'>
        <li><g:link uri="/">Home</g:link></li>
        <li class="active">My Account</li>
    </g:header>

    <div class="col-md-8">
        <g:if test='${flash.message}'>
            <p class="alert bg-success">${flash.message}</p>
        </g:if>

        <h3>Roles:</h3>

        <ul>
            <g:each in="${user.authorities}" var="role">
                <li>${role.authority}
                <g:render template="/role/role" model="[role: role, readonly: true]"/>
                </li>
            </g:each>
        </ul>
    </div>
    <g:if test="${events}">
        <div class="col-md-8">
            <h3>Latest events:</h3>
            <g:render template="/event/events" model="[list: events]"/>
        </div>
    </g:if>
</div>
</body>
</html>