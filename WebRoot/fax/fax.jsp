<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="/extcommon.jsp"/>
	<style type="">
		.x-grid-no-read  td{ 
		    font-weight:bold;
		}
	</style>
    <!-- page specific -->
    <script type="text/javascript" src="fax/js/faxTabPanel.js"></script>
    <script type="text/javascript" src="common/js/popedom.js"></script>
  </head>
  
	<body>
		<input type="hidden" id="flag" value="FAXRSEND"/>
		<input type="hidden" id="custId" value="<%=request.getParameter("custId") %>"/>
	</body>
</html>
