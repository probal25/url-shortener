package ws.probal.urlshortener.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.probal.urlshortener.common.annotations.SensitiveData;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {

    @SensitiveData
    private String url;
}
