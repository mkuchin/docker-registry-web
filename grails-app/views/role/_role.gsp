<%@ page import="docker.registry.acl.AccessLevel" %>
<g:if test="${!role.specialRole}">
    <h4>Access control list</h4>
    <table class="table-bordered table table-hover">
        <tr><th class="text-center">IP</th><th class="text-center">Repository</th><th
                class="text-center">Permissions</th><th class="text-center">Comments</th>
            <g:if test="${!readonly}">
                <th></th>
            </g:if>
        </tr>
        <g:each in="${role.acls}" var="acl">
            <tr>
                <td>${acl.ip}</td>
                <td>${acl.name}</td>
                <td>${acl.level.actions.join(', ')}</td>
                <td>${acl.comment}</td>
                <g:if test="${!readonly}">
                    <td><g:link action="deleteAcl" id="${acl.id}"
                                params="[roleId: role.id]"><span class="glyphicon glyphicon-remove text-danger"
                                                                 aria-label="remove"></span>
                    </g:link></td>
                </g:if>
            </tr>
        </g:each>
        <g:if test="${!readonly}">
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
        </g:if>
    </table>
</g:if>