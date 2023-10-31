package de.seb.beer.server.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record Validity(
        ZonedDateTime from,
        ZonedDateTime to
) { }