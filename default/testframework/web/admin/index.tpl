<#include "../includes/header.tpl">


<div class="container">

<p> This is protected area </p>
<p> Email: ${session.getAttribute("email")} and your password ${session.getAttribute("password")} you are logged! </p>
</div>

<a href="${context_path}auth/logout">Logout</a>

<body>
</html>
