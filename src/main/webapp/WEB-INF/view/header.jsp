<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<div class="nheader">

	<div class="span6">
		<font style="font-size: 30px; font-weight:bold;">Seminaro</font>
	
		</div>
		
		<div class="span3">
		<font style="font-size: 16px; font-weight:bold;"><%=session.getAttribute("username")%></font>
		</div>
	<div class="span3">
		<a href="<c:url value="/home.html"/>" class="offset1" title="Home"
			style="#"><i class="icon-home icon-white"></i></a>&nbsp; <a href=""
			title="My Profile"><i class="icon-user icon-white"></i></a>&nbsp; <a
			href="#" title="Settings"><i class="icon-wrench icon-white"></i></a>&nbsp;
		<a href="<c:url value="/logout.html"/>" title="Log-Out"><i
			class="icon-off icon-white"></i></a>
	</div>

</div>