package ws.probal.urlshortener.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.probal.urlshortener.model.request.UrlRequest;
import ws.probal.urlshortener.model.response.OriginalUrlResponse;
import ws.probal.urlshortener.model.response.ShortenedUrlResponse;
import ws.probal.urlshortener.service.UrlShortenerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/url-shortener")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ResponseEntity<ShortenedUrlResponse> shortTheUrl(@RequestBody UrlRequest request) {
        ShortenedUrlResponse response = urlShortenerService.shortTheUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{key}")
    public ResponseEntity<OriginalUrlResponse> getUrl(@PathVariable String key) {
        OriginalUrlResponse response = urlShortenerService.getUrlByKey(key);
        return ResponseEntity.ok(response);
    }
}
