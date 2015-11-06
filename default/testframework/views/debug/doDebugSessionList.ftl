<#include "*/includes/header.ftl">

<div class="container">
<h3> Choose session Id </h3>

<table class="table table-bordered table-stripped" data-toggle="table">
<thead>
  <tr>
    <th>Session Id</th>
    <th>Remote Ip</th>
    <th>  </th>
  </tr>
</thead>
  <#list session_list as session>
  <tr>
    <td>${session.sessionId}</td>
  <td>${session.remoteIp}</td>
  <td><a href="/debug/session_view.html?id=${session.sessionId}"> Link </a></td>
  <tr>
  </#list>

</table>
</div>


</body>
</html>
