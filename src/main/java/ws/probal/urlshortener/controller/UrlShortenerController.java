package ws.probal.urlshortener.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.probal.urlshortener.common.response.ErrorResponse;
import ws.probal.urlshortener.model.entity.redis.Url;
import ws.probal.urlshortener.model.request.UrlRequest;
import ws.probal.urlshortener.model.response.OriginalUrlResponse;
import ws.probal.urlshortener.model.response.ShortenedUrlResponse;
import ws.probal.urlshortener.service.UrlShortenerService;

import static ws.probal.urlshortener.service.UrlShortenerService.validateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/api/v1/url-shortener")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ResponseEntity shortTheUrl(@RequestBody UrlRequest request) {

        if (!validateRequest(request)) {
            ErrorResponse error = new ErrorResponse("url", request.getUrl(), " is not valid");
            return ResponseEntity.badRequest().body(error);
        }

        ShortenedUrlResponse response = urlShortenerService.shortTheUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{key}")
    public ResponseEntity getUrl(@PathVariable String key) {
        OriginalUrlResponse response = urlShortenerService.getUrlByKey(key);
        return ResponseEntity.ok(response);
    }
}
