function addProject() {
    var projectName = prompt("请输入要新增的项目名","");
    if(projectName!=null && projectName!=""){
        var url  = document.URL;
        var reqUrl = url+"Project/addnewProject?projectName="+projectName;
        $.get(reqUrl,function (data) {
            window.location.href=url;
        });
    }
}
function clickToThisPage(pid) {
    var url  = document.URL;
    window.location.href=url+'Application/loadApplications?pid='+pid;
}