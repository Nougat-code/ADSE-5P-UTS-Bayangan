package com.tugas.utsbayangan1.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal FOUR = BigDecimal.valueOf(4);
    private static final MathContext ROOT_CONTEXT = MathContext.DECIMAL128;

    public SalaryResult calculateSalary(
            BigDecimal weeklyHours,
            BigDecimal standardHours,
            BigDecimal hourlyRate,
            BigDecimal overtimeMultiplier,
            BigDecimal weeklyExpenses) {
        BigDecimal regularHours = weeklyHours.min(standardHours);
        BigDecimal overtimeHours = weeklyHours.subtract(regularHours);
        BigDecimal regularPay = money(regularHours.multiply(hourlyRate));
        BigDecimal overtimePay = money(
                overtimeHours.multiply(hourlyRate).multiply(overtimeMultiplier));
        BigDecimal totalIncome = money(regularPay.add(overtimePay));
        BigDecimal expenses = money(weeklyExpenses);
        BigDecimal balance = money(totalIncome.subtract(expenses));

        String status;
        String message;
        if (balance.compareTo(ZERO) > 0) {
            status = "Bisa menabung";
            message = "Bisa menabung sebesar Rp " + format(balance) + ".";
        } else if (balance.compareTo(ZERO) == 0) {
            status = "Tidak bisa menabung";
            message = "Pemasukan sama dengan pengeluaran, jadi tidak ada sisa untuk ditabung.";
        } else {
            status = "Cari tambahan";
            message = "Cari tambahan sebesar Rp " + format(balance.abs()) + ".";
        }

        return new SalaryResult(
                regularHours.stripTrailingZeros(),
                overtimeHours.stripTrailingZeros(),
                regularPay,
                overtimePay,
                totalIncome,
                expenses,
                balance,
                status,
                message);
    }

    public QuadraticResult solveQuadratic(BigDecimal a, BigDecimal b, BigDecimal c) {
        BigDecimal discriminant = b.multiply(b).subtract(FOUR.multiply(a).multiply(c));
        BigDecimal denominator = TWO.multiply(a);
        if (discriminant.compareTo(ZERO) > 0) {
            BigDecimal squareRoot = discriminant.sqrt(ROOT_CONTEXT);
            BigDecimal rootOne = b.negate().add(squareRoot).divide(denominator, ROOT_CONTEXT);
            BigDecimal rootTwo = b.negate().subtract(squareRoot).divide(denominator, ROOT_CONTEXT);
            return new QuadraticResult(
                    "real",
                    "Persamaan memiliki dua akar real berbeda.",
                    format(rootOne),
                    format(rootTwo));
        }
        if (discriminant.compareTo(ZERO) == 0) {
            BigDecimal root = b.negate().divide(denominator, ROOT_CONTEXT);
            String formattedRoot = format(root);
            return new QuadraticResult(
                    "repeated",
                    "Persamaan memiliki akar real kembar.",
                    "x = " + formattedRoot,
                    "x = " + formattedRoot);
        }

        BigDecimal realPart = b.negate().divide(denominator, ROOT_CONTEXT);
        BigDecimal imaginaryPart = discriminant.negate().sqrt(ROOT_CONTEXT)
                .divide(denominator.abs(), ROOT_CONTEXT);
        return new QuadraticResult(
                "complex",
                "Persamaan memiliki dua akar kompleks.",
                format(realPart) + " + " + format(imaginaryPart) + "i",
                format(realPart) + " - " + format(imaginaryPart) + "i");
    }

    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private String format(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }

    public record SalaryResult(
            BigDecimal regularHours,
            BigDecimal overtimeHours,
            BigDecimal regularPay,
            BigDecimal overtimePay,
            BigDecimal totalIncome,
            BigDecimal weeklyExpenses,
            BigDecimal balance,
            String savingsStatus,
            String savingsMessage) {
    }

    public record QuadraticResult(
            String kind, String description, String rootOne, String rootTwo) {
    }
}
