<div class="modal" id="${id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">${title}</h4>
            </div>

            <div class="modal-body">
                ${raw(body)}
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <a class="btn btn-danger btn-ok">Delete</a>
            </div>
        </div>
    </div>
</div>
<asset:script>
    $(document).ready(function () {
           $('#${id}').on('show.bs.modal', function (e) {
               $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
    <g:each in="${fields}" var="field">
        $(this).find('#${field}').html($(e.relatedTarget).data('${field}'));
    </g:each>
    });
});
</asset:script>