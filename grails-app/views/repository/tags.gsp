<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tags</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link action="index">Home</g:link></li>
            <li class="active">${params.id}</li>
        </ol>

        <div class="page-header">
            <h1>Tags</h1>
        </div>
        <dl>
            <dt>Repository</dt>
            <dd>${grailsApplication.config.registry.name}/${params.id}</dd>
        </dl>

        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <tr><th>Id</th><th>Tag</th><th>Layers</th><th>Size</th><th>Delete</th></tr>
                <g:each in="${tags}" var="tag">
                    <g:if test="${tag.exists}">
                        <tr><td>${tag.id}</td>
                            <td><g:link action="tag" params="[name: params.id]"
                                        id="${tag.name}">${tag.name}</g:link></td>
                            <td>${tag.data.fsLayers.size()}</td><td><g:formatSize value="${tag.size}"/></td>
                            <td><g:link action="delete" params="[name: params.id]" id="${tag.name}">Delete</g:link></td>
                        </tr>
                    </g:if>
                </g:each>
            </table>
        </div>
    </div>
</div>
</body>
</html>