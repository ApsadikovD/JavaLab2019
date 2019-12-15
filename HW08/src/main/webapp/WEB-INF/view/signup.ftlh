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
    <link rel="stylesheet" type="text/css" href="static/style.css">
</head>
<body>
<div class="container content">
    <div class="row">
        <form action="" method="post" class="col-10 col-sm-8 col-md-6 col-lg-5 col-xl-4 shadow login-form">
            <div class="text">Регистрация</div>
            <input type="text" class="input" name="email"
                   placeholder="Email" autocomplete="false">
            <div class="error">
            </div>
            <input type="password" class="input" name="password"
                   placeholder="Пароль" autocomplete="false">
            <div class="error">
            </div>
            <input type="submit" id="signup" class="submit" value="Зарегистрироваться">
        </form>
    </div>
</div>
</body>
</html>