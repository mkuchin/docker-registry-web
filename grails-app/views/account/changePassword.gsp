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

        <g:if test='${flash.message}'>
            <p class="alert bg-danger"><g:message code="${flash.message}"/></p>
        </g:if>
    <div class='col-md-6'>
        <g:form action='updatePassword' method='POST' autocomplete='off'>
            <div class="form-group">
                <label for='oldPassword'>Current password</label>

                    <input type='password' class='form-control' name='oldPassword' id='oldPassword'/>
            </div>

            <div class="form-group">
                <label for='password'>New password</label>

                    <input type='password' class='form-control' name='newPassword' id='password'/>
            </div>

            <div class="form-group">
                <label for='password2'>Confirm new password</label>

                    <input type='password' class='form-control' name='newPasswordRepeat' id='password2'/>
            </div>
            <input class="btn btn-primary" type='submit' id="submit"
                   value='Change Password'/>
        </g:form>
</div>

</body>
</html>