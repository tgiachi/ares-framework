<#include "*/includes/header.ftl">
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment-with-locales.js" type="text/javascript"> </script>


<script type="text/javascript">

$(document).ready(function(){
  var list = document.getElementsByClassName("moment");
  Array.prototype.forEach.call(list, function(el) {
    el.innerHTML = moment(el.innerHTML, 'DD.MM.YYYY HH:mm:ss').fromNow();
  });

  var table = document.getElementById("tbl");
for (var i = 1, row; row = table.rows[i]; i++) {

  //var level = row.cells[4];
  if (row.cells[6] != null)
  {
    var level =row.cells[6].textContent;


     if (level == "ERROR")
      row.className = "danger fade in";
  }

}

});


</script>
<h3> Current session id ${id}</h3>

<div class="container">
<table id="tbl" class="table table-bordered table-stripped" data-toggle="table">
<thead>
  <tr>
    <th></th>
    <th>Date</th>
    <th>Action</th>
    <th>Type</th>
    <th>Result code </th>
    <th>Source</th>
    <th>Level</th>
    <th>Log</th>



  </tr>
</thead>
  <#list list as session>
  <tr>
  <td> ${session.date?string('dd.MM.yyyy HH:mm:ss')} </td>
  <td><div class="moment"> ${session.date?string('dd.MM.yyyy HH:mm:ss')} </div></td>
  <td><code>${session.navigateUrl}</code></td>
  <td class="text-center"><span class="label label-default">${session.type}</span></td>
  <td> ${session.resultCode}</td>
  <td  class="text-center">${session.source}</td>
  <td class="text-center"><span class="label label-info">${session.level}</span></td>
  <td>${session.log}</td>
  <tr>
  </#list>

</table>
</div>



</body>
</html>
