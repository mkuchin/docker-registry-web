<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Roles</title>
</head>

<body>
<div class="row">
    <div class="col-md-8 col-lg-offset-2">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li class="active">Roles</li>
        </ol>

        <div class="page-header"><h3>Roles</h3></div>
        <%-- todo: use list group http://getbootstrap.com/components/#list-group-linked --%>
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
        <g:link class="btn btn-primary" action="add">Add</g:link>
    </div>
</div>
</body>
</html>