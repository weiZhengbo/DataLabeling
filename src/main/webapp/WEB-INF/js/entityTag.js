$(document).ready(function () {
    $("#classTable").each(function(){
       var obj=$(this).find('td').eq(0);
        var obj2=$(this).find('td').eq(1);
        $(obj).parent().css('background-color','bisque');
        $("#classId").val($(obj).text());
        $("#classCode").val(obj2.text());
    });
    var i = 0;
    $("#recordTable tbody tr").each(function(){;
        var obj;
        var obj1;
        var text;
        if(i%2==0){
            obj = $(this).find('div').eq(0);
            obj1 = $(this).find('div').eq(1);
            text=$(obj1).text();
            var kg = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+
                "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
            var reg = new RegExp(kg,"g");
            var html = text.replace(/O /g,"&nbsp&nbsp").replace("&nbspO","&nbsp&nbsp").replace(" O","&nbsp&nbsp")
                .replace(reg,kg+"<br/>");
            $(obj).html(html);
        }
        i++;
         });
});

/**
 * 上传文件
 */
function uploadFile(){
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
                if(data=="上传成功"){
                    location.reload();
                }
            }
        });
}

function getQueryString(url,name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = url.match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

function addClass() {
    var className = prompt("请输入要新增的前缀标签","");
    var url  = decodeURI(window.location.search.substr(1));
    var appId = getQueryString(url,"appId");
    if(className!=null && className!=""){
        var url1  = document.URL;
        var urlHead = url1.substring(0,url1.lastIndexOf('/'));
        var reqUrl = urlHead+"/addnewRecordClass?recordClass="+className+"&appId="+appId;
        $.get(reqUrl,function (data) {
            //var data = eval("("+data+")");
            var tr = ' <tr draggable="true" ondragstart="drag(event)" id="sid${tClasses.id}">'
                +'<td onclick="chooseTag('+data.id+',\''+data.recordClass+'\',this)">'+data.id+'</td>'
                +'<td onclick="chooseTag('+data.id+',\''+data.recordClass+'\',this)">'+data.recordClass+'</td>'
                +'<td onclick="deleteTag('+data.id+',\''+data.recordClass+'\',this)">'
                +'<i class="glyphicon glyphicon-remove" ></i> </td> </tr>';
            $("#classTable tbody").append(tr);
        })
    }
}

/**
 * 选择前缀
 */
function chooseTag(id,classCode,obj){
    $('#classTable tbody tr').css('background-color', '');
    $(obj).parent().css('background-color','bisque');
    $("#classId").val(id);
    $("#classCode").val(classCode);
}

//删除前缀
function deleteTag(sid,recordClass,obj) {
    if (confirm("确认删除前缀标签："+recordClass+"吗？")) {
        var url1  = document.URL;
        var urlHead = url1.substring(0,url1.lastIndexOf('/'));
        var reqUrl = urlHead+"/deleteRecordClass?sid="+sid;
        $.get(reqUrl,function (data) {
            if(data==true){
               alert("删除成功！");
               $(obj).parent().remove();
            }
        });
    }
}

/**
 * 标记
 * @param id
 * @param infoId
 */
function getSelect(id,infoId) {
    var selecter = window.getSelection();
    var el = document.getElementById(id)
    var range = window.getSelection().getRangeAt(0);
    var start = range.startOffset;
    var end = range.endOffset;
    var text = el.innerText;
    var html = el.innerHTML;
    var offset = 0;
    var str = '';
    var container = range.startContainer;
    while (container.previousSibling) {
        str += container.previousSibling.textContent.trim();
        offset += container.previousSibling.textContent.trim().length;
        container = container.previousSibling;
    }
    start += offset;
    end += offset;
    if(end<start){
        end=text.length;
    }
    if(text==selecter.toString().trim()){
        start=0;
        end=text.length;
    }
    if (text.substring(start, end) != selecter.toString().trim()) {
        return;
    }
    if(end< text.length){
        var node = document.createElement("span");
        node.setAttribute("style", "color:orange");
        node.innerHTML = selecter.toString();
        selecter.deleteFromDocument();
        range.insertNode(node);
    }else{
        var subffix = html.substring(0,html.length -(end-start));
        var middel = "<span style=\"color:orange\">"+text.substring(start,end)+"</span>";
        el.innerHTML = subffix+middel;
    }
    var result = "";
    var classCode = $("#classCode").val();
    var resultCode = $("#" + id + "Code").text().split(" ");
    var sub="";
    if(classCode!=null && classCode!="") {
        sub = "-"+classCode;
    }
    for (var i = 0; i < resultCode.length; i++) {
        if (i < start || i > end - 1) {
            result += resultCode[i];
        } else if (i > start && i < end ) {
            result += 'I'+sub;
        } else if (i == start) {
            result += 'B' + sub;
        }
        if (i != resultCode.length - 1) {
            result += ' '
        }
    }
    var kg = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+
        "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
    var reg = new RegExp(kg,"g");
    $("#" + id + "Code").text(result);
    $("#" + id + "Code1").html(result.replace(/O /g,"&nbsp&nbsp").replace("&nbspO","&nbsp&nbsp").replace(" O","&nbsp&nbsp").replace(reg,kg+"<br/>"));
    //存入数据库
    $.ajax({
        url: "saveTagInfo.action",
        type: "POST",
        async: false,
        data: {
            "resultRecord": $("#" + id).html(),
            "resultCode": result,
            "id": infoId,
            "recordClassId":0
        },
        dataType: "json",
        success: function (data) {
            // alert(data);
        }
    });
}

/**
 * 取消标记
 * @param id
 * @param infoId
 */
function cancelSelect(id,infoId){
    var text = $("#"+id).text();
    var resultCode = "";
    for (var i = 0; i < text.length; i++) {
        resultCode+="O";
        if(i != text.length-1){
            resultCode +=" ";
        }
    }
    $("#" + id + "Code").text(resultCode)
    $("#" + id + "Code1").text("");
    $("#" + id).text(text);
    //存入数据库
    $.ajax({
        url: "saveTagInfo.action",
        type: "POST",
        async: false,
        data: {
            "resultRecord": $("#" + id).html(),
            "resultCode": resultCode,
            "id": infoId,
            "recordClassId":-1
        },
        dataType: "json",
        success: function (data) {
            // alert(data);
        }
    });

}
    function exportResult() {
        var url =  "exportResult.action?appId="+ $("#appId").val();
        window.location.href = url;
    }


    function changeType(obj){
        var dataType= $(obj).val();
        var appId= $("#appId").val();
        location.href="getTaglist?appId="+appId+"&&dataType="+dataType;
    }


function getUrl(change,value) {
    var url  = decodeURI(window.location.search.substr(1));
    var url1  = document.URL;
    var head = url1.substring(0,url1.indexOf('?'));
    
    var appId = getQueryString(url,'appId');
    var dataType = getQueryString(url,'dataType');

    var reqUrl = head;
    reqUrl=reqUrl+'?pc='+value;
    reqUrl=reqUrl+"&appId="+appId+"&dataType="+dataType;

    window.location.href=reqUrl;
}

function reloadPage(){
    location.reload();
}