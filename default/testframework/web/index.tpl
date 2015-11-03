<#include "includes/header.tpl">

<div class="container">

  <div class="page-header">
    <h2>Ares Framework - Demo</h2>
  </div>

  <div class="panel panel-success">
    <div class="panel-heading"> Choose demo </div>
    <div class="panel-body">
<ul class="nav nav-pills nav-stacked">
  <li role="presentation"> <a href="${context_path}testdatabase.html"> Test database performance </a> </li>
  <li role="presentation"> <a href="page_random.html"> Test <kbd>404</kbd> Page Not found </a> </li>
  <li role="presentation"> <a href="make_error.html"> Test <kbd>500</kbd> Internal server Error </a> </li>
  <li role="presentation"> <a href="?myParam=3&secondParam=2"> Test page params </a> </li>
  <li role="presentation"> <a href="${context_path}debug.html"> View debug </a> </li>
  <li role="presentation"> <a href="${context_path}api/data.json"> Result as <kbd>JSON</kbd> data </a> </li>
  <li role="presentation"> <a href="${context_path}api/data.xml"> Result as <kbd>XML</kbd> data </a> </li>
  <li role="presentation"> <a href="${context_path}api/data.yaml"> Result as <kbd>YAML</kbd> data </a> </li>
  <li role="presentation"> <a href="${context_path}admin/"> Test secure area </a> </li>
</ul>
</div>
</div>

<ul class="nav nav-tabs">
  <li role="presentation" class="active"><a data-toggle="tab" href="#home">Query params <span class="badge">${values?size}</span></a></li>
  <li role="presentation"><a data-toggle="tab" href="#info">HTTP Info</a></li>
  <li role="presentation"><a data-toggle="tab" href="#headers">Headers <span class="badge">${headers?size}</span> </a></li>
  <li role="presentation"><a data-toggle="tab" href="#session">Session <span class="badge">${session_map?size}</span> </a></li>
</ul>
<div class="tab-content">
  <div id="home" class="tab-pane fade in active">
      <p> Query params  count is ${values?size}</p>
    <#if (values?keys?size > 0)>
    <table class="table btn-success">

        <#list values?keys as key>
            <tr>
                <td>
                    <div>${key}</div>
                </td>
                <td>${values[key]}</td>

            </tr>
        </#list>
      </table>
    </#if>
  </div>
  <div id="info" class="tab-pane fade">
    <code>
    <p> HTTP Method: is ${request_type}</p>
    </code>
  </div>
  <div id="session" class="tab-pane fade">
    <#if (session_map?keys?size > 0)>
    <table class="table btn-success">

        <#list session_map?keys as key>
            <tr>
                <td>
                    <div>${key}</div>
                </td>
                <td>${session_map[key]}</td>

            </tr>
        </#list>
      </table>
    </#if>
  </div>



  <div id="headers" class="tab-pane fade">
    <p> Headers count ${headers?size}</p>
    <#if (headers?keys?size > 0) >
    <table class="table btn-info">
        <#list headers?keys as key>

            <tr>
                <td width="35%">
                    <div>${key}</div>
                </td>
                <td width="45%">${headers[key]}</td>
                <td width="19%">&nbsp;</td>
            </tr>
        </#list>
      </table>
    </#if>
  </div>
</div>
</div>
</body>
</html>
