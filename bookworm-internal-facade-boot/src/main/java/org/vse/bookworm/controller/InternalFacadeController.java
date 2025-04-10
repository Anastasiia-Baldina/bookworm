package org.vse.bookworm.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.vse.bookworm.service.Router;

import java.net.URI;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/book-worm")
public class InternalFacadeController {
    private static final Logger log = LoggerFactory.getLogger(InternalFacadeController.class);
    private final Router router;
    private final RestTemplate restTemplate;

    public InternalFacadeController(Router router, RestTemplate restTemplate) {
        this.router = router;
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = "/{affinityKey}/**", produces = "application/json")
    public ResponseEntity<byte[]> proxy(@PathVariable("affinityKey") long affinityKey, RequestEntity<byte[]> request) {
        try {
            var host = router.route(affinityKey);
            var remotePath = "/book-worm/" + Stream.of(request.getUrl().getPath().split("/"))
                    .skip(3)
                    .collect(Collectors.joining("/"));
            URI uri = UriComponentsBuilder.fromUriString(host.endpoint())
                    .path(remotePath)
                    .query(request.getUrl().getRawQuery())
                    .build()
                    .toUri();
            RequestEntity<byte[]> requestCopy = new RequestEntity<>(
                    request.getBody(), request.getHeaders(), request.getMethod(),
                    uri, request.getType());
            return restTemplate.exchange(requestCopy, byte[].class);
        } catch (RestClientResponseException e) {
            log.error("Error during proxy request", e);
            return new ResponseEntity<>(e.getResponseBodyAsByteArray(),
                    e.getResponseHeaders(), e.getStatusCode());
        } catch (Exception e) {
            log.error("Error during proxy request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
