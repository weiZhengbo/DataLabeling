$(document).ready(function () {
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');
    var clickwordId = getQueryString(url,'clickwordId');
    if(appId == ''||appId == null){
        $('.mynav').children().first().addClass('active');
    }else {
        $('.mynav').find('#'+appId).addClass('active');
    }
    $('#myModal').on('show.bs.modal', function (event) {
        var clickedId = event.relatedTarget.parentNode.parentNode.childNodes[1].textContent;
        var clickedText =event.relatedTarget.parentNode.parentNode.childNodes[3].textContent;
        $('#clickText').text(clickedText);
        $('#clickId').text(clickedId);
    });
    $('#sid'+clickwordId).css('background-color','bisque');

    $('.uploadFile').click(function () {
        var url  = decodeURI(window.location.search.substr(1));
        var appId = getQueryString(url,'appId');
        var url1  = document.URL;
        var head = url1.substring(0,url1.lastIndexOf('/'));
        var reqUrl = head+'/UploadHandleServlet?method=uploadFile&appId='+appId;
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
            getUrl("refresh",0);
        }).fail(function (res) {

        });
    });
});
function downLoadData(type) {
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');

    var url1  = document.URL;
    var head = url1.substring(0,url1.lastIndexOf('/'));
    if(type=='1'){
        var clickwordId = getQueryString(url,'clickwordId');
        if(clickwordId==null||clickwordId=='null'||clickwordId==''){
            alert('当前没有选择相似记录范本');
            return;
        }
        var reqUrl = head+'/DownLoadZipFileServlet?method=downLoadsimilarFile&appId='+appId+'&downType=1'+'&clickwordId='+clickwordId;
    }else if(type=='2'){
        var keyword = getQueryString(url,'keyword');
        if(keyword==null||keyword=='null'||keyword==''){
            alert('当前没有选择搜索的相似记录范本');
            return;
        }
        var reqUrl = head+'/DownLoadZipFileServlet?method=downLoadsimilarFile&appId='+appId+'&downType=2'+'&keyword='+keyword;
    }else if(type=='3'){
        var reqUrl = head+'/DownLoadZipFileServlet?method=downLoadsimilarFile&appId='+appId+'&downType=3';
    }
    window.location.href=reqUrl;
}
function addNewRecordClassProject(appType) {
    var appName = prompt("请输入新增的任务名称","");
    var url1  = document.URL;
    var url  = decodeURI(window.location.search.substr(1));
    var pid = getQueryString(url,"pid");
    var urlHead = url1.substring(0,url1.lastIndexOf('/'));
    if(appName !=null && appName!=""){
        if(appType=='1'){
            appName=appName+'-分类标注';
        }else if(appType=='2'){
            appName=appName+'-相似标注';
        }
        var reqUrl = urlHead+'/ProjectServlet?method=addNewApplication&pid='+pid+'&appType='+appType+'&appName='+appName;
        $.get(reqUrl,function (data) {
            getUrl("refresh",0);
        });
    }
}
function showAppDetail(appId,that) {
    var $li = $(that);
    $li.parent().find(".active").removeClass();
    $li.addClass('active');
    //判断点击的这个是分类标注还是相似标注
    var str = $li.find('a').text();
    var laststr = str.substring(str.length-4);
    var url  = decodeURI(window.location.search.substr(1));
    var pid = getQueryString(url,'pid');
    var url1  = document.URL;
    var urlHead = url1.substring(0,url1.lastIndexOf('/'));
    if(laststr == '分类标注'){
        var reqUrl = urlHead+"/RecordServlet?method=findAll&appId="+appId+"&pid="+pid;

    }else if(laststr == '相似标注'){
        var reqUrl = urlHead+"/SimilarServlet?method=findAll&appId="+appId+"&pid="+pid;
    }
    window.location.href=reqUrl
}
function drag(event) {
    event.dataTransfer.setData("Text",event.target.id);
    event.dataTransfer.dropEffect = "copy";
}
function drop_handler(event) {
    event.preventDefault();
    var tr = event.dataTransfer.getData("Text");
    var similarId = $('#'+tr).children().eq(0).text();
    var data = $('#'+tr).children().eq(1).text();
    event.target.textContent=data;
    var rid =event.target.parentNode.firstChild.nextElementSibling.textContent;
    var url  = decodeURI(window.location.search.substr(1));
    var url1  = document.URL;
    var appId = getQueryString(url,"appId");
    var urlHead = url1.substring(0,url1.indexOf('?'));
    var reqUrl = urlHead+"?method=addSimilarRecordClass&rid="+rid+"&sid="+similarId+"&appId="+appId;
    $.get(reqUrl,function (data,status) {
        if(data!=null && data!=""){
            var handled = parseInt($(".Handled").text())+1;
            var unHandled = parseInt($(".UnHandled").text())-1;
            $(".Handled").text(handled);
            $(".UnHandled").text(unHandled);
            event.target.parentNode.lastChild.previousElementSibling.innerHTML="<i class='glyphicon glyphicon-remove' onclick='javascript:similarTagRemove("+rid+",this)'></i>";
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
    var method = getQueryString(url,'method');
    var pc = getQueryString(url,'pc');
    var pc1 = getQueryString(url,'pc1');
    var clickwordId = getQueryString(url,'clickwordId');
    var keyword = getQueryString(url,'keyword');
    var noHandledWord = getQueryString(url,'noHandledWord');
    var pid = getQueryString(url,'pid');
    var appId = getQueryString(url,'appId');
    var dataType = getQueryString(url,'dataType');
    var reqUrl = head+'?method='+method;
    if (change=='pc'){
        reqUrl=reqUrl+'&pc='+value;
        reqUrl=reqUrl+'&pc1='+pc1+'&clickwordId='+clickwordId+'&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&pid='+pid+'&refresh=1';
    }else if(change=='pc1'){
        reqUrl=reqUrl+'&pc1='+value;
        reqUrl=reqUrl+'&pc='+pc+'&clickwordId='+clickwordId+'&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&pid='+pid+'&refresh=0'+'&noHandledWord='+noHandledWord;
    }else if(change=='clickwordId'){
        reqUrl=reqUrl+'&clickwordId='+value;
        reqUrl=reqUrl+'&pc=1'+'&pc1=1&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&pid='+pid+'&refresh=0'+'&noHandledWord='+noHandledWord;
    }else if(change=='keyword'){
        var $i=$(value);
        var txt = $i.parents('span').siblings('input').val();
        reqUrl=reqUrl+'&keyword='+txt;
        reqUrl=reqUrl+'&pc='+pc+'&clickwordId='+clickwordId+'&pc1='+pc1+"&appId="+appId+"&dataType="+dataType+'&pid='+pid+'&refresh=0'+'&noHandledWord='+noHandledWord;
    }else if(change=='dataType'){
        reqUrl=reqUrl+'&dataType='+value;
        reqUrl=reqUrl+"&appId="+appId+'&keyword='+keyword+'&pid='+pid+'&refresh=1'+'&noHandledWord='+noHandledWord;
    }else if (change=='refresh'){
        reqUrl=reqUrl+'&refresh='+value;
        reqUrl=reqUrl+'&pc='+pc+'&clickwordId='+clickwordId+'&keyword='+keyword+"&appId="+appId+"&dataType="+dataType+'&pid='+pid+'&pc1='+pc1+'&noHandledWord='+noHandledWord;
    }else if (change=='noHandledWord'){
        var $i=$(value);
        var txt = $i.parents('span').siblings('input').val();
        reqUrl=reqUrl+'&noHandledWord='+txt;
        reqUrl=reqUrl+'&pc='+pc+'&clickwordId='+clickwordId+'&pc1='+pc1+"&appId="+appId+"&dataType="+dataType+'&pid='+pid+'&refresh=1'+'&keyword='+keyword;
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
    var url  = decodeURI(window.location.search.substr(1));
    var url1  = document.URL;
    var appId = getQueryString(url,"appId");
    var urlHead = url1.substring(0,url1.indexOf('?'));
    if (CCid==null||CCid==''){
        alert('请选择类别标签');
    }else {
        var rid = $('#clickId').text();
        var reqUrl = urlHead+"?method=addSimilarRecordClass&rid="+rid+"&sid="+CCid+"&appId="+appId;
        $.get(reqUrl,function (data,status) {
            if(data!=null && data!=""){
                $('#myModal').modal('hide')
                $('#rr'+rid).parents('td').siblings('.sclass').text(data);
                $('#rr'+rid).parents('td')[0].innerHTML="<i class='glyphicon glyphicon-remove' onclick='javascript:similarTagRemove("+rid+",this)'></i>";
            }
        });
    }
}
function similarTagRemove(rid,that) {
    var $i = $(that);
    var url1  = document.URL;
    var urlHead = url1.substring(0,url1.indexOf('?'));
    var reqUrl = urlHead+"?method=removeSimilarRecordClass&rid="+rid;
    $.get(reqUrl,function (data,status) {
        if(data){
            var handled = parseInt($(".Handled").text())-1;
            var unHandled = parseInt($(".UnHandled").text())+1;
            $(".Handled").text(handled);
            $(".UnHandled").text(unHandled);
            $i.parents('td').siblings('.sclass').text("");
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
        var urlHead = url1.substring(0,url1.indexOf('?'));
        var reqUrl = urlHead+"?method=deleteSimilarRecordClass&sid="+sid;
        $.get(reqUrl,function (data,status) {
            if(clickwordId==sid){
                getUrl('clickwordId',"");
            }else {
                getUrl("refresh",0);
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
        var urlHead = url1.substring(0,url1.indexOf('?'));
        var reqUrl = urlHead+"?method=addnewSimilarRecordClass&className="+className+"&appId="+appId;
        $.get(reqUrl,function (data) {
            getUrl("refresh",0);
        })
    }
}