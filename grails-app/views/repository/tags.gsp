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
            <table class="table table-bordered table-hover">
                <tr><th>Id</th><th>Tag</th><th>Layers</th><th>Size</th>
                    <g:if test="${!grailsApplication.config.registry.readonly}">
                        <th>Delete</th>
                    </g:if>
                </tr>
                <g:each in="${tags}" var="tag">
                    <g:if test="${tag.exists}">
                        <tr><td>${tag.id}</td>
                            <td><g:link action="tag" params="[name: tag.name]"
                                        id="${params.id}">${tag.name}</g:link></td>
                            <td>${tag.data.fsLayers.size()}</td><td><g:formatSize value="${tag.size}"/></td>
                            <g:if test="${!grailsApplication.config.registry.readonly}">
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