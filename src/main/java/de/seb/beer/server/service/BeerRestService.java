package de.seb.beer.server.service;

import de.seb.beer.server.domain.Beer;
import de.seb.beer.server.domain.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public RestResponse poll() throws Exception {
        LOG.info("Start polling discounts ...");
        LocalDate today = LocalDate.now();
        LocalDate lastFetched = offerService.getOffersFetched();

        if(lastFetched == null || today.isAfter(lastFetched)) {
            LOG.info("Fetching new offers ...");
            Map<String, List<Beer>> offers = scrapeService.scrape();
            offerService.storeOffers(offers);
            LOG.info("... offers fetched.");
        }

        LOG.info("... finished polling discounts.");
        return new RestResponse(
                offerService.getOffersFetched(),
                offerService.getOffers().entrySet().stream()
                        .map(entry -> new Discount(entry.getKey(), entry.getValue())).
                        toList()
        );
    }

    record RestResponse(LocalDate lastFetched, List<Discount> offers) {}

}
