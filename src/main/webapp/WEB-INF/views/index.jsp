<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Menu Program</title>
</head>
<body>
<h1>Program Spring Boot</h1>
<nav>
    <a href="<c:url value='/'/>">Beranda</a> |
    <a href="<c:url value='/john'/>">Program Gaji John Travolta</a> |
    <a href="<c:url value='/kuadrat'/>">Program Persamaan Kuadrat</a>
</nav>
<hr>
<p>Pilih program yang ingin digunakan:</p>
<ul>
    <li><a href="<c:url value='/john'/>">Kalkulator gaji John Travolta</a></li>
    <li><a href="<c:url value='/kuadrat'/>">Kalkulator persamaan kuadrat</a></li>
</ul>
</body>
</html>
