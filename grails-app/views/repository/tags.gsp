<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tags</title>
    <script>
        $(document).ready(function () {
            $('#main').DataTable({
                "paging": false,
                "searching": false,
                "info": false,
                "order": [[2, "desc"]],
                "columnDefs": [
                    {orderable: false, targets: -1}
                ]
            });
        });
    </script>
</head>

<body>
<g:modal id="deleteTag" title="Confirm Delete" fields="['tag', 'id']">
    <p>You are about to delete tag <strong id="tag"></strong> and all images with id <strong id="id"></strong>.</p>

    <p>Do you want to proceed?</p>
</g:modal>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link action="index">Home</g:link></li>
            <li class="active">${params.id.decodeURL()}</li>
        </ol>
        <g:if test="${flash.deleteAction}">
            <div class="alert alert-${flash.success ? 'success' : 'danger'}" role="alert">
                ${raw(flash.message)}
            </div>
        </g:if>
        <div class="page-header">
            <h1>Tags</h1>
        </div>
        <dl>
            <dt>Repository</dt>
            <dd>${grailsApplication.config.registry.name}/${params.id.decodeURL()}</dd>
        </dl>

        <div class="table-responsive">
            <table class="table table-bordered table-hover" id="main">
                <thead>
                <tr><th>Id</th><th>Tag</th><th>Created</th><th>Layers</th><th>Size</th>
                    <g:if test="${!readonly}">
                        <th>Delete</th>
                    </g:if>
                </tr>
                </thead>
                <tbody>
                <g:each in="${tags}" var="tag">
                    <g:if test="${tag.exists}">
                        <tr><td>${tag.id}</td>
                            <td><g:link action="tag" params="[name: tag.name]"
                                        id="${params.id}">${tag.name}</g:link></td>
                            <td data-sort="${tag.unixTime}"><abbr title="${tag.createdStr}"><prettytime:display
                                    date="${tag.created}"/></abbr></td>
                            <td>${tag.count}</td>
                            <td data-sort="${tag.size}"><g:formatSize value="${tag.size}"/></td>
                            <g:if test="${!grailsApplication.config.registry.readonly}">
                                <td>
                                    <a href="#" data-tag="${tag.name}" data-id="${tag.id}"
                                       data-href="${g.createLink(action: 'delete', params: [id: tag.name, name: params.id])}"
                                       data-toggle="modal" data-target="#deleteTag">Delete</a></td>
                            <g:if test="${!readonly}">
                            <td><g:link action="delete" params="[name: params.id]" id="${tag.name}">Delete</g:link></td>
                            </g:if>
                        </tr>
                    </g:if>
                </g:each>
            </table>
        </div>
    </div>
</div>
</body>
</html>