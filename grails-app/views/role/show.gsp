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
            <span class="pull-right">
                <g:link class="btn btn-danger" onclick="return confirm('Are you sure to delete this role?')"
                        action="delete" id="${role.id}">Delete</g:link>
            </span>
        </h3></div>
        <h4>Access control list</h4>
        <table class="table-bordered table table-hover">
            <tr><th>IP</th><th>Repository</th><th>Permissions</th><th>Comments</th><th></th></tr>
            <g:each in="${acls}" var="acl">
                <tr>
                    <td>${acl.ip}</td>
                    <td>${acl.name}</td>
                    <td>${acl.level.actions.join(', ')}</td>
                    <td>${acl.comment}</td>
                    <td><g:link action="deleteAcl" id="${acl.id}"
                                onclick="return confirm('Are you sure to delete this action?')"
                                params="[roleId: role.id]"><span class="glyphicon glyphicon-remove text-danger"
                                                                 aria-label="remove"></span>
                    </g:link></td>
                </tr>
            </g:each>
            <g:form action="addAcl">
                <g:hiddenField name="id" value="${role.id}"/>
                <tr>
                    <td><g:textField class="form-control" name="ip"/></td>
                    <td><g:textField class="form-control" name="name"/></td>
                    <td><g:select class="form-control" from="${AccessLevel.values()}"
                                  optionValue="${{ it.actions.join(', ') }}" name="level"/></td>
                    <td><g:textField class="form-control" name="comment"/></td>
                    <td><g:submitButton name="Add" class="btn btn-primary"/></td>
                </tr>
            </g:form>
        </table>
    </div>
</div>
</body>
</html>
