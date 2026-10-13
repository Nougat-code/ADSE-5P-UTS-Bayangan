package com.tugas.utsbayangan1.controller;

import com.tugas.utsbayangan1.service.CalculatorService;
import com.tugas.utsbayangan1.service.PengajuanIzinService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    private static final Map<String, String> SALARY_DEFAULTS = Map.of(
            "weeklyHours", "40",
            "standardHours", "40",
            "hourlyRate", "15000",
            "overtimeMultiplier", "1.5",
            "weeklyExpenses", "600000");

    private final CalculatorService calculatorService;
    private final PengajuanIzinService pengajuanIzinService;

    public HomeController(
            CalculatorService calculatorService, PengajuanIzinService pengajuanIzinService) {
        this.calculatorService = calculatorService;
        this.pengajuanIzinService = pengajuanIzinService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/john")
    public String john(Model model) {
        model.addAllAttributes(SALARY_DEFAULTS);
        return "john";
    }

    @PostMapping("/john")
    public String calculateSalary(
            @RequestParam(defaultValue = "") String weeklyHours,
            @RequestParam(defaultValue = "") String standardHours,
            @RequestParam(defaultValue = "") String hourlyRate,
            @RequestParam(defaultValue = "") String overtimeMultiplier,
            @RequestParam(defaultValue = "") String weeklyExpenses,
            Model model) {
        Map<String, String> values = Map.of(
                "weeklyHours", weeklyHours,
                "standardHours", standardHours,
                "hourlyRate", hourlyRate,
                "overtimeMultiplier", overtimeMultiplier,
                "weeklyExpenses", weeklyExpenses);
        model.addAllAttributes(values);

        List<String> errors = new ArrayList<>();
        BigDecimal hours = parseNumber("Total jam kerja", weeklyHours, errors);
        BigDecimal standard = parseNumber("Jam kerja standar", standardHours, errors);
        BigDecimal rate = parseNumber("Rate normal", hourlyRate, errors);
        BigDecimal multiplier = parseNumber("Multiplier lembur", overtimeMultiplier, errors);
        BigDecimal expenses = parseNumber("Pengeluaran mingguan", weeklyExpenses, errors);

        validateRange(hours, "Total jam kerja", false, errors);
        validateRange(standard, "Jam kerja standar", true, errors);
        validateRange(rate, "Rate normal", false, errors);
        validateRange(multiplier, "Multiplier lembur", true, errors);
        validateRange(expenses, "Pengeluaran mingguan", false, errors);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "john";
        }

        CalculatorService.SalaryResult result =
                calculatorService.calculateSalary(hours, standard, rate, multiplier, expenses);
        model.addAttribute("regularHours", result.regularHours().toPlainString());
        model.addAttribute("overtimeHours", result.overtimeHours().toPlainString());
        model.addAttribute("regularPay", result.regularPay().toPlainString());
        model.addAttribute("overtimePay", result.overtimePay().toPlainString());
        model.addAttribute("totalIncome", result.totalIncome().toPlainString());
        model.addAttribute("weeklyExpensesResult", result.weeklyExpenses().toPlainString());
        model.addAttribute("balance", result.balance().abs().toPlainString());
        model.addAttribute("savingsStatus", result.savingsStatus());
        model.addAttribute("savingsMessage", result.savingsMessage());
        return "john";
    }

    @GetMapping("/kuadrat")
    public String quadratic(Model model) {
        model.addAttribute("a", "");
        model.addAttribute("b", "");
        model.addAttribute("c", "");
        return "kuadrat";
    }

    @PostMapping("/kuadrat")
    public String calculateQuadratic(
            @RequestParam(defaultValue = "") String a,
            @RequestParam(defaultValue = "") String b,
            @RequestParam(defaultValue = "") String c,
            Model model) {
        model.addAttribute("a", a);
        model.addAttribute("b", b);
        model.addAttribute("c", c);

        List<String> errors = new ArrayList<>();
        BigDecimal coefficientA = parseNumber("Koefisien a", a, errors);
        BigDecimal coefficientB = parseNumber("Koefisien b", b, errors);
        BigDecimal coefficientC = parseNumber("Koefisien c", c, errors);

        if (coefficientA != null && coefficientA.compareTo(BigDecimal.ZERO) == 0) {
            errors.add("Koefisien a tidak boleh 0 karena persamaan harus berderajat dua.");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "kuadrat";
        }

        CalculatorService.QuadraticResult result =
                calculatorService.solveQuadratic(coefficientA, coefficientB, coefficientC);
        model.addAttribute("resultKind", result.kind());
        model.addAttribute("resultDescription", result.description());
        model.addAttribute("rootOne", result.rootOne());
        model.addAttribute("rootTwo", result.rootTwo());
        return "kuadrat";
    }

    @GetMapping("/pengajuan-izin")
    public String pengajuanIzin(Model model) {
        model.addAttribute("days", "");
        model.addAttribute("studentName", "");
        model.addAttribute("reason", "");
        return "pengajuan-izin";
    }

    @PostMapping("/pengajuan-izin")
    public String submitPengajuanIzin(
            @RequestParam(defaultValue = "") String studentName,
            @RequestParam(defaultValue = "") String reason,
            @RequestParam(defaultValue = "") String days,
            Model model) {
        model.addAttribute("studentName", studentName);
        model.addAttribute("reason", reason);
        model.addAttribute("days", days);

        try {
            int requestedDays = Integer.parseInt(days.trim());
            if (requestedDays <= 0) {
                model.addAttribute("error", "Lama izin harus lebih besar dari 0 hari.");
                return "pengajuan-izin";
            }
            if (studentName.isBlank() || reason.isBlank()) {
                model.addAttribute("error", "Nama mahasiswa dan alasan wajib diisi.");
                return "pengajuan-izin";
            }

            PengajuanIzinService.Decision decision =
                    pengajuanIzinService.submit(studentName.trim(), reason.trim(), requestedDays);
            model.addAttribute("status", decision.status());
            model.addAttribute("message", decision.message());
        } catch (NumberFormatException exception) {
            model.addAttribute("error", "Lama izin harus berupa bilangan bulat.");
        }
        return "pengajuan-izin";
    }

    private BigDecimal parseNumber(String label, String value, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add(label + " wajib diisi.");
            return null;
        }
        try {
            BigDecimal number = new BigDecimal(value.trim());
            if (number.abs().compareTo(new BigDecimal("1000000000000")) > 0) {
                errors.add(label + " tidak boleh lebih dari 1.000.000.000.000.");
                return null;
            }
            return number;
        } catch (NumberFormatException exception) {
            errors.add(label + " harus berupa angka yang valid.");
            return null;
        }
    }

    private void validateRange(
            BigDecimal value, String label, boolean mustBePositive, List<String> errors) {
        if (value == null) {
            return;
        }
        int comparison = value.compareTo(BigDecimal.ZERO);
        if (mustBePositive && comparison <= 0) {
            errors.add(label + " harus lebih besar dari 0.");
        } else if (!mustBePositive && comparison < 0) {
            errors.add(label + " tidak boleh kurang dari 0.");
        }
    }
}