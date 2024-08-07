package de.seb.beer.server.service;

import de.seb.beer.server.domain.Beer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OfferService {

    private Map<String, LocalDate> offersFetched = new HashMap<>();
    private Map<String, List<Beer>> store;

    public Map<String, List<Beer>> getOffers() {
        return store;
    }

    public void storeOffers(Map<String, List<Beer>> offers, String zip) {
        store = offers;
        offersFetched.put(zip, LocalDate.now());
    }

    public LocalDate getOffersFetched(String zip) {
        return offersFetched.get(zip);
    }

}
