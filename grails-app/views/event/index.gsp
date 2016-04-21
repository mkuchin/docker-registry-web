<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Event list</title>
</head>

<body>
<h3>Latest events</h3>
<g:paginate total="${total}" action="index" max="20"/>
<table class="table table-bordered">
    <tr><th>User</th><th>Repo:Tag</th><th>Action</th><th>Time</th><th>IP</th></tr>
    <g:each in="${list}" var="event">
        <tr>
            <td>${event.user.username}</td><td>${event.repo}:${event.tag}</td>
            <td>${event.action}</td><td><g:formatDate date="${event.time}"
                                                      format="yyyy-MM-dd HH:mm"/></td><td>${event.ip}</td>
        </tr>
    </g:each>
</table>
<g:paginate total="${total}" action="index" max="20"/>
</body>
</html>