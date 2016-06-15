<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Change password</title>
</head>

<body>
<div class="row">
    <g:header title='User: ${user.username}'>
        <li><g:link uri="/">Home</g:link></li>
        <li><g:link action="index">My Account</g:link></li>
        <li class="active">Change Password</li>
    </g:header>
    <div class='col-md-6'>
        <g:if test='${flash.message}'>
            <p class="alert bg-danger">${flash.message}</p>
        </g:if>
        <g:form action='updatePassword' method='POST' class='form-horizontal' autocomplete='off'>
            <div class="form-group">
                <label for='oldPassword' class="col-sm-4 control-label">Current Password</label>

                <div class="col-sm-8">
                    <input type='password' class='form-control' name='oldPassword' id='oldPassword'/>
                </div>
            </div>

            <div class="form-group">
                <label for='password' class="col-sm-4 control-label">New Password</label>

                <div class="col-sm-8">
                    <input type='password' class='form-control' name='newPassword' id='password'/>
                </div>
            </div>

            <div class="form-group">
                <label for='password2' class="col-sm-4 control-label">Confirm New Password</label>

                <div class="col-sm-8">
                    <input type='password' class='form-control' name='newPasswordRepeat' id='password2'/>
                </div>
            </div>
            <input class="btn btn-primary" type='submit' id="submit"
                   value='Change Password'/>
        </g:form>
    </div>
</div>

</body>
</html>