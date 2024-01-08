package de.seb.beer.server.domain;

import java.util.List;

public record Discount(String store, List<Beer> offers) {

}
