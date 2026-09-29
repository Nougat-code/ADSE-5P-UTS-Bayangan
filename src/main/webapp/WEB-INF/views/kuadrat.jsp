<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Kalkulator Persamaan Kuadrat</title>
</head>
<body>
<nav>
    <a href="<c:url value='/'/>">Beranda</a> |
    <a href="<c:url value='/john'/>">Kalkulator gaji</a> |
    <a href="<c:url value='/kuadrat'/>">Persamaan kuadrat</a>
</nav>
<h1>Kalkulator Persamaan Kuadrat</h1>
<p>Masukkan koefisien untuk persamaan ax² + bx + c = 0. Koefisien a tidak boleh 0.</p>

<c:if test="${not empty errors}">
    <div role="alert">
        <strong>Input tidak valid:</strong>
        <ul><c:forEach var="error" items="${errors}"><li><c:out value="${error}"/></li></c:forEach></ul>
    </div>
</c:if>

<form action="<c:url value='/kuadrat'/>" method="post">
    <p>
        <label for="a">Koefisien a:</label><br>
        <input id="a" name="a" type="number" step="any" required value="<c:out value='${a}'/>">
    </p>
    <p>
        <label for="b">Koefisien b:</label><br>
        <input id="b" name="b" type="number" step="any" required value="<c:out value='${b}'/>">
    </p>
    <p>
        <label for="c">Koefisien c:</label><br>
        <input id="c" name="c" type="number" step="any" required value="<c:out value='${c}'/>">
    </p>
    <button type="submit">Hitung akar</button>
</form>

<c:if test="${not empty resultKind}">
    <hr>
    <h2><c:out value="${resultDescription}"/></h2>
    <p>x₁ = <c:out value="${rootOne}"/></p>
    <p>x₂ = <c:out value="${rootTwo}"/></p>
    <p>
        <c:choose>
            <c:when test="${resultKind == 'real'}">Diskriminan positif (D &gt; 0).</c:when>
            <c:when test="${resultKind == 'repeated'}">Diskriminan nol (D = 0).</c:when>
            <c:otherwise>Diskriminan negatif (D &lt; 0).</c:otherwise>
        </c:choose>
    </p>
</c:if>

<p><a href="<c:url value='/'/>">Kembali ke menu</a></p>
</body>
</html>
