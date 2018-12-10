$(document).ready(function () {
    var url  = decodeURI(window.location.search.substr(1));
    var clickwordId = getQueryString(url,'clickwordId');
    $('#myModal').on('show.bs.modal', function (event) {
        var clickedId = event.relatedTarget.parentNode.parentNode.childNodes[1].textContent;
        var clickedText =event.relatedTarget.parentNode.parentNode.childNodes[3].textContent;
        $('#clickText').text(clickedText);
        $('#clickId').text(clickedId);
    });
    $('#sid'+clickwordId).css('background-color','bisque');

    $('.maintable').keydown(function(event){
        var keynum = (event.keyCode ? event.keyCode : event.which);
        if(keynum == '13'){
            var $tb = $(event.currentTarget);
            var $input = $(event.target);
            var url  = decodeURI(window.location.search.substr(1));
            var appId = getQueryString(url,'appId');
            var sid = $input.val();
            var rid = $input.parents('tr').children().first().text();

            var url1  = document.URL;
            var head1 = url1.substring(0,url1.lastIndexOf('/'));
            var head = head1.substring(0,head1.lastIndexOf('/'));
            var reqUrl = head+"/RecordController/addRecordClass?rid="+rid+"&sid="+sid+"&appId="+appId;
            $.get(reqUrl,function (data,status) {
                if(data){
                    var $td = $input.parents('td');
                    reqUrl = head+"/RecordController/selectRecordClassById?sid="+sid;
                    $.get(reqUrl,function (data1) {
                        $td.html(data1);
                    });
                    $td.next().html("<i class='glyphicon glyphicon-remove' onclick='javascript:similarTagRemove("+rid+",this)'></i>");
                    var $tdList = $tb.find('input');
                    var numInput = $tdList.length;
                    var tdId = $tdList.index($input);
                    if(tdId<numInput-1){
                        $nxtTd = $tdList.get(tdId+1);
                        $nxtTd.focus();
                    }
                }else {
                    alert("输入编号有误");
                }
            });
        }
    });

    $('.uploadFile').click(function () {
        var url  = decodeURI(window.location.search.substr(1));
        var appId = getQueryString(url,'appId');
        var url1  = document.URL;
        var head1 = url1.substring(0,url1.lastIndexOf('/'));
        var head = head1.substring(0,head1.lastIndexOf('/'));
        var reqUrl = head+'/UploadHandleController/uploadFile?appId='+appId;
        var formData = new FormData();
        formData.append('file', $('#file')[0].files[0]);
        $.ajax({
            url:reqUrl,
            type: 'POST',
            data:formData,
            processData: false,
            cache: false,
            contentType: false,
            clearForm:true,
        }).done(function(res) {
            alert('文件处理成功');
            getUrl("refresh",'no');
        }).fail(function (res) {
        });
    });
});
function downLoadData(type) {
    var url = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url, 'appId');

    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var head = head1.substring(0,head1.lastIndexOf('/'));
    if (type == '1') {
        var clickwordId = getQueryString(url, 'clickwordId');
        if (clickwordId == null || clickwordId == 'null' || clickwordId == '') {
            alert('当前没有选择相似记录范本');
            return;
        }
        var reqUrl = head + '/DownLoadZipFileController/downLoadsimilarFile?appId=' + appId + '&downType=1' + '&clickwordId=' + clickwordId;
    } else if (type == '2') {
        var keyword = getQueryString(url, 'keyword');
        if (keyword == null || keyword == 'null' || keyword == '') {
            alert('当前没有选择搜索的相似记录范本');
            return;
        }
        var reqUrl = head + '/DownLoadZipFileController/downLoadsimilarFile?appId=' + appId + '&downType=2' + '&keyword=' + keyword;
    } else if (type == '3') {
        var reqUrl = head + '/DownLoadZipFileController/downLoadsimilarFile?appId=' + appId + '&downType=3';
    }
    window.location.href = reqUrl;
}
function drag(event) {
    event.dataTransfer.setData("Text",event.target.id);
    event.dataTransfer.dropEffect = "copy";
}
function drop_handler(event) {
    debugger;
    event.preventDefault();
    var tagName = event.target.tagName;
    var $td = null;
    if(tagName=='TD'){
        $td = $(event.target);
    }else {
        var $input = $(event.target);
        $td = $input.parents('td');
    }
    var tr = event.dataTransfer.getData("Text");
    var similarId = $('#'+tr).children().eq(0).text();
    var data = $('#'+tr).children().eq(1).text();
    var rid = $td.parents('tr').children().first().text();
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');
    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var head = head1.substring(0,head1.lastIndexOf('/'));
    var reqUrl = head+"/RecordController/addRecordClass?rid="+rid+"&sid="+similarId+"&appId="+appId;
    $.get(reqUrl,function (data1,status) {
        if(data1){
            $td.html(data);
            $td.next().html("<i class='glyphicon glyphicon-remove' onclick='javascript:similarTagRemove("+rid+",this)'></i>");
        }else {
            alert("输入编号有误");
        }
    });
}
function dragover_handler(event) {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
}
function getUrl(change,value) {
    var url  = decodeURI(window.location.search.substr(1));
    var url1  = document.URL;
    var head = url1.substring(0,url1.indexOf('?'));

    var pc = getQueryString(url,'pc');
    var clickwordId = getQueryString(url,'clickwordId');
    var keyword = getQueryString(url,'keyword');
    var noHandledWord = getQueryString(url,'noHandledWord');
    var appId = getQueryString(url,'appId');
    var dataType = getQueryString(url,'dataType');
    var reqUrl = head;
    if (change=='pc'){
        if(dataType=='dealed'){
            reqUrl=reqUrl+'?pc='+value;
            reqUrl=reqUrl+'&clickwordId='+clickwordId+'&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&refresh=yes'+'&noHandledWord='+noHandledWord;
        }else {
            reqUrl=reqUrl+'?pc='+value;
            reqUrl=reqUrl+'&clickwordId='+clickwordId+'&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&refresh=no'+'&noHandledWord='+noHandledWord;
        }
    }else if(change=='clickwordId'){
        reqUrl=reqUrl+'?clickwordId='+value;
        reqUrl=reqUrl+'&pc=1'+'&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&refresh=no'+'&noHandledWord='+noHandledWord;
    }else if(change=='keyword'){
        var $i=$(value);
        var txt = $i.parents('span').siblings('input').val();
        reqUrl=reqUrl+'?keyword='+txt;
        reqUrl=reqUrl+'&pc='+pc+'&clickwordId='+clickwordId+"&appId="+appId+"&dataType="+dataType+'&refresh=no'+'&noHandledWord='+noHandledWord;
    }else if(change=='dataType'){
        reqUrl=reqUrl+'?dataType='+value;
        reqUrl=reqUrl+"&appId="+appId+'&refresh=yes';
    }else if (change=='refresh'){
        reqUrl=reqUrl+'?refresh='+value;
        reqUrl=reqUrl+'&pc='+pc+'&clickwordId='+clickwordId+'&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&noHandledWord='+noHandledWord;
    }else if (change=='noHandledWord'){
        if(dataType=='dealed') {
            pc=1;
        }
        var $i=$(value);
        var txt = $i.parents('span').siblings('input').val();
        reqUrl=reqUrl+'?noHandledWord='+txt;
        reqUrl=reqUrl+'&pc='+pc+'&clickwordId='+clickwordId+"&appId="+appId+"&dataType="+dataType+'&refresh=yes'+'&keyword='+keyword;
    }
    window.location.href=reqUrl
}
function getQueryString(url,name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = url.match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

function changeChoosed(that) {
    var $li = $(that);
    var CCId = $li.attr('id').substring(2);
    var CCText = $li.find('a').text();
    $li.parent().siblings('button').html('<span id="CCText">'+CCText+'</span>\n' +
        '\t\t\t\t\t\t\t\t\t\t<span id="CCid" style="display: none">'+CCId+'</span>\n' +
        '\t\t\t\t\t\t\t\t\t\t<span class="caret"></span>');
}
function confimClick() {
    var CCid = $('#CCid').text();
    var CCText = $('#CCText').text();
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');
    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var urlHead = head1.substring(0,head1.lastIndexOf('/'));
    if (CCid==null||CCid==''){
        alert('请选择相似记录范本');
    }else {
        var rid = $('#clickId').text();
        var reqUrl = urlHead+"/RecordController/addRecordClass?rid="+rid+"&sid="+CCid+"&appId="+appId;
        $.get(reqUrl,function (data,status) {
            if(data){
                $('#myModal').modal('hide')
                $('#rr'+rid).parents('td').siblings('.sclass').text(CCText);
                $('#rr'+rid).parents('td')[0].innerHTML="<i class='glyphicon glyphicon-remove' onclick='javascript:similarTagRemove("+rid+",this)'></i>";
            }else {
                alert("输入编号有误");
            }
        });
    }
}
function similarTagRemove(rid,that) {
    var $i = $(that);
    var url1  = document.URL;
    var head1 = url1.substring(0,url1.lastIndexOf('/'));
    var urlHead = head1.substring(0,head1.lastIndexOf('/'));
    var reqUrl = urlHead+"/RecordController/removeRecordClass?rid="+rid;
    $.get(reqUrl,function (data,status) {
        if(data){
            $i.parents('td').siblings('.sclass').html("<input type=\"number\" placeholder=\"请输入编号\"/>");
            $i.parents('td')[0].innerHTML="<i class='glyphicon glyphicon-plus' id='rr"+rid+"' data-toggle='modal' data-target='#myModal'></i>";
        }
    });
}
function similarTagDelete(sid,similarclass) {
    var r=confirm("确认删除相似记录范本："+similarclass+"吗？");
    if (r==true) {
        var url1  = document.URL;
        var url  = decodeURI(window.location.search.substr(1));
        var clickwordId = getQueryString(url,"clickwordId");
        var head1 = url1.substring(0,url1.lastIndexOf('/'));
        var urlHead = head1.substring(0,head1.lastIndexOf('/'));
        var reqUrl = urlHead+"/RecordController/deleteRecordClass?sid="+sid;
        $.get(reqUrl,function (data,status) {
            if(clickwordId==sid){
                getUrl('clickwordId',"");
            }else {
                getUrl("refresh",'no');
            }
        });
    }
}
function addClass() {
    var className = prompt("请输入要新增的相似记录范本","");
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,"appId");
    if(className!=null && className!=""){
        var url1  = document.URL;
        var head1 = url1.substring(0,url1.lastIndexOf('/'));
        var urlHead = head1.substring(0,head1.lastIndexOf('/'));
        var reqUrl = urlHead+"/RecordController/addnewRecordClass?recordClass="+className+"&appId="+appId;
        $.get(reqUrl,function (data) {
            getUrl("refresh",'no');
        })
    }
}

function keyDown(e,obj){
    var ev= window.event||e;

//13是键盘上面固定的回车键
    if (ev.keyCode == 13) {
        debugger;
        $(obj).next().find('i')[0].click();
    }
}