<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>HomePage</title>
<%@ include file="../common/header.jsp" %>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
	<script type="application/javascript">
		$(function(){
			$('#userList').datagrid({
				url:'user/listWithAjax',
				pagination:true,//分页控件
				loadMsg: "加载中,请稍候...",
				columns:[[
					{field:'id',title:'ID',width:100},
					{field:'username',title:'Name',width:100}
				]]
			});
		});
	</script>
</head>	

<body>
<table id="userList"></table>
</body>
</html>