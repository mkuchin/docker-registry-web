<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add User</title>
</head>
<body>
<div class="row">
    <div class="col-md-8 col-lg-offset-2">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li><g:link action="index">Users</g:link></li>
            <li class="active">Add User</li>
        </ol>

        <div class="page-header"><h3>Create User</h3>
        </div>
        <g:form action="create">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="username">Username</label> <g:textField name="username" class="form-control"
                                                                        required=""/>
                </div>

                <div class="form-group">
                    <label for="password">Password</label> <g:passwordField name="password" class="form-control"
                                                                            required=""/>
                </div>

                <div class="checkbox">
                    <label>
                        <g:checkBox name="accountLocked"/>
                        Locked
                    </label>
                </div>
                <g:submitButton name="Create" class="btn btn-primary"/>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>