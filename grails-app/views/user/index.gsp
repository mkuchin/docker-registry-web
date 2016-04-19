<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Users</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li class="active">Users</li>
        </ol>

        <div class="page-header"><h1>Users</h1></div>
        <table class="table table-bordered">
            <tr><th>Username</th><th>Roles</th></tr>
            <g:each in="${list}" var="user">
                <tr>
                    <td>
                        <g:link action="show"
                                id="${user.id}">${user.username}</g:link>
                    </td>
                    <td>
                        ${user.authorities*.authority.join(', ')}
                    </td>
                </tr>
            </g:each>
        </table>
    </div>

    <div class="col-md-12"><g:link class="btn btn-default" action="add">Add</g:link></div>
</div>
</body>
</html>