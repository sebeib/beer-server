package de.seb.beer.server.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record Beer(
        BigDecimal price,
        String description,
        String brand,
        String name,
        ZonedDateTime from,
        ZonedDateTime to,
        Retailer retailer
) { }