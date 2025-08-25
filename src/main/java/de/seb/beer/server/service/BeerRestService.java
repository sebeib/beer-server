package de.seb.beer.server.service;

import de.seb.beer.server.domain.Beer;
import de.seb.beer.server.domain.Discount;
import de.seb.beer.server.domain.raw.RawOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static de.seb.beer.server.service.ScrapeService.*;

@RestController
@RequestMapping("/")
public class BeerRestService {

    private static final Logger LOG = LoggerFactory.getLogger(BeerRestService.class);
    private final OfferService offerService;
    private final ScrapeService scrapeService;

    public BeerRestService(OfferService offerService, ScrapeService scrapeService) {
        this.offerService = offerService;
        this.scrapeService = scrapeService;
    }

    @GetMapping("poll/beer")
    @CrossOrigin(origins = "*")
    public RestResponse pollBeer(@RequestParam("zip") String zip) throws Exception {
        if(zip == null) {
            throw new RuntimeException("Zip code is missing.");
        }

        return poll("bier", zip, IS_BEER.and(IS_CRATE));
    }

    @GetMapping("poll/spezi")
    @CrossOrigin(origins = "*")
    public RestResponse pollSpezi(@RequestParam("zip") String zip) throws Exception {
        if(zip == null) {
            throw new RuntimeException("Zip code is missing.");
        }

        return poll("spezi", zip, IS_SPEZI);
    }

    @GetMapping("poll/monster")
    @CrossOrigin(origins = "*")
    public RestResponse pollMonsterEnergy(@RequestParam("zip") String zip) throws Exception {
        if(zip == null) {
            throw new RuntimeException("Zip code is missing.");
        }

        return poll("monster%20energy", zip, offer -> true);
    }

    private RestResponse poll(String query, String zip, Predicate<RawOffer.Result> filter) throws Exception {
        LOG.info("Start polling discounts ...");
        LocalDate today = LocalDate.now();
        LocalDate lastFetched = offerService.getOffersFetched(query+zip);

        if(lastFetched == null || today.isAfter(lastFetched)) {
            LOG.info("Fetching new offers ...");
            Map<String, List<Beer>> offers = scrapeService.scrape(query, zip, filter);
            offerService.storeOffers(offers, zip);
            LOG.info("... offers fetched.");
        }

        LOG.info("... finished polling discounts.");
        return new RestResponse(
                offerService.getOffersFetched(zip),
                offerService.getOffers().entrySet().stream()
                        .map(entry -> new Discount(entry.getKey(), entry.getValue())).
                        toList()
        );
    }

    public record RestResponse(LocalDate lastFetched, List<Discount> offers) {}

}
