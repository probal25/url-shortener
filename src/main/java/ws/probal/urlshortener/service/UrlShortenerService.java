package ws.probal.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ws.probal.urlshortener.common.exceptions.InvalidUrlException;
import ws.probal.urlshortener.common.exceptions.ResourceNotFoundException;
import ws.probal.urlshortener.common.utils.EncryptionUtils;
import ws.probal.urlshortener.model.entity.redis.Url;
import ws.probal.urlshortener.model.request.UrlRequest;
import ws.probal.urlshortener.model.response.OriginalUrlResponse;
import ws.probal.urlshortener.model.response.ShortenedUrlResponse;
import ws.probal.urlshortener.repository.UrlRepository;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlRepository urlRepository;

    @Value("${base.url}")
    private String baseUrl;
    private final static String getOriginalUrlEndpoint = "/api/v1/url-shortener/";

    public ShortenedUrlResponse shortTheUrl(UrlRequest request) {

        boolean validUrl = validateURL(request);
        if (!validUrl)
            throw new InvalidUrlException(request.getUrl());

        String originalUrl = request.getUrl();
        String key = EncryptionUtils.generateHash(originalUrl);

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setKey(key);
        url.setShortUrl(makeUrlShorter(key));
        url.setCreated(new Date());
        urlRepository.save(url);

        return ShortenedUrlResponse
                .builder()
                .shortUrl(url.getShortUrl())
                .build();
    }

    public OriginalUrlResponse getUrlByKey(String key) {
        Url url = urlRepository.findByKey(key).orElse(null);

        if (Objects.isNull(url)) {
            throw new ResourceNotFoundException(key);
        }
        return OriginalUrlResponse
                .builder()
                .originalUrl(url.getOriginalUrl())
                .build();
    }

    private boolean validateURL(UrlRequest request) {
        UrlValidator validator = new UrlValidator(new String[]{"http", "https"});
        return validator.isValid(request.getUrl());
    }

    private String makeUrlShorter(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append(getOriginalUrlEndpoint).append(key);
        return sb.toString();
    }
}
