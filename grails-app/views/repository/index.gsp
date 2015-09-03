<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<html>
<head>
    <title>Repositories</title>
</head>

<body>
<h1>Repositories</h1>
<g:each in="${repos}" var="repo">
    <g:link action="tags" id="${repo}">${repo}</g:link><br/>
</g:each>
</body>
</html>