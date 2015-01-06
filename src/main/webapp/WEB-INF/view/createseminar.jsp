  
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">


<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.0/jquery-ui.js"></script>

<script>

  $(function() {
    $( "#datepicker" ).datepicker();
  });
  $(function() {
	    $( "#datepicker2" ).datepicker();
	  });
  </script>



<div class="row-fluid">
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

<form method="post" action="addseminar.html" class="jumbotron">
   
  <input type="text" class="span" style=" height:40px; font-size:20px;" placeholder="Topic" name="topic" /><br/>
  <textarea class="span" rows="5" style="height:140px; font-size:20px; resize:none;"  placeholder="Long Description" name="longDescription" /></textarea></br><br/>
    <input type="text" class="span" style="height:40px; font-size:20px;" placeholder="Venue" name="venue" /><br/>
  <input type="text" class="span" style="height:40px; font-size:20px;" id="datepicker" placeholder="Start Date" name="startDate" /><br/>
  <input type="text" class="span" style="height:40px; font-size:20px;"  id="datepicker2" placeholder="End Date" name="endDate" /><br/>
    <input type="text" class="span" style="height:40px; font-size:20px;" placeholder="Seats" name="seats" /><br/>
      <input type="text" class="span" style="height:40px; font-size:20px;" placeholder="Price" name="price" /><br/>
  
   <br/>
  <button type="submit" class="btn">Add Seminar</button>
</form>

</div>

<div class="span3">
</div>
</div>