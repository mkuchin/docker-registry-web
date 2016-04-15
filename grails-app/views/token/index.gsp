<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tokens</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li class="active">Tokens</li>
        </ol>

        <div class="page-header"><h1>Tokens</h1></div>
        <table class="table table-bordered">
            <tr><th>Token name</th><th>Permissions</th></tr>
            <g:each in="${list}" var="token">
                <tr>
                    <td>
                        <g:link action="show"
                                id="${token.id}">${token.name}</g:link>
                    </td>
                    <td>
                        ${token.permissions}
                    </td>
                </tr>
            </g:each>
        </table>
    </div>

    <div class="col-md-12"><g:link class="btn btn-default" action="add">Add</g:link></div>
</div>
</body>
</html>