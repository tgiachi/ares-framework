<#include "includes/header.ftl">


<div class="container">

<h2> I'll test the power of database </h2>

<h3> Query execution time is ${query_execution_time} microseconds </h3>


<table class="table table-bordered" data-toggle="table">
<#list tables as table>

  <div>
      <strong>${table}</strong>
  </div>
</#list>
</table>

<table class="table table-bordered table-stripped" data-toggle="table">
<thead>
  <tr>
    <th>table_catalog</th>
    <th>table_schema</th>
    <th>table_name</th>
    <th>table_type</th>
  </tr>
</thead>
  <#list mapped as map>
  <tr>
    <td>${map.tableCatalog}</td>
  <td>${map.tableSchema}</td>
  <td>${map.tableName}</td>
  <td>${map.tableType}</td>
  <tr>


  </#list>

</table>
</div>


</body>
</html>
