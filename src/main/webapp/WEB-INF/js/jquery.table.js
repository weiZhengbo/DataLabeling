/**
 * 杨洪全 2012-11-12 
 * 
  	$("#table1").initTable({
 		url:path + "/getGroupDmKs.action",
  		columnData:[
  		            {title:"科室代码",name:"ksDm",width:"25%"},
  		            {title:"科室名称",name:"ksmc",width:"25%",format:function(json){return json.ksmc+"1";},align:"center",click:function(json){alert(json.ksmc);}},
  		            {title:"拼音简码",name:"pym",width:"25%",format:function(json){return json.pym+"2";},align:"left",click:function(json){alert(json.pym);}},
  		            {title:"五笔码",name:"wbm",width:"25%",format:function(json){return json.wbm+"3";},click:function(json){alert(json.wbm);}}
  		           ],
  		selectDataNow:true,
  		isrowClick:false,	
  		showIndex:true//是否显示序号
  	});
  	
 * 	$("#table2").initTable({
 * 		url:path + "/getGroupDmKs.action",
 * 		columnData:[
 * 		            {title:"科室代码",name:"ksDm",width:"25%",align:"right"},
 * 		            {title:"科室名称",name:"ksmc",width:"25%",format:function(json){return json.ksmc+"1";},align:"center",click:function(json){alert(json.ksmc);}},
 * 		            {title:"拼音简码",name:"pym",width:"25%",format:function(json){return json.pym+"2";},align:"center",click:function(json){alert(json.pym);}},
 * 		            {title:"五笔码",name:"wbm",width:"25%",format:function(json){return json.wbm+"3";},align:"center",click:function(json){alert(json.wbm);}}
 * 		           ],
 * 		selectDataNow:true,
 * 		rowClickMethod:function(json){
 * 			alert(json.id);
 * 		}
 * 	});
 */
