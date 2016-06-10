<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Repositories</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <ol class="breadcrumb">
            <li class="active">Home</li>
        </ol>

        <g:if test="${message}">
            <div class="alert alert-danger" role="alert">${message}</div>
        </g:if>
        <div class="page-header"><h1>Repositories</h1></div>
        <dl>
            <dt>Registry</dt>
            <dd>${grailsApplication.config.registry.name}</dd>
        </dl>
        <table class="table table-bordered">
            <tr><th>Repository</th><th>Tags</th></tr>
            <g:each in="${repos}" var="repo">
                <g:if test="${repo.tags}">
                    <tr>
                        <td>
                            <g:link action="tags"
                                    id="${repo.name.encodeAsURL()}">${repo.name}</g:link>
                        </td>
                        <td>
                            ${repo.tags}
                        </td>
                    </tr>
                </g:if>
            </g:each>
        </table>
        <g:if test="${pagination}">
            <nav>
                <ul class="pager">
                    <g:set var="prevActive" value="${params.prev != null ? '' : 'disabled'}"/>
                    <g:set var="nextActive" value="${hasNext ? '' : 'disabled'}"/>

                    <li class="${prevActive}">
                        <g:link action="index" params="[start: params.prev]" aria-label="Previous">
                            <span aria-hidden="true">&laquo; Previous</span>
                        </g:link>
                    </li>
                    <li class="${nextActive}">
                        <g:link action="index" params="[start: next, prev: prev]" aria-label="Next">
                            <span aria-hidden="true">Next &raquo;</span>
                        </g:link>
                    </li>
                </ul>
            </nav>
        </g:if>
    </div>
</div>
</body>
</html>