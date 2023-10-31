package de.seb.beer.server.service;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.seb.beer.server.domain.Beer;
import de.seb.beer.server.domain.RawOffer;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ScrapeService {

    private static final String URL   = System.getenv("URL");
    private static final String TOKEN = System.getenv("TOKEN");

    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final Gson gson = Converters.registerZonedDateTime(new GsonBuilder()).create();

    public Map<String, List<Beer>> scrape() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("X-ApiKey", TOKEN)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        RawOffer rawOffer = gson.fromJson(body, RawOffer.class);

        return rawOffer.results()
                .stream()
                .map(offer -> new Beer(
                        offer.price(),
                        offer.description(),
                        offer.brand().name(),
                        offer.product().name(),
                        offer.validityDates().get(0).from(),
                        offer.validityDates().get(0).to(),
                        offer.advertisers().get(0)
                ))
                .collect(Collectors.groupingBy(beer -> beer.retailer().name()));
    }

}
