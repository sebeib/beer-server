package de.seb.beer.server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthRessource {

    @GetMapping("health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok().build();
    }

}