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
                "order": [[2, "desc"]],
                "columnDefs": [
                    {orderable: false, targets: -1}
                ]
            });

        });
    </script>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link action="index">Home</g:link></li>
            <li class="active">${params.id.decodeURL()}</li>
        </ol>

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