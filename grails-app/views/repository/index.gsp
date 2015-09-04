<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<html>
<head>
    <title>Repositories</title>
</head>

<body>
<h1>Repositories</h1>
<table border="1">
    <tr><th>Repo</th><th>Tags</th></tr>
    <g:each in="${repos}" var="repo">
        <g:if test="${repo.tags}">
            <tr>
                <td>
                    <g:link action="tags" id="${URLEncoder.encode(repo.name, 'UTF-8')}">${repo.name}</g:link>
                </td>
                <td>
                    ${repo.tags}
                </td>
            </tr>
        </g:if>
    </g:each>
</table>
</body>
</html>