package ws.probal.urlshortener.model.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;


@Data
@RedisHash("url")
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    private String id;
    @Indexed
    private String originalUrl;
    @Indexed
    private String shortUrl;
    @Indexed
    private String key;

    private Date created;
}
