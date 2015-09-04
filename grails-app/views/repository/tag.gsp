<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<html>
<head>
    <title>Image history</title>
</head>

<body>
<h1>Image history</h1>
<table border="1">
    <tr><th>image</th><th>cmd</th><th>size</th></tr>
    <g:each in="${history}" var="image">
        <tr><td>${image.id.substring(0, 11)}</td>
            <td>${raw(image.container_config.Cmd.last().replaceAll('&&', '&&<br>'))}</td>
            <td><g:formatSize value="${image.Size}"/></td></tr>
    </g:each>
</table>

<h2>Total size: <g:formatSize value="${totalSize}"/></h2>
</body>
</html>