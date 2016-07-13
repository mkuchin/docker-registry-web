<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Status page</title>
</head>

<body>
<div class="row">
    <g:header title='App Status'>
        <li><g:link uri="/">Home</g:link></li>
        <li class="active">Status</li>
    </g:header>
    <div class="col-md-8">
        <table class="table table-bordered table-hover">
            <tr><td>Version</td><td>${version}</td></tr>
            <tr><td>Config path</td><td>${configPath}</td></tr>
            <tr><td>Auth key digest</td><td>${keyDigest}</td></tr>
        </table>

        <h4>Config</h4>
        <table class="table table-bordered table-hover">
            <g:each in="${config}" var="e">
                <tr><td>${e.key}</td><td>${e.value}</td></tr>
            </g:each>
        </table>
    </div>
</div>
</body>
</html>