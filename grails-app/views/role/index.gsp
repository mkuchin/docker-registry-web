<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Roles</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li class="active">Roles</li>
        </ol>

        <div class="page-header"><h1>Roles</h1></div>
        <table class="table table-bordered">
            <tr><th>Role</th></tr>
            <g:each in="${roles}" var="role">
                <tr>
                    <td>
                        <g:link action="show" id="${role.id}">${role.authority}</g:link>
                    </td>
                </tr>
            </g:each>
        </table>
    </div>

    <div class="col-md-12"><g:link class="btn btn-default" action="add">Add</g:link></div>
</div>
</body>
</html>