<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta charset="UTF-8">
    <title>Document</title>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="/static/style.css">
</head>
<body>
    <#list products as product>
    <div class="container-fluid">
        <div>Название: ${product.name}</div>
        <div>Описание:${product.desc}</div>
        <div>Цена: ${product.price} руб</div>
        <hr>
    </div>
    </#list>
</body>
</html>