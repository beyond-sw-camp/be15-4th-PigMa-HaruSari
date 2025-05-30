package com.pigma.harusari.statistics.query.dto.response;

import lombok.Builder;

@Builder
public record StatisticsCategoryRateResponse(
        String categoryName,
        String color,
        double achievementRate
) {
}