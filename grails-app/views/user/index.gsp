<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Users</title>
</head>

<body>
<div class="row">
    <g:header title='Users'>
        <li><g:link uri="/">Home</g:link></li>
        <li class="active">Users</li>
    </g:header>
    <g:if test='${flash.message}'>
        <p class="alert bg-success"><g:message code="${flash.message}" args="[flash.username]"/></p>
    </g:if>
    <div class="col-md-6">
        <table class="table table-bordered">
            <tr><th>Username</th><th>Roles</th></tr>
            <g:each in="${list}" var="user">
                <tr>
                    <td style="vertical-align: middle;">
                        <strong><g:link action="show"
                                        id="${user.id}">${user.username}</g:link></strong>
                    </td>
                    <td>
                        <g:each in="${user.authorities}" var="role">
                            <g:link action="show" controller="role" id="${role.id}"
                                    class="btn btn-default">${role.authority}</g:link>
                        </g:each>
                    </td>
                </tr>
            </g:each>
        </table>
        <g:link class="btn btn-primary" action="add">Create new user</g:link>
    </div>
</div>
</body>
</html>