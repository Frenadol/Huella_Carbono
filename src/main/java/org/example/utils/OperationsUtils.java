package org.example.utils;

import org.example.entities.Huella;
import org.example.entities.Categoria;
import org.example.entities.Usuario;
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

/**
 * Utility class for various operations related to environmental impact calculations.
 */
public class OperationsUtils {
    private static FingerprintService fingerprintService = new FingerprintService();

    /**
     * Calculates the total impact for all footprints.
     *
     * @param huellas the list of Huella entities.
     * @return the total impact as a BigDecimal.
     */
    public static BigDecimal calculateImpactForAllFootprints(List<Huella> huellas) {
        BigDecimal totalImpact = BigDecimal.ZERO;
        for (Huella huella : huellas) {
            BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
            BigDecimal impacto = huella.getValor().multiply(factorEmision);
            totalImpact = totalImpact.add(impacto);
        }
        return totalImpact.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the impact for all categories.
     *
     * @param huellas the list of Huella entities.
     * @return a map of category names to their impact values.
     */
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

    /**
     * Calculates the monthly impact.
     *
     * @param huellas the list of Huella entities.
     * @return a map of week numbers to their impact values.
     */
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

    /**
     * Calculates the weekly impact.
     *
     * @param huellas the list of Huella entities.
     * @return the total weekly impact as a BigDecimal.
     */
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

    /**
     * Calculates the average impact by category.
     *
     * @param allHuellas the list of all Huella entities.
     * @return a map of category names to their average impact values.
     */
    public static Map<String, BigDecimal> calculateAverageImpactByCategory(List<Huella> allHuellas) {
        Map<String, BigDecimal> totalImpactByCategory = new HashMap<>();
        Map<String, Integer> countByCategory = new HashMap<>();
        for (Huella huella : allHuellas) {
            String category = huella.getIdActividad().getIdCategoria().getNombre();
            BigDecimal valor = huella.getValor();
            totalImpactByCategory.put(category, totalImpactByCategory.getOrDefault(category, BigDecimal.ZERO).add(valor));
            countByCategory.put(category, countByCategory.getOrDefault(category, 0) + 1);
        }
        Map<String, BigDecimal> averageImpactByCategory = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : totalImpactByCategory.entrySet()) {
            String category = entry.getKey();
            BigDecimal totalImpact = entry.getValue();
            int count = countByCategory.get(category);
            averageImpactByCategory.put(category, totalImpact.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_UP));
        }
        return averageImpactByCategory;
    }

    /**
     * Calculates the average impact for a user.
     *
     * @param usuario the user to calculate the impact for.
     * @return the average impact as a BigDecimal.
     */
    public static BigDecimal calculateAverageImpactForUser(Usuario usuario) {
        List<Huella> huellas = fingerprintService.viewFingerPrints(usuario);
        Map<String, BigDecimal> averageImpactMap = calculateAverageImpactByCategory(huellas);
        if (averageImpactMap.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalImpact = averageImpactMap.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageImpact = totalImpact.divide(new BigDecimal(averageImpactMap.size()), RoundingMode.HALF_UP);
        return averageImpact.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the total impact for a user.
     *
     * @param usuario the user to calculate the impact for.
     * @return the total impact as a BigDecimal.
     */
    public static BigDecimal calculateImpactoTotal(Usuario usuario) {
        List<Huella> huellas = fingerprintService.viewFingerPrints(usuario);
        BigDecimal totalImpact = huellas.stream()
                .map(huella -> {
                    BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                    return huella.getValor().multiply(factorEmision);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalImpact.setScale(2, RoundingMode.HALF_UP);
    }
}