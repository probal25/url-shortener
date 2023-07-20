package ws.probal.urlshortener.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ws.probal.urlshortener.model.entity.Url;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, String> {
    Optional<Url> findByShortKey(String key);
}
