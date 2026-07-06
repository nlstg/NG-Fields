package tg.ngstars.interv.dto;

import java.time.OffsetDateTime;

public record UpdateScheduleRequest(
    OffsetDateTime departureTime,
    OffsetDateTime arrivalTime,
    OffsetDateTime startTime,
    OffsetDateTime endTime
) {}
