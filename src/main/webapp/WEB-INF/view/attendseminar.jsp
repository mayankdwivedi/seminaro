<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>


<div class="span3">
<br>
<br>
<br>
<br>
 <div class="alert alert-dismissable alert-success"><a href="createseminar.html" font style="color:#ffffff;">Organize</a></div>
 <div class="alert alert-dismissable alert-success"><a href="attendseminar.html" font style="color:#ffffff;">Attend</a></div>
 <div class="alert alert-dismissable alert-success"><a href="history.html" font style="color:#ffffff;">History</a></div>

</div>

<div class="span6">
<h1>${message}</h1>
<c:forEach var="seminarDisplayTopic" items="${seminarDisplayMap}">
 <div class="jumbotron">
 <a href="seminarinfo.html?seminarId=${seminarDisplayTopic.key}"> ${seminarDisplayTopic.value}</a>  
  <a class="btn btn-primary"
			href="acceptseminar.html?seminarId=${seminarDisplayTopic.key}" style="float:right;">Attend</a>
			</div>
		<br>
	</c:forEach>
	
	
	<c:forEach var="seminarAttendedMap" items="${seminarAttendedMap}">
 <div class="jumbotron">
 <a href="seminarinfo.html?seminarId=${seminarAttendedMap.key}"> ${seminarAttendedMap.value}</a>  
  <a class="btn btn-primary"
			href="unattendseminar.html?seminarId=${seminarAttendedMap.key}" style="float:right;">Unattend</a>
			</div>
		<br>
	</c:forEach>
</div>

<div class="span3">
</div>
</div>