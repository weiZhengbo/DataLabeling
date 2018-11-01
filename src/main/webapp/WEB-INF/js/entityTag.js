$(document).ready(function () {

});

function uploadFile(){
    debugger;
        var form = new FormData(document.getElementById("fileUpload"));
        $.ajax({
            type: "POST",
            url:"fileUpload.action",
            data:form,
            cache: false,
            contentType: false,
            processData: false,
            async: false,
            success: function(data) {
                alert(data);
            }
        });
}