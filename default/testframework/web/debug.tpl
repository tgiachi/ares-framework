<#include "includes/header.tpl">
<script>
setTimeout(function(){
   window.location.reload(1);
}, 5000);
</script>
<div class="container">
  <script>
    function clearLogs()
    {
      $.get( "debug_clear_logs.json", function( data ) {
  $( ".result" ).html( data );

});
    }

  </script>

<div class="alert alert-info">
    <a href="#" onclick="clearLogs();">Clear logs</a>
  <p class="result">
    IDLE
  </p>

  <div>
    Prev url ${session_prev}
  </div>
</div>


  <table class="table table-bordered table-stripped" data-toggle="table">
  <thead>
    <tr>
      <th>Data</th>
      <th>Class name</th>
      <th>Operation</th>
      <th>Generation Time (in &#xb5;seconds)</th>
      <th>Log</th>
    </tr>
  </thead>
    <#list generation_stats as map>
    <tr>
        <td>${map.date?string('dd.MM.yyyy HH:mm:ss')}</td>
      <td>${map.className}</td>
    <td>${map.operation}</td>
    <td>${map.generationTime}</td>
    <td><code>${map.log}</code></tb>
    <tr>


    </#list>

  </table>

</div>

</body>
</html>
