<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add User</title>
</head>
<body>
<div class="row">
    <g:header title='Create User'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">Users</g:link></li>
        <li class="active">Add User</li>
    </g:header>
    <g:if test='${flash.errors}'>
        <g:each in="${flash.errors.allErrors}" var="error">
            <p class="alert bg-danger"><g:message error="${error}"/></p>
        </g:each>
    </g:if>
    <div class="col-md-6">
        <g:form action="create">
                <div class="form-group">
                    <label for="username">Username</label> <g:textField name="username" autocomplete="off"
                                                                        class="form-control"
                                                                        required=""/>
                </div>
            <input type="password" name="password" class="hidden"/>


            <div class="form-group">
                    <label for="password">Password</label> <g:passwordField name="password" autocomplete="off"
                                                                            class="form-control"
                                                                            required=""/>
                </div>

                <div class="checkbox">
                    <label>
                        <g:checkBox name="accountLocked"/>
                        Locked
                    </label>
                </div>
                <g:submitButton name="Create" class="btn btn-primary"/>
        </g:form>
    </div>
</div>
</body>
</html>