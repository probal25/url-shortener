package ws.probal.urlshortener.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "URL_INFO")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalUrl;

    private String shortUrl;

    private String shortKey;

    private Date created;
}
