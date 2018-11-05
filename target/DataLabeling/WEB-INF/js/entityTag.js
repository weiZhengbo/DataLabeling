$(document).ready(function () {
    pageSize = 10;
    initPage();
    $("#classTable").each(function(){
       var obj=$(this).find('td').eq(0);
        var obj2=$(this).find('td').eq(1);
        $(obj).parent().css('background-color','bisque');
        $("#classId").val($(obj).text());
        $("#classCode").val(obj2.text());
    })
});

function initPage(){
    $("#Pagination").pagination('${pageResource.totalPage}', {
        items_per_page:'${pageResource.pageSize}', // 每页显示多少条记录
        current_page: '${pageResource.pageNo}',  //当前页
        num_display_entries:10, // 分页显示的条目数
        next_text:"下一页",
        prev_text:"上一页",
        num_edge_entries:4, // 连接分页主体，显示的条目数
        callback:PageCallback
    });
}
//翻页调用
function PageCallback(index, jq) {
    InitTable(index);
}

//请求数据
function InitTable(pageIndex) {
 location.href="noTaglist.action?appId="+$("#appId").val()+"&pageNo="+pageIndex+"&pageSize=10";

}

/**
 * 上传文件
 */
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
                +'<td onclick="chooseTag('+data.id+')">'+data.id+'</td>'
                +'<td onclick="chooseTag('+data.id+')">'+data.recordClass+'</td>'
                +'<td onclick="deleteTag('+data.id+','+data.recordClass+')">'
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
    if (text.substring(start, end) != selecter.toString()) {
        return;
    }

    var node = document.createElement("span");
    node.setAttribute("style", "color:orange");
    node.innerHTML = selecter.toString();
    selecter.deleteFromDocument();
    range.insertNode(node);
    var result = "";
    var classCode = $("#classCode").val();
    var resultCode = $("#" + id + "Code").text().split(" ");
    for (var i = 0; i < resultCode.length; i++) {
        if (i < start || i > end - 1) {
            result += resultCode[i];
        } else if (i > start && i < end - 1) {
            result += classCode + '_I';
        } else if (i == start) {
            result += classCode + '_B';
        } else if (i == end - 1) {
            result += classCode + '_O';
        }
        if (i != resultCode.length - 1) {
            result += ' '
        }
    }
    $("#" + id + "Code").text(result);
    //存入数据库
    $.ajax({
        url: "saveTagInfo.action",
        type: "POST",
        async: false,
        data: {
            "resultRecord": $("#" + id).html(),
            "resultCode": result,
            "id": infoId
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
        resultCode+="N";
        if(i != text.length-1){
            resultCode +=" ";
        }
    }
    $("#" + id + "Code").text(resultCode);
    $("#" + id).text(text);
    //存入数据库
    $.ajax({
        url: "saveTagInfo.action",
        type: "POST",
        async: false,
        data: {
            "resultRecord": $("#" + id).html(),
            "resultCode": resultCode,
            "id": infoId
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