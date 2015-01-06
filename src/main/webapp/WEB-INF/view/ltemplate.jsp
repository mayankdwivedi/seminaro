<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<c:url value="/resources/css/Bootstrap.css"/>" rel="stylesheet">
<link href="<c:url value="/resources/css/design.css"/>" rel="stylesheet">
<link href="<c:url value="/resources/css/theme.css"/>"
	rel="stylesheet">
<title>me n we</title>
</head>
<body>
<div class="page-wrap">
	<div class="row-fluid">
		<tiles:insertAttribute name="header"></tiles:insertAttribute>
		
	</div>

	
	<div class="row-fluid">
		

		<tiles:insertAttribute name="body"></tiles:insertAttribute>
	</div>
</div>
	<div class="row-fluid">
		
				
				<tiles:insertAttribute name="footer"></tiles:insertAttribute>
			
	</div>
</body>
</html>

