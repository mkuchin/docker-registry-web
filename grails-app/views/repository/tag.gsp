<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Image history</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li><g:link action="index">Home</g:link></li>
            <li><g:link action="tags" id="${params.id}">${params.id.decodeURL()}</g:link></li>
            <li class="active">${params.name}</li>
        </ol>

        <div class="page-header"><h1>Image history</h1></div>
        <dl>
            <dt>Image</dt>
            <dd>${grailsApplication.config.registry.name}/${params.id.decodeURL()}:${params.name}</dd>
        </dl>

        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <tr><th>Image</th><th>Cmd</th><th>Size</th></tr>
                <g:each in="${history}" var="image">
                    <tr><td>${image.id}</td>
                        <td>${raw(image.cmd)}</td>
                        <td class="text-nowrap"><g:formatSize value="${image.size}"/></td></tr>
                </g:each>
            </table>
        </div>
        <h5>Total size: <g:formatSize value="${totalSize}"/></h5>
    </div>
</div>
</body>
</html>