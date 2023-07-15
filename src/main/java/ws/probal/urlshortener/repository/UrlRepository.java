package ws.probal.urlshortener.repository;

import org.springframework.data.repository.CrudRepository;
import ws.probal.urlshortener.model.entity.redis.Url;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, String> {
    Optional<Url> findByKey(String key);
}
