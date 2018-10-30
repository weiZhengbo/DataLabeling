function addProject() {
    var projectName = prompt("请输入要新增的项目名","");
    if(projectName!=null && projectName!=""){
        var url1  = document.URL;
        var urlHead = url1.substring(0,url1.indexOf('?'));
        var reqUrl = urlHead+"?method=addnewProject&projectName="+projectName;
        $.get(reqUrl,function (data) {
            console.log(data);
            window.location.href=url1;
        });
    }
}
function clickToThisPage(pid) {
    var url1  = document.URL;
    var urlHead = url1.substring(0,url1.lastIndexOf('/'));
    window.location.href=urlHead+'/ProjectServlet?method=loadApplications&pid='+pid;
}