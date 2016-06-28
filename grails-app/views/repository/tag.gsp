<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Image history</title>
</head>

<body>
<div class="row">
    <g:header title='Image history'>
        <li><g:link action="index">Home</g:link></li>
        <li><g:link action="tags" id="${params.id}">${params.id.decodeURL()}</g:link></li>
        <li class="active">${params.name}</li>
    </g:header>
    <div class="col-md-12">
        <dl>
            <dt>Image</dt>
            <dd>${registryName}/${params.id.decodeURL()}:${params.name}</dd>
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