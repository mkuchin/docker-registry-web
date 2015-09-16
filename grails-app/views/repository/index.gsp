<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Repositories</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li class="active">Home</li>
        </ol>

        <div class="page-header"><h1>Repositories</h1></div>
        <dl>
            <dt>Registry</dt>
            <dd>${grailsApplication.config.registry.name}</dd>
        </dl>
        <table class="table table-bordered">
            <tr><th>Repository</th><th>Tags</th></tr>
            <g:each in="${repos}" var="repo">
                <g:if test="${repo.tags}">
                    <tr>
                        <td>
                            <g:link action="tags"
                                    id="${repo.name.encodeAsURL()}">${repo.name}</g:link>
                        </td>
                        <td>
                            ${repo.tags}
                        </td>
                    </tr>
                </g:if>
            </g:each>
        </table>
    </div>
</div>
</body>
</html>