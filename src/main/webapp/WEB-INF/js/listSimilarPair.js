$(document).ready(function () {
    $('.maintable').keydown(function(event){
        var keynum = (event.keyCode ? event.keyCode : event.which);
        if(keynum == '13'){
            var $tb = $(event.currentTarget);
            var $input = $(event.target);
            var url  = decodeURI(window.location.search.substr(1));
            var appId = getQueryString(url,'appId');
            var val = $input.val();
            var patt = /[0-1]/;
            if(!patt.test(val)){
                alert("输入值有误");
                return;
            }
            var rid = $input.parents('tr').children().first().text();

            var url1  = document.URL;
            var urlHead = url1.substring(0,url1.lastIndexOf('/'));
            var reqUrl = urlHead+"/updateState?isSimilar="+val+"&id="+rid+"&appId="+appId;
            $.get(reqUrl,function (data,status) {
                if(data){
                    var $tdList = $tb.find('input');
                    var numInput = $tdList.length;
                    var tdId = $tdList.index($input);
                    if(tdId<numInput-1){
                        var $nxtTd = $tdList.get(tdId+1);
                        $nxtTd.focus();
                    }else {
                        $input.blur();
                    }
                }else {
                    alert("输入值有误");
                }
            });
        }
    });
});


function uploadFile() {
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');
    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var head = head1.substring(0,head1.lastIndexOf('/'));
    var reqUrl = head+'/SimilarPair/fileUpload?appId='+appId;
    var formData = new FormData();
    if($('#file')[0].files.length==0){
        alert('没有选择文件');
        return;
    }
    formData.append('file', $('#file')[0].files[0]);
    $("body").mLoading();
    $.ajax({
        url:reqUrl,
        type: 'POST',
        data:formData,
        processData: false,
        cache: false,
        contentType: false,
        clearForm:true,
        async:true,
        success: function(data) {
            $("body").mLoading("hide");
            if(data==true){
                alert("文件处理成功");
                location.reload();
            }else {
                alert("文件处理失败");
            }
        }
    });
}
function getUrl(change, value) {
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');
    var dataType = getQueryString(url,'dataType');

    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var head = head1.substring(0,head1.lastIndexOf('/'));
    if(change == 'refresh'){
        location.reload();
    }else {
        var reqUrl = head+'/SimilarPair/findAll?appId='+appId+"&dataType="+dataType+"&pc="+value;
        window.location.href=reqUrl;
    }
}
function chagnePage(dataType) {
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');
    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var head = head1.substring(0,head1.lastIndexOf('/'));
    var reqUrl = head+'/SimilarPair/findAll?appId='+appId+"&dataType="+dataType;
    window.location.href=reqUrl;
}
function downLoadData(type) {
    var url = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url, 'appId');

    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var head = head1.substring(0,head1.lastIndexOf('/'));
    if(type==1){
        var reqUrl = head+'/SimilarPair/download?appId='+appId+"&dataType="+type;
    }
    window.location.href = reqUrl;
}
function getQueryString(url,name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = url.match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}


function loadFile(file) {
    $('#filename').html(file.name);
}

function keyDown(e,obj){
    var ev= window.event||e;
//13是键盘上面固定的回车键
    if (ev.keyCode == 13) {
        $(obj).next().find('i')[0].click();
    }
}