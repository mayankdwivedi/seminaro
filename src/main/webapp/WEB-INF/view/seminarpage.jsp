
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
<h1>${message}</h1>
<div class="jumbotron">
<%-- Topic &nbsp;${seminarInfo.topic} 
 --%>

 <div class="panel panel-default">
  <div class="panel-heading">Topic</div>
  <div class="panel-body">
    ${seminarInfo.topic}
  </div>
</div>

	<br>
	
	<div class="panel panel-default">
  <div class="panel-heading">Long Description</div>
  <div class="panel-body">
    ${seminarInfo.longDescription}
  </div>
</div>
<%-- Long Description &nbsp;${seminarInfo.longDescription}  --%> <br>

<div class="panel panel-default">
  <div class="panel-heading">Venue</div>
  <div class="panel-body">
    ${seminarInfo.venue}
  </div>
</div>


<div class="panel panel-default">
  <div class="panel-heading">Start Date</div>
  <div class="panel-body">
    ${seminarInfo.startDate}
  </div>
</div>
 
 
 
<div class="panel panel-default">
  <div class="panel-heading">End Date</div>
  <div class="panel-body">
   ${seminarInfo.endDate}
  </div>
</div>

<div class="panel panel-default">
  <div class="panel-heading">Seats</div>
  <div class="panel-body">
    ${seminarInfo.seats}
  </div>
</div>

<div class="panel panel-default">
  <div class="panel-heading">Price</div>
  <div class="panel-body">
    ${seminarInfo.price}
  </div>
</div>

<!-- we have to hide this button in case of history
 --><%-- <c:choose>
  				<c:when test="${not empty postItem.value.imagelocation}"><img src="${imagePath}${postItem.value.imagelocation}"/><br></c:when>
				</c:choose>
 --%>
 <%-- <a class="btn btn-primary"
			href="acceptseminar.html?seminarId=${seminarId}" style="float:right;">Attend</a>
 --%>
</div>
<div class="span3">
</div>
