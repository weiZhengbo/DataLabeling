$(document).ready(function () {
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,'appId');
    if(appId == ''||appId == null){
        $('.mynav').children().first().addClass('active');
    }else {
        $('.mynav').find('#' + appId).addClass('active');
    }
});
function getQueryString(url,name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = url.match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}
function showAppDetail(appId,that) {
    sessionStorage.clear();
    var $li = $(that);
    $li.parent().find(".active").removeClass();
    $li.addClass('active');
    //判断点击的这个是分类标注还是相似标注
    var str = $li.find('a').text();
    var laststr = str.substring(str.indexOf('-')+1);
    var url1  = document.URL;
    var urlHead = url1.substring(0,url1.indexOf('DataLabeling'))+'DataLabeling';

    if(laststr == '分类标注'){
        var reqUrl = urlHead+"/RecordController/findAll?appId="+appId;

    }else if(laststr == '相似标注'){
        var reqUrl = urlHead+"/SimilarController/findAll?appId="+appId;
    }else if(laststr == '实体标注'){
        var reqUrl = urlHead+"/entityTag/getTaglist?appId="+appId+"&&dataType=-1";
    }
    $('#mainframe').attr('src',reqUrl);
    // window.location.href=reqUrl;
}
function showMainPage(that) {
    sessionStorage.clear();
    var $li = $(that);
    $li.parent().find(".active").removeClass();
    $li.addClass('active');
}
function addNewRecordClassProject(appType) {
    var appName = prompt("请输入新增的任务名称","");
    var url1  = document.URL;
    var url  = decodeURI(window.location.search.substr(1));
    var pid = getQueryString(url,"pid");
    var urlHead = url1.substring(0,url1.indexOf('DataLabeling'))+'DataLabeling';
    if(appName !=null && appName!=""){
        if(appType=='1'){
            appName=appName+'-分类标注';
        }else if(appType=='2'){
            appName=appName+'-相似标注';
        }else if(appType=='3'){
            appName=appName+'-实体标注';
        }
        var reqUrl = urlHead+'/Application/addNewApplication?projectId='+pid+'&appType='+appType+'&appName='+appName;
        $.get(reqUrl,function (data) {
            var appId = data.id;
            var appName = data.appName;
            $('.mynav').append('<li id="'+appId+'" onclick="showAppDetail('+appId+',this)"><a>'+appName+'</a></li>')
        });
    }
}
function iFrameHeight(id) {
    var ifm= document.getElementById(id);
    var subWeb = document.frames ? document.frames[id].document :ifm.contentDocument;
    if(ifm != null && subWeb != null) {
        ifm.height = subWeb.body.scrollHeight;
        ifm.width = subWeb.body.scrollWidth;
    }
}

function manageApplication(){
    var url1  = document.URL;
    var url  = decodeURI(window.location.search.substr(1));
    var pid = getQueryString(url,"pid");
    var urlHead = url1.substring(0,url1.indexOf('DataLabeling'))+'DataLabeling';
    url=urlHead+'/Application/manageApplications?pid='+pid;
    $.dialog({
        id:"dlg1",
        title:"标注项目管理",
        content:"url:"+url,
        width:600,
        height:400,
        min:false,
        max:false,
        background:"#000",
        opacity:0.2,
        lock:true,
        close:function() {
            location.reload();
        },
        button: [
            {
                name: '关闭'
            }
        ]
    });
}
var bak;
function editApp(id,appId){
    debugger;
    var value = $("#"+id).text();
    $("#"+id).replaceWith("<input type='text' id='"+id+"'value='"+value+"' onblur='change(\""+id+"\","+appId+")'/>");
    $("#"+id).focus();
    bak=value;
}

/**
 * 修改名称
 * @param id
 * @param appId
 */
function change(id,appId){
    var newName = $("#"+id).val();
    if(newName=="" || newName==null){
        alert("名称不能为空！");
        $("#"+id).replaceWith('<span  id="appName'+appId+'">'+bak+'</span>')
        return;
    }
    $.post("updateApp.action",
        {
            appName:newName,
            id:appId
        },
        function(data){
            alert(data);
            location.reload();
        });
}

function delApp(id,appName){
    if(confirm("确定要删除"+appName+"吗（删除后不可恢复）？"))
    {
        $.post("deleteApp.action",
            {
                id:id
            },
            function(data){
                alert(data);
                location.reload();
            });
    }
}