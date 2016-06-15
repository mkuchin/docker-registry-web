<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User ${user.username}</title>
</head>

<body>
<div class="row">
    <g:header title='User: ${user.username}'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">Users</g:link></li>
        <li><g:link action="show" params="[id: user.id]">${user.username}</g:link></li>
        <li class="active">Edit</li>
    </g:header>
    <div class="col-md-6">
        <g:form action="update" autocomplete="off">
            <g:hiddenField name="id" value="${user.id}"/>
            <div class="form-group">
                <label for="password">Password</label> <g:passwordField name="password" class="form-control"
                                                                        placeholder="new password"
                                                                        onfocus=""/>
            </div>

            <div class="checkbox">
                <label>
                    <g:checkBox name="accountLocked" value="${user.accountLocked}"/>
                    Locked
                </label>
            </div>
            <g:submitButton name="Save" class="btn btn-primary"/>
        </g:form>
    </div>
</div>
</body>
</html>
