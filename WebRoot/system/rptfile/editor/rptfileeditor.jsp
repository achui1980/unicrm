<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>编辑报表信息</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="/extcommon.jsp"/>
	<link rel="stylesheet" type="text/css" href="common/ext/resources/css/file-upload.css" />
	<script type="text/javascript" src="common/ext/ux/FileUploadField.js"></script>
    <!-- page specific -->
    <script type="text/javascript" src="system/rptfile/editor/js/rptfileedit.js"></script>
  </head>
  
	<body>
		<input type="hidden" id="modId" value="<%=request.getParameter("id")%>">
		<input type="hidden" id="gridId" value="<%=request.getParameter("gridId")%>">
		<input type="hidden" id="pIfameId" value="<%=request.getParameter("pIfameId") %>"/>
		<input type="hidden" id="myIfameId" value="<%=request.getParameter("myIfameId") %>"/>
	</body>
</html>
