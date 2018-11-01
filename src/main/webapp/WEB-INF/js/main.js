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
        var reqUrl = urlHead+"/entityTag/noTaglist?appId="+appId;
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