<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Web Registry - <g:layoutTitle/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <asset:deferredScripts/>
    <g:layoutHead/>
</head>

<body>
<!-- navbar -->

<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">

            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span
                    class="icon-bar"></span><span class="icon-bar"></span>
            </button>  <g:link class="navbar-brand" controller="repository" action="index">汇耳Docker私有镜像仓库</g:link>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

            <ul class="nav navbar-nav navbar-right">

                <sec:ifAnyGranted roles="UI_ADMIN">
                    <li>
                        <g:link controller="user">用户</g:link>
                    </li>
                    <li>
                        <g:link controller="role">角色</g:link>
                    </li></sec:ifAnyGranted>
                <sec:ifLoggedIn>
                    <li>
                        <g:link controller="event">事件</g:link>
                    </li>
                    <li>
                        <g:link controller="status">状态</g:link>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false"><strong><sec:username/><span class="caret"></span></strong></a>
                        <ul class="dropdown-menu">
                            <li><g:link controller="account" action="index">个人中心</g:link></li>
                            <li role="separator" class="divider"></li>
                            <li><g:link controller="logout" action="index">注销</g:link></li>
                        </ul>
                    </li>
                </sec:ifLoggedIn>
            </ul>

        </div>
    </div>
</nav>


<div class="container">
    <g:layoutBody/>
</div>
</body>
</html>