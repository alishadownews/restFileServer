<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>exam REST</title>
</head>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
    var prefix = 'http://localhost:8080/restFileServer';
    var findBoxValue = document.getElementById('findBox').value;



    var RestFindFile = function() {
        $.ajax({
            type: 'GET',
            url:  prefix + '/' + findBoxValue,
            dataType: 'json',
            async: true,
            success: function(result) {
                alert('Время: ' + result.time
                        + ', сообщение: ' + result.message);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }

    var RestGetFile = function() {
        $.ajax({
            type: 'GET',
            url:  prefix + '/' + Date.now(),
            dataType: 'json',
            async: true,
            success: function(result) {
                alert('Время: ' + result.time
                        + ', сообщение: ' + result.message);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }

    var RestLoadFile = function() {
        var JSONObject= {
            'time': Date.now(),
            'message': 'Это пример вызова PUT метода'
        };

        $.ajax({
            type: 'PUT',
            url:  prefix,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(JSONObject),
            dataType: 'json',
            async: true,
            success: function(result) {
                alert('Время: ' + result.time
                        + ', сообщенеи: ' + result.message);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }

    var RestDelete = function() {
        $.ajax({
            type: 'DELETE',
            url:  prefix + '/' + Date.now(),
            dataType: 'json',
            async: true,
            success: function(result) {
                alert('Время: ' + result.time
                        + ', сообщение: ' + result.message);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }
</script>

<body>

    <h3>пример использования REST</h3>

    <button type="button" onclick="RestFindFile()">Найти файл</button>
    <input name="findBox" maxlength="25" size="40" >
    <BR>
    <button type="button" onclick="RestGetFile()">Получение файла</button>
    <input maxlength="25" size="40" >
    <BR>
    <button type="button" onclick="RestLoadFile()">Залить Файл</button>
    <input maxlength="25" size="40" >
    <BR>
    <button type="button" onclick="RestDeleteFile()">Удалить файл</button>
    <input maxlength="25" size="40" >

</body>
</html>
