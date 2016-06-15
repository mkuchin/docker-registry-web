<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add role</title>
</head>

<body>
<div class="row">
    <g:header title='Add Role'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">Roles</g:link></li>
        <li class="active">Add Role</li>
    </g:header>
    <div class="col-md-6">
        <g:form action="create">
                <div class="form-group">
                    <label for="role">Role name</label> <g:textField name="role" class="form-control" required=""/>
                </div>
                <g:submitButton name="Create" class="btn btn-primary"/>
        </g:form>
    </div>
</div>
</body>
</html>
