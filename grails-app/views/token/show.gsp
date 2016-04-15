<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Token ${token.name}</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li><g:link action="index">Tokens</g:link></li>
            <li class="active">token ${token.name}</li>

        </ol>

        <div class="page-header"><h1>Token ${token.name}</h1></div>
        <dl class="dl-horizontal">
            <dt>Token id</dt>
            <dd>${token.id}</dd>
            <dt>Permissions</dd>
            <dd>${token.permissions}</dd>
        </dl>
        <h4>Allowed repositories:</h4>
        <table class="table table-bordered">
            <tr><th>Repository</th></tr>
            <g:each in="${repos}" var="repo">
                <tr>
                    <td>
                        <g:link controller="repository" action="tags"
                                id="${repo.repository.name}">${repo.repository.name}</g:link>
                    </td>
                </tr>
            </g:each>
        </table>
    </div>
</div>
</body>
</html>
</body>
</html>