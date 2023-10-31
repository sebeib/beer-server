package de.seb.beer.server.service;

import de.seb.beer.server.domain.Beer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class OfferService {

    private LocalDate offersFetched = null;
    private Map<String, List<Beer>> store;

    public Map<String, List<Beer>> getOffers() {
        return store;
    }

    public void storeOffers(Map<String, List<Beer>> offers) {
        store = offers;
        offersFetched = LocalDate.now();
    }

    public LocalDate getOffersFetched() {
        return offersFetched;
    }

}
