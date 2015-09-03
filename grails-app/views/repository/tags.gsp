<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<html>
<head>
    <title>Tags</title>
</head>

<body>
<h1>Tags</h1>
<table border="1">
    <tr><th>Tag</th><th>Layers</th><th>Size</th><th>Delete</th></tr>
    <g:each in="${tags}" var="tag">
        <g:if test="${tag.found}">
        <tr><td><g:link action="tag" params="[name: params.id]" id="${tag.name}">${tag.name}</g:link></td>
            <td>${tag.data.fsLayers?.size()}</td><td>${tag.size}</td>
            <td><g:link action="delete" params="[name: params.id]" id="${tag.name}">Delete</g:link></td>
        </tr>
        </g:if>
    </g:each>
</table>
</body>
</html>