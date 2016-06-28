<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Event list</title>
</head>

<body>
<div class="row">

    <g:header title='Latest events'>
        <li><g:link uri="/">Home</g:link></li>
        <li class="active">Events</li>
    </g:header>

    <g:paginate total="${total}" action="index" max="20"/>
    <g:render template="events" model="[list: list]"/>
    <g:paginate total="${total}" action="index" max="20"/>
</div>
</body>
</html>