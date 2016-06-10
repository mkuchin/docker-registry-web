<%@ page import="docker.registry.acl.AccessLevel" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Role ${role.authority}</title>
</head>

<body>
<div class="row">
    <div class="col-md-8 col-lg-offset-2">
        <ol class="breadcrumb">
            <li><g:link uri="/">Home</g:link></li>
            <li><g:link action="index">Roles</g:link></li>
            <li class="active">${role.authority}</li>
        </ol>

        <div class="page-header"><h3>Role: ${role.authority}
            <g:if test="${role.specialRole}">
                </h3><div class="alert alert-warning" role="alert">Special Role: ${role.specialRoleDescription}</div>
            </g:if>
            <g:else>
                <span class="pull-right">
                    <g:link class="btn btn-danger" action="delete" id="${role.id}">Delete</g:link>
                </span></h3>
            </g:else>

            <g:render template="role" model="[role: role]"/>
        </div>
    </div>
</body>
</html>