;(function($) {
	//初始化table
	$.fn.initTable = function(obj) {
		var defaultOption={
					tableCss:"tabStyle mtop10",//table样式
					url : null,//获得数据url
					columnData : null,//table中每一列的信息[{title:"第一列",name:"columnName",width:"10%",format:function(json){return json;},align:"center",click:function(json){}},{。。。。}，。。。。。]
					pageCountArray : [ 10, 20, 50, 100 ],//每页显示条数
					total : 0,
					allData : null,//当前用户条件查询出的数据集合
					userParm : null,//保存用户当前查询条件参数
					sysParm : {"pageResource.pageNum":1,"pageResource.pageCount":10},//系统参数,如果需要分页,默认分页为第一页,分页条数为每页10条
					
					allowClick:true,//是否允许添加单击事件,(注:如果不需要单击事件，请将属性设置为false,减少数据循环)
					
					isrowClick:false,//是否是行单击(注:如果为true,columnData中的列单击将失效)
					rowClickMethod:null,//行单击事件'
					
					selectDataNow:false,//是否立刻查询数据
					
					indexWidth:null,//序号列宽度
					showIndex:false,//是否显示序号
					
					onlyFirstPage:false,//只有一页数据
					index:1
		};

		var SUCCESS = true;// 初始化状态
		debugger;
		if (typeof obj == 'object') {
			try {
				$.extend(defaultOption,obj);
				$( this ).data( "option", defaultOption );
				if(defaultOption.selectDataNow){
					this.createdTable();
				}
			} catch (e) {
				SUCCESS = false;
			}
		} else {
			SUCCESS = false;
		}
		if (!SUCCESS) {
			alert("\u521D\u59CB\u5316\u5206\u9875Table\u51FA\u9519,\u8BF7\u68C0\u67E5\u53C2\u6570!");
		}
	};
	//创建table
	$.fn.createdTable=function(){
		var option = this.data("option");
		this.html("");//清空内容
		this.loadDataByAjax();//加载数据
		this.html(this.analysis());//解析数据
		if(option.allowClick){//绑定事件
			this.bindClick();
		}
		this.setRowClass();//设置行样式
	};
	
	//加载数据
	$.fn.loadDataByAjax=function(){
		var option = this.data("option");
		option.index=1;
		var url=null;//为了兼容一些程序设计时候用了url传参数，所以解析下url
		var urlArray=option.url.split("?");
		if(urlArray.length==2){
			url=urlArray[0]+"?_math="+Math.random()+"&"+urlArray[1];
		}else{
			url=option.url+"?_math="+Math.random();
		}
		var total=0;
		var tdata=null;
		$.ajax({    
			url: url,    
			type: 'POST',    
			dataType: 'json',
			data:option.sysParm,
			async:false,
			error: function(){        
				alert('\u670D\u52A1\u5668\u8BF7\u6C42\u5931\u8D25\uFF01');    
			},    
			success: function(data){  
				console.log(data);
				var json=eval(data);
				total=json.total;
				tdata=json.list;
			}
		});
		option.total=total;
		option.allData=tdata;
	};
	
	//解析数据
	$.fn.analysis=function(){
		var option = this.data("option");
		var tdata=option.allData;
		var table="<table class='"+option.tableCss+"' width='100%' border='0' cellspacing='0' cellpadding='0' >";//table构造开始
		table+="<tr >";//构造列表头
		if(option.showIndex){
			table+="<th style='text-align:center' width='"+(option.indexWidth==null?"5%":option.indexWidth)+"'>\u5E8F\u53F7</th>";
		}
		for(var i in option.columnData)
		{
			table+="<th  width='"+option.columnData[i]["width"]+"'>"+option.columnData[i]["title"]+"</th>";
		}
		table+="</tr>";//构造列表头结束
		if(tdata!=null && tdata.length>0)
		{
			for(var i in tdata)
			{
				var row="<tr onclick='javascript:clickTr(this);'>";
				if(option.showIndex){
					var pagenum=parseInt(option.sysParm["pageResource.pageNum"]);
					var pagecount=parseInt(option.sysParm["pageResource.pageCount"]);
					var no=(pagenum - 1)*pagecount+(parseInt(i)+1);
					row+="<td style='text-align:center;cursor: pointer;'>"+no+"</td>";
				}
				for(var j=0;j<option.columnData.length;j++)
				{
					if(option.columnData[j]["format"]==undefined){
						if(option.columnData[j]["showTips"] == undefined || option.columnData[j]["showTips"] == false) {
							row+="<td style='text-align:"+
							(option.columnData[j]["align"]==undefined?"center":option.columnData[j]["align"])+
							";cursor: pointer;'>"+((tdata[i][option.columnData[j]["name"]]==null)?"":tdata[i][option.columnData[j]["name"]])+"</td>";
						}else{
							var wordWidth = $(this).width() * $.pointToFloatNumber(option.columnData[j]["width"]);
							var tdContentWidth = 0;
							var tdContent = "";
							if(tdata[i][option.columnData[j]["name"]] != null || tdata[i][option.columnData[j]["name"]] != '') {
								tdContentWidth = $.getStringSizeNumber(tdata[i][option.columnData[j]["name"]]);
							}
							//-20是因为样式的左右各有10像素的距离
							if(tdContentWidth != 0 && tdContentWidth > wordWidth - 20) {
								tdContent = ($.getInterceptionReturnString(tdata[i][option.columnData[j]["name"]],
									wordWidth - 40)) + "...";
							} else {
								tdContent = tdata[i][option.columnData[j]["name"]];
							}
							if(tdata[i][option.columnData[j]["name"]] == null || tdata[i][option.columnData[j]["name"]] == '') {
								row+=$("<td></td>").css("tword-break", "break-all").css("word-wrap",
										"break-word").css("cursor", "pointer").css("text-align",
										option.columnData[j]["align"] == undefined ? "center" : option.columnData[j]["align"]).html((tdContent == null || tdContent == '') ? "&nbsp;" : tdContent)[0].outerHTML;
							} else {
								row+=$("<td></td>").css("tword-break", "break-all").css("word-wrap",
										"break-word").css("cursor", "pointer").css("text-align",
										option.columnData[j]["align"] == undefined ? "center" : option.columnData[j]["align"]).attr("title",
										(tdata[i][option.columnData[j]["name"]] == null) || (tdata[i][option.columnData[j]["name"]] == '') ? "&nbsp;" : tdata[i][option.columnData[j]["name"]]).html((tdContent == null || tdContent == '') ? "&nbsp;" : tdContent)[0].outerHTML;
							}
						}
					}else{
						row+="<td style='text-align:"+
						(option.columnData[j]["align"]==undefined?"center":option.columnData[j]["align"])+
						";cursor: pointer;'>"+option.columnData[j]["format"](option.allData[i])+"</td>";
					}
				}
				row+="</tr>";
				table+=row;
			}
			table+="</table>";//table构造结束
			
			if(!option.onlyFirstPage){//创建分页
				table+="<div class='tableMtop tablePage'>";
				table+="<span class='tableLeft'>\u5171"+option.total+"\u6761\u8BB0\u5F55,\u6BCF\u9875"+option.sysParm["pageResource.pageCount"]+"\u6761\u8BB0\u5F55,\u5F53\u524D\u7B2C"+
				option.sysParm["pageResource.pageNum"]+"\u9875</span><span class='tableRight'>\u6BCF\u9875\u663E\u793A<select style='width:45px;' onchange=\"$('#"+this.attr("id")+"').changePageCount(this)\">"+
				this.getPageCount()+"</select>\u6761&nbsp;<a onclick=\"$('#"+this.attr("id")+"').toPage(1)\">\u9996\u9875</a><a onclick=\"$('#"+this.attr("id")+"').toPage("+
				this.Previous()+")\">\u524D\u4E00\u9875</a>"+this.showOtherPage()+" <a onclick=\"$('#"+this.attr("id")+"').toPage("+
				this.Next()+")\">\u4E0B\u4E00\u9875</a><a onclick=\"$('#"+this.attr("id")+"').toPage("+
				this.getLastPageNum()+")\">\u672B\u9875</a></span>";
				table+="</div>";
			}
		}
		else
		{
			table="<table width='100%' border='0' cellspacing='0' cellpadding='0'>";
			table+="<tr>";
			table+="<td width='100%' height='200' align='center'><img src='js/table/100.png' /><span style='line-height: 48px; font-size: 20px;vertical-align:top'>\u6CA1\u6709\u641C\u7D22\u5230\u7ED3\u679C!</span></td>";
			table+="</tr>";
			table+="</table>";
		}
		return table;
	};
	
	//绑定行或者列click事件当设置了行单击事件后，列单击时间将失效
	$.fn.bindClick=function(){
		var option = this.data("option");
		var tableOption=option;
		if(option.rowClickMethod!=null && option.isrowClick==true)
		{
			//添加行单击事件
			var tdata=option.allData;
			$("tr",this).each(function(i){
				if(i>0){
					$(this).unbind("click").click( function() {
						tableOption["rowClickMethod"](tdata[i-1]);
                    });
				}
			});
		}
		else
		{	
			//添加列单击事件
			var columnData=option.columnData;
			var tdata=option.allData;
			$("tr",this).each(function(i){
				if(i>0){//排除表头
					$("td",this).each(function(j){
						if(option.showIndex){
							if(j!=0){
								if(columnData[j-1]["click"]!=undefined){
									$(this).unbind("click").click(function() {
										columnData[j-1]["click"](tdata[i-1]);
				                    });
								}
							}
						}else{
							if(j!=0){
								if(columnData[j]["click"]!=undefined){
									$(this).unbind("click").click(function() {
										columnData[j]["click"](tdata[i-1]);
				                    });
								}
							}
						}

					});
				}
			});
		}
	};
	
	//设置行样式
	$.fn.setRowClass=function(){
		var option = this.data("option");
		$("."+option.tableCss+" tr").mouseover(function() { // 如果鼠标移到class为tabStyle的表格的tr上时，执行函数
			$(this).addClass("over");
		}).mouseout(function() { // 给这行添加class值为over，并且当鼠标一出该行时执行函数
			$(this).removeClass("over");
		}); // 移除该行的class
		$("."+option.tableCss+" tr:odd").addClass("alt"); // 给class为tabStyle的表格的偶数行添加class值为alt
	};
	//获得最大页数
	$.fn.getLastPageNum= function(){
		var option = this.data("option");
		var last=0;
		if((option.total%option.sysParm["pageResource.pageCount"])!=0)
		{
			last=parseInt(option.total/option.sysParm["pageResource.pageCount"])+1;
		}
		else
		{
			last=option.total/option.sysParm["pageResource.pageCount"];
		}
		return last;
	};
	//构造页列表
	$.fn.showOtherPage=function()
	{
		var option = this.data("option");
		var showString="";
		var totalPageNum=this.getLastPageNum();
		if(totalPageNum<=5)
		{
			for(var i=1;i<=totalPageNum;i++)
			{
				showString+="<a onclick=\"$('#"+this.attr("id")+"').toPage("+i+")\">"+i+"</a>";
			}
		}else
		{
			var show=0;
			if(option.sysParm["pageResource.pageNum"]-2>=-1 && option.sysParm["pageResource.pageNum"]+2<=5)
			{
				for(var i=1;i<=option.sysParm["pageResource.pageNum"];i++)
				{
					show++;
					showString+="<a onclick=\"$('#"+this.attr("id")+"').toPage("+i+")\">"+i+"</a>";
				}
				for(var i=1;i<=(5-show);i++)
				{
					showString+="<a onclick=\"$('#"+this.attr("id")+"').toPage("+(option.sysParm["pageResource.pageNum"]+i)+")\">"+(option.sysParm["pageResource.pageNum"]+i)+"</a>";
				}
			}else if(totalPageNum<=option.sysParm["pageResource.pageNum"]+2 && option.sysParm["pageResource.pageNum"]>3)
			{
				show=totalPageNum-option.sysParm["pageResource.pageNum"];
				for(var i=(5-show);i>=1;i--)
				{
					showString+="<a onclick=\"$('#"+this.attr("id")+"').toPage("+(option.sysParm["pageResource.pageNum"]-i+1)+")\">"+(option.sysParm["pageResource.pageNum"]-i+1)+"</a>";
				}
				for(var i=1;i<=show;i++)
				{
					showString+="<a onclick=\"$('#"+this.attr("id")+"').toPage("+(option.sysParm["pageResource.pageNum"]+i)+")\">"+(option.sysParm["pageResource.pageNum"]+i)+"</a>";
				}
			}
			else
			{
				for(var i=3;i>=1;i--)
				{
					showString+="<a onclick=\"$('#"+this.attr("id")+"').toPage("+(option.sysParm["pageResource.pageNum"]-i+1)+")\">"+(option.sysParm["pageResource.pageNum"]-i+1)+"</a>";
				}
				for(var i=1;i<=2;i++)
				{
					showString+="<a onclick=\"$('#"+this.attr("id")+"').toPage("+(option.sysParm["pageResource.pageNum"]+i)+")\">"+(option.sysParm["pageResource.pageNum"]+i)+"</a>";
				}
			}
		}
		
		return showString;
	};
	//跳转到上一页
	$.fn.Previous=function()
	{
		var option = this.data("option");
		if(option.sysParm["pageResource.pageNum"]>1)
		{
			return option.sysParm["pageResource.pageNum"]-1;
		}
		else
		{
			return 1;
		}
	};
	//跳转到下一页
	$.fn.Next=function()
	{
		var option = this.data("option");
		if(option.sysParm["pageResource.pageNum"]<this.getLastPageNum())
		{
			return option.sysParm["pageResource.pageNum"]+1;
		}
		else
		{
			return this.getLastPageNum();
		}
	};
	//通过初始化参数显示每页显示条数选择
	$.fn.getPageCount=function()
	{
		var option = this.data("option");
		var options="";
		
		for(var i=0;i<option.pageCountArray.length;i++)
		{
			if(option.sysParm["pageResource.pageCount"]==option.pageCountArray[i])
			{
				options+="<option selected='selected' value='"+option.pageCountArray[i]+"'>"+option.pageCountArray[i]+"</option>";
			}
			else
			{
				options+="<option value='"+option.pageCountArray[i]+"'>"+option.pageCountArray[i]+"</option>";
			}
			
		}
		return options;
	};
	//跳转到制定页面
	$.fn.toPage=function(pageNum)
	{
		var option = this.data("option");
		if(pageNum==option.sysParm["pageResource.pageNum"])
		{
			return;
		}
		option.sysParm["pageResource.pageNum"]=pageNum;
		if(option.userParm!=null)
		{
			$.extend(option.sysParm,option.userParm);
		}
		this.createdTable();
	};
	//当改变每页显示条数时候刷新到第一页面
	$.fn.changePageCount=function(obj)
	{
		var option = this.data("option");
		option.sysParm["pageResource.pageCount"]=obj.value;
		option.sysParm["pageResource.pageNum"]=1;
		this.refresh();
	};
	//刷新当前页面数据
	$.fn.refresh=function()
	{
		var option = this.data("option");
		if(option.userParm!=null)
		{
			$.extend(option.sysParm,option.userParm);
		}
		this.createdTable();
	};
	//通过构造用户参数查询数据
	$.fn.selectDataAfterSetParm=function(userParm){
		var option = this.data("option");
		if(userParm!=null)
		{
			option.userParm=userParm;
			$.extend(option.sysParm,{"pageResource.pageNum":1});
			$.extend(option.sysParm,userParm);
		}
		this.createdTable();
	};
	$.pointToFloatNumber = function (pointVal) {
		return  parseInt(pointVal.replace("%", "")) / 100;
	};

	$.getStringSizeNumber = function (strVal) {
		if(strVal==null){
			return "";
		}
		var valArray = strVal.split("");
		var totalWidth = 0;
		for(var i in valArray) {
			if(valArray[i].match(/[0-9a-zA-z]+$/g) != null) {
				totalWidth += 8;
			} else if(valArray[i].match(/[.,]+$/g) != null) {
				totalWidth += 3;
			} else {
				totalWidth += 15;
			}
		}
		return totalWidth;
	};

	$.getInterceptionReturnString = function (strVal, backStrLength) {
		if(strVal==null){
			return "";
		}
		var valArray = strVal.split("");
		strVal = "";
		var totalWidth = 0;
		for(var i in valArray) {
			if(totalWidth < backStrLength) {
				if(valArray[i].match(/[0-9a-zA-z]+$/g) != null) {
					totalWidth += 8;
				} else if(valArray[i].match(/[.,]+$/g) != null) {
					totalWidth += 3;
				} else {
					totalWidth += 15;
				}
				strVal += valArray[i];
			}
		}
		return strVal.substr(0, strVal.length - 1);
	};
})(jQuery);

function clickTr(obj){
	
	$(obj).parent().find("tr").each(function(i){
		if(i!=0){
			$(this).removeClass("clickCss");
		}
	});
	$(obj).addClass("clickCss");
}
