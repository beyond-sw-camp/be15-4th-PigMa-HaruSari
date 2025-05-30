package com.pigma.harusari.task.schedule.command.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ScheduleUpdateRequest {

    @NotNull
    private Long categoryId;

    @NotBlank
    private String scheduleContent;

    @NotNull
    private LocalDate scheduleDate;

    @Builder
    public ScheduleUpdateRequest(Long categoryId, String scheduleContent, LocalDate scheduleDate) {
        this.categoryId = categoryId;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
    }
}