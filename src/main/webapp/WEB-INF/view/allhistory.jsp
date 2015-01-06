<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>


<div class="span3">
<br>
<br>
<br>
<br>
 <div class="alert alert-dismissable alert-success"><a href="createseminar.html" font style="color:#ffffff;	">Organize</a></div>
 <div class="alert alert-dismissable alert-success"><a href="attendseminar.html" font style="color:#ffffff;">Attend</a></div>
 <div class="alert alert-dismissable alert-success"><a href="history.html" font style="color:#ffffff;">History</a></div>

</div>

<div class="span6">
<b>Seminar Organised</b>
<hr>


<c:forEach var="seminarOrganisedTopic" items="${seminarOrganisedTopic}">
 <div class="jumbotron">
  <a href="seminarinfo.html?seminarId=${seminarOrganisedTopic.key}"> ${seminarOrganisedTopic.value}</a>  
 
 </div>
 </c:forEach>
 <br>
 
 <b>Seminar Attended</b>
<hr>
 <c:forEach var="seminarAttendedTopic" items="${seminarAttendedTopic}">
 <div class="jumbotron">${seminarAttendedTopic.value}</div>
 </c:forEach>
 </div>

</div>
<div class="span3">
</div> 