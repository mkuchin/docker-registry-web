<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Event list</title>
</head>

<body>
<h3>Latest events</h3>
<g:paginate total="${total}" action="index" max="20"/>
<g:render template="events" bean="[list: list]"/>
<g:paginate total="${total}" action="index" max="20"/>
</body>
</html>