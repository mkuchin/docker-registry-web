<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Users</title>
</head>

<body>
<div class="row">
    <div class="col-md-8 col-lg-offset-2">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li class="active">Users</li>
        </ol>

        <div class="page-header"><h3>Users</h3></div>
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
        <g:link class="btn btn-primary" action="add">Add</g:link>
    </div>
</div>
</body>
</html>