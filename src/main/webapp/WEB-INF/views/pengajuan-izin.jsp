<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Pengajuan Izin Mahasiswa</title>
</head>
<body>
<h1>Pengajuan Izin Mahasiswa</h1>
<nav><a href="<c:url value='/'/>">Beranda</a></nav>
<form method="post" action="<c:url value='/pengajuan-izin'/>">
    <label>Nama mahasiswa
        <input type="text" name="studentName" value="<c:out value='${studentName}'/>" required>
    </label><br>
    <label>Alasan
        <textarea name="reason" required><c:out value="${reason}"/></textarea>
    </label><br>
    <label>Lama izin (hari)
        <input type="number" name="days" min="1" value="<c:out value='${days}'/>" required>
    </label><br>
    <button type="submit">Ajukan izin</button>
</form>
<c:if test="${not empty error}">
    <p role="alert"><c:out value="${error}"/></p>
</c:if>
<c:if test="${not empty status}">
    <h2>Hasil: <c:out value="${status}"/></h2>
    <p><c:out value="${message}"/></p>
</c:if>
</body>
</html>
