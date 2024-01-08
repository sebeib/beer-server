package de.seb.beer.server.domain.raw;

import de.seb.beer.server.domain.Retailer;

import java.math.BigDecimal;
import java.util.List;

public record RawOffer(
        List<Result> results
) {
    public record Result(
        Brand brand,
        List<Retailer> advertisers,
        BigDecimal price,
        String description,
        List<Validity>validityDates,
        Product product,
        List<Category> categories
    ) { }
}