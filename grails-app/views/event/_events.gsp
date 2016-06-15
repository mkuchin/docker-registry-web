<table class="table table-bordered">
    <tr><th>User</th><th>Repo:Tag</th><th>Action</th><th>Time</th><th>IP</th></tr>
    <g:each in="${list}" var="event">
        <tr>
            <td>${event.username}</td><td>${event.repo}:${event.tag}</td>
            <td>${event.action}</td><td><g:formatDate date="${event.time}"
                                                      format="yyyy-MM-dd HH:mm"/></td><td>${event.ip}</td>
        </tr>
    </g:each>
</table>