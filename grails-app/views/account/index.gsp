<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>My Account</title>
</head>

<body>
<div class="row">
    <ol class="breadcrumb">
        <li><g:link uri="/">Home</g:link></li>
        <li class="active">My Account</li>
    </ol>

    <div class="col-md-8 col-lg-offset-2">
        <g:if test='${flash.message}'>
            <p class="alert bg-info">${flash.message}</p>
        </g:if>
        <div class="page-header"><h3>User: ${user.username}
            <span class="pull-right">
                <g:if test="${user.accountLocked}">
                    <span class="label label-default">
                        <span class="glyphicon glyphicon-ban-circle" aria-label="Locked"></span>
                        Locked</span></g:if>
                <g:link class="btn btn-primary" action="changePassword">Change password</g:link>
        </div>
        <h4>Roles:</h4>
        <ul>
            <g:each in="${user.authorities}" var="role">
                <li>${role.authority}
                <g:render template="/role/role" model="[role: role, readonly: true]"/>
                </li>
            </g:each>
        </ul>
    </div>
    <g:if test="${events}">
        <hr/>

        <div class="col-md-8 col-lg-offset-2">
            <h4>Latest events:</h4>
            <g:render template="/event/events" model="[list: events]"/>
        </div>
    </g:if>
</div>
</body>
</html>