<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Kalkulator Gaji John Travolta</title>
    <fmt:setLocale value="id_ID"/>
</head>
<body>
<nav>
    <a href="<c:url value='/'/>">Beranda</a> |
    <a href="<c:url value='/john'/>">Kalkulator gaji</a> |
    <a href="<c:url value='/kuadrat'/>">Persamaan kuadrat</a>
</nav>
<h1>Kalkulator Gaji John Travolta</h1>
<p>Masukkan jam kerja dan pengeluaran untuk menghitung gaji mingguan.</p>

<c:if test="${not empty errors}">
    <div role="alert">
        <strong>Input tidak valid:</strong>
        <ul><c:forEach var="error" items="${errors}"><li><c:out value="${error}"/></li></c:forEach></ul>
    </div>
</c:if>

<form action="<c:url value='/john'/>" method="post">
    <p>
        <label for="weeklyHours">Total jam kerja per minggu:</label><br>
        <input id="weeklyHours" name="weeklyHours" type="number" min="0" step="any" required value="<c:out value='${weeklyHours}'/>"> jam
    </p>
    <p>
        <label for="standardHours">Jam kerja standar:</label><br>
        <input id="standardHours" name="standardHours" type="number" min="0.01" step="any" required value="<c:out value='${standardHours}'/>"> jam
    </p>
    <p>
        <label for="hourlyRate">Rate normal:</label><br>
        Rp <input id="hourlyRate" name="hourlyRate" type="number" min="0" step="any" required value="<c:out value='${hourlyRate}'/>"> per jam
    </p>
    <p>
        <label for="overtimeMultiplier">Multiplier lembur:</label><br>
        <input id="overtimeMultiplier" name="overtimeMultiplier" type="number" min="0.01" step="any" required value="<c:out value='${overtimeMultiplier}'/>"> kali rate normal
    </p>
    <p>
        <label for="weeklyExpenses">Pengeluaran mingguan:</label><br>
        Rp <input id="weeklyExpenses" name="weeklyExpenses" type="number" min="0" step="any" required value="<c:out value='${weeklyExpenses}'/>">
    </p>
    <button type="submit">Hitung gaji</button>
</form>

<c:if test="${not empty totalIncome}">
    <hr>
    <h2>Hasil Perhitungan</h2>
    <p>Jam reguler: <c:out value="${regularHours}"/> jam</p>
    <p>Gaji reguler: Rp <fmt:formatNumber value="${regularPay}" type="number" maxFractionDigits="2" minFractionDigits="0"/></p>
    <p>Jam lembur: <c:out value="${overtimeHours}"/> jam</p>
    <p>Gaji lembur: Rp <fmt:formatNumber value="${overtimePay}" type="number" maxFractionDigits="2" minFractionDigits="0"/></p>
    <p><strong>Total pemasukan: Rp <fmt:formatNumber value="${totalIncome}" type="number" maxFractionDigits="2" minFractionDigits="0"/></strong></p>
    <p>Pengeluaran mingguan: Rp <fmt:formatNumber value="${weeklyExpensesResult}" type="number" maxFractionDigits="2" minFractionDigits="0"/></p>
    <h3><c:out value="${savingsStatus}"/></h3>
    <p><c:out value="${savingsMessage}"/></p>
</c:if>

<p><a href="<c:url value='/'/>">Kembali ke menu</a></p>
</body>
</html>
