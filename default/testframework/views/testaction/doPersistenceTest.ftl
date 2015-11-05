<body>
  <div class="container">
    This is test of ORM!

    <#list result as entity>
      <div>
          <strong>${entity.field1}</strong>
          <strong>${entity.field2}</strong>
          <strong>${entity.field3}</strong>
      </div>
    </#list>

</div>
</body>
