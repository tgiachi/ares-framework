<#include "includes/header.tpl">

<div class="container">

<h1> Ares framework </h1>
<h2> Current date time => ${title} </h2>

<a href="pluto.html"> Test Database connection </a>

<img src="imgs/pippo.jpg">

<p> HTTP Method: is ${request_type}</p>

<a href="?myParam=3&maParam2=4"> Test params </a>

<p> Query params  count is ${values?size}</p>

<#if (values?keys?size > 0) >
<table class="table btn-success">
    <#list values?keys as key>
        <tr>
            <td width="35%">
                <div>${key}</div>
            </td>
            <td width="45%">${values[key]}</td>
            <td width="19%">&nbsp;</td>
        </tr>
    </#list>
  </table>
</#if>

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
</body>
</html>
