<html>
<head>
    <title><g:message code="springSecurity.login.title"/></title>
</head>

<body>
<div class="row">
    <div class='col-md-4 col-md-offset-4'>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title"><g:message code="springSecurity.login.header"/></h3>
            </div>

            <div class="panel-body">
                <g:if test='${flash.message}'>
                    <p class="alert bg-danger">${flash.message}</p>
                </g:if>
                <form action='${postUrl}' method='POST' id='loginForm' class='form-horizontal' autocomplete='off'>
                    <g:hiddenField name="spring-security-redirect" value="${params.target}"/>
                    <div class="form-group">
                        <label for='username' class="col-sm-4 control-label"><g:message
                                code="springSecurity.login.username.label"/></label>

                        <div class="col-sm-8">
                            <input type='text' class='form-control' name='j_username' id='username'/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for='password' class="col-sm-4 control-label"><g:message
                                code="springSecurity.login.password.label"/></label>

                        <div class="col-sm-8">
                            <input type='password' class='form-control' name='j_password' id='password'/>
                        </div>
                    </div>
                    <input class="btn btn-primary" type='submit' id="submit"
                           value='${message(code: "springSecurity.login.button")}'/>
                </form>
            </div>
        </div>
    </div>
</div>
<script type='text/javascript'>
    (function () {
        document.forms['loginForm'].elements['j_username'].focus();
    })();
</script>
</body>
</html>
