<#include "../includes/header.tpl">


<div class="container">

<p> Need auth for continue </p>
</div>
<form method="post" action="${context_path}auth/login_post" name="frmPostData">
  <div class="form-group">
     <label for="exampleInputEmail1">Email address</label>
     <input name="email" type="email" class="form-control" id="exampleInputEmail1" placeholder="Email">
   </div>
   <div class="form-group">
     <label for="exampleInputPassword1">Password</label>
     <input name="password" type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
 </div>
  <button type="submit" class="btn btn-default">Login</button>

</form>

<body>
</html>
