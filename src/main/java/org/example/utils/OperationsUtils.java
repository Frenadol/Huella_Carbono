package org.example.utils;

import org.example.entities.Huella;
import org.example.entities.Categoria;
import org.example.services.FingerprintService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationsUtils {
    private static FingerprintService fingerprintService = new FingerprintService();


    public static BigDecimal calculateImpactForAllFootprints(List<Huella> huellas) {
        BigDecimal totalImpact = BigDecimal.ZERO;

        for (Huella huella : huellas) {
            BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
            BigDecimal impacto = huella.getValor().multiply(factorEmision);
            totalImpact = totalImpact.add(impacto);
        }

        return totalImpact.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateImpactForCategory(List<Huella> huellas, Categoria category) {
        BigDecimal totalImpact = BigDecimal.ZERO;

        for (Huella huella : huellas) {
            if (huella.getIdActividad().getIdCategoria().equals(category)) {
                BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                BigDecimal impacto = huella.getValor().multiply(factorEmision);
                totalImpact = totalImpact.add(impacto);
            }
        }

        return totalImpact.setScale(2, RoundingMode.HALF_UP);
    }

    public static Map<String, BigDecimal> calculateImpactForAllCategories(List<Huella> huellas) {
        Map<String, BigDecimal> categoryImpactMap = new HashMap<>();

        for (Huella huella : huellas) {
            String categoryName = huella.getIdActividad().getIdCategoria().getNombre();
            BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
            BigDecimal impacto = huella.getValor().multiply(factorEmision);

            categoryImpactMap.put(categoryName, categoryImpactMap.getOrDefault(categoryName, BigDecimal.ZERO).add(impacto));
        }

        categoryImpactMap.replaceAll((k, v) -> v.setScale(2, RoundingMode.HALF_UP));
        return categoryImpactMap;
    }

    public static Map<Integer, BigDecimal> calculateMonthlyImpact(List<Huella> huellas) {
        Map<Integer, BigDecimal> weeklyImpactMap = new HashMap<>();
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (Huella huella : huellas) {
            if (huella.getFecha().getMonth() == now.getMonth() && huella.getFecha().getYear() == now.getYear()) {
                int weekOfMonth = huella.getFecha().get(weekFields.weekOfMonth());
                BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                BigDecimal impacto = huella.getValor().multiply(factorEmision);
                weeklyImpactMap.putIfAbsent(weekOfMonth, BigDecimal.ZERO);
                weeklyImpactMap.put(weekOfMonth, weeklyImpactMap.get(weekOfMonth).add(impacto));
            }
        }

        weeklyImpactMap.replaceAll((k, v) -> v.setScale(2, RoundingMode.HALF_UP));
        return weeklyImpactMap;
    }

    public static BigDecimal calculateWeeklyImpact(List<Huella> huellas) {
        BigDecimal totalImpact = BigDecimal.ZERO;
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int currentWeek = now.get(weekFields.weekOfYear());

        for (Huella huella : huellas) {
            if (huella.getFecha().get(weekFields.weekOfYear()) == currentWeek && huella.getFecha().getYear() == now.getYear()) {
                BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                BigDecimal impacto = huella.getValor().multiply(factorEmision);
                totalImpact = totalImpact.add(impacto);
            }
        }

        return totalImpact.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateDailyImpact(List<Huella> huellas) {
        BigDecimal totalImpact = BigDecimal.ZERO;
        LocalDate now = LocalDate.now();

        for (Huella huella : huellas) {
            if (huella.getFecha().isEqual(now.atStartOfDay())) {
                BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                BigDecimal impacto = huella.getValor().multiply(factorEmision);
                totalImpact = totalImpact.add(impacto);
            }
        }

        return totalImpact.setScale(2, RoundingMode.HALF_UP);
    }

    public static Map<Integer, BigDecimal> calculateWeeklyImpactMap(List<Huella> huellas) {
        Map<Integer, BigDecimal> weeklyImpactMap = new HashMap<>();
        for (Huella huella : huellas) {
            int weekOfYear = huella.getFecha().get(WeekFields.of(Locale.getDefault()).weekOfYear());
            weeklyImpactMap.putIfAbsent(weekOfYear, BigDecimal.ZERO);
            weeklyImpactMap.put(weekOfYear, weeklyImpactMap.get(weekOfYear).add(huella.getValor()));
        }
        return weeklyImpactMap;
    }
    public static void calculateImpactForCategory(Categoria category) {
        List<Huella> allFingerprints = fingerprintService.viewFingerPrints();
        Map<String, List<Huella>> groupedByCategory = allFingerprints.stream()
                .collect(Collectors.groupingBy(huella -> huella.getIdActividad().getIdCategoria().getNombre()));

        BigDecimal totalImpact = groupedByCategory.getOrDefault(category.getNombre(), List.of()).stream()
                .map(Huella::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Total impact for category " + category.getNombre() + ": " + totalImpact);
    }
}