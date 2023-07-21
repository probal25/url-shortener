package ws.probal.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ws.probal.urlshortener.common.exceptions.InvalidUrlException;
import ws.probal.urlshortener.common.exceptions.ResourceNotFoundException;
import ws.probal.urlshortener.common.utils.ApplicationUtils;
import ws.probal.urlshortener.common.utils.EncryptionUtils;
import ws.probal.urlshortener.model.entity.Url;
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
    private static final String GetOriginalUrlEndpoint = ApplicationUtils.userBasePath + "/url-shortener/";


    public ShortenedUrlResponse shortTheUrl(UrlRequest request) {

        boolean validUrl = validateURL(request);
        if (!validUrl)
            throw new InvalidUrlException(request.getUrl());

        String originalUrl = request.getUrl();
        String key = EncryptionUtils.generateHash(originalUrl);

        Url url = saveUrl(key, originalUrl);

        return ShortenedUrlResponse
                .builder()
                .shortUrl(url.getShortUrl())
                .build();
    }


    public OriginalUrlResponse getUrlByKey(String key) {
        Url url = findByKey(key);

        if (Objects.isNull(url)) {
            throw new ResourceNotFoundException(key);
        }
        return OriginalUrlResponse
                .builder()
                .originalUrl(url.getOriginalUrl())
                .build();
    }

    @CachePut(value = "urls", key = "#key", unless = "#result == null")
    public Url saveUrl(String key, String originalUrl) {
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortKey(key);
        url.setShortUrl(makeUrlShorter(key));
        url.setCreated(new Date());
        return urlRepository.save(url);
    }
    @Cacheable(value = "urls", key = "#key")
    public Url findByKey(String key) {
        return urlRepository.findByShortKey(key).orElse(null);
    }

    private boolean validateURL(UrlRequest request) {
        UrlValidator validator = new UrlValidator(new String[]{"http", "https"});
        return validator.isValid(request.getUrl());
    }

    private String makeUrlShorter(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append(GetOriginalUrlEndpoint).append(key);
        return sb.toString();
    }
}
