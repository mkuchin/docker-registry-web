<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User ${user.username}</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li><g:link action="index">Users</g:link></li>
            <li class="active">Edit ${user.username}</li>
        </ol>

        <div class="page-header"><h1>User: ${user.username}</h1></div>
        <g:form action="update">
            <g:hiddenField name="id" value="${user.id}"/>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="password">Password</label> <g:passwordField name="password" class="form-control"/>
                </div>

                <div class="checkbox">
                    <label>
                        <g:checkBox name="accountLocked" value="${user.accountLocked}"/>
                        Locked
                    </label>
                </div>
                <g:submitButton name="Save" class="btn btn-default"/>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>
