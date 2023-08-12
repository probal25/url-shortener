package ws.probal.urlshortener.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.probal.urlshortener.common.utils.ApplicationUtils;
import ws.probal.urlshortener.model.request.UrlRequest;
import ws.probal.urlshortener.model.response.OriginalUrlResponse;
import ws.probal.urlshortener.model.response.ShortenedUrlResponse;
import ws.probal.urlshortener.service.UrlShortenerService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApplicationUtils.userBasePath + "/url-shortener")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ResponseEntity<ShortenedUrlResponse> shortTheUrl(@RequestBody UrlRequest urlRequest) {
        ShortenedUrlResponse response = urlShortenerService.shortTheUrl(urlRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{key}")
    public ResponseEntity<OriginalUrlResponse> getUrl(@PathVariable String key) {
        OriginalUrlResponse response = urlShortenerService.getUrlByKey(key);
        return ResponseEntity.ok(response);
    }
}
