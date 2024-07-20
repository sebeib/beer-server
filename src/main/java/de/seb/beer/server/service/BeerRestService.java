package de.seb.beer.server.service;

import de.seb.beer.server.domain.Beer;
import de.seb.beer.server.domain.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class BeerRestService {

    private static final Logger LOG = LoggerFactory.getLogger(BeerRestService.class);
    private OfferService offerService;
    private ScrapeService scrapeService;

    public BeerRestService(OfferService offerService, ScrapeService scrapeService) {
        this.offerService = offerService;
        this.scrapeService = scrapeService;
    }

    @GetMapping("poll")
    @CrossOrigin(origins = "*")
    public RestResponse poll(@RequestParam("zip") String zip) throws Exception {
        if(zip == null) {
            throw new RuntimeException("Zip code is missing.");
        }

        LOG.info("Start polling discounts ...");
        LocalDate today = LocalDate.now();
        LocalDate lastFetched = offerService.getOffersFetched(zip);

        if(lastFetched == null || today.isAfter(lastFetched)) {
            LOG.info("Fetching new offers ...");
            Map<String, List<Beer>> offers = scrapeService.scrape(zip);
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

    record RestResponse(LocalDate lastFetched, List<Discount> offers) {}

}
