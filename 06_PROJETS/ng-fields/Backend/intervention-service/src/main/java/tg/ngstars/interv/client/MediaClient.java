package tg.ngstars.interv.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class MediaClient {

    private final RestClient restClient;
    private final String mediaBaseUrl;

    public MediaClient(
            @Value("${media-service.url:http://localhost:8084}") String mediaBaseUrl) {
        this.mediaBaseUrl = mediaBaseUrl;
        this.restClient = RestClient.builder()
                .baseUrl(mediaBaseUrl + "/api/media")
                .build();
    }

    @SuppressWarnings("unchecked")
    public String uploadFile(MultipartFile file) {
        var body = restClient.post()
                .uri("/upload")
                .body(createMultipartBody(file))
                .retrieve()
                .body(Map.class);
        var filename = (String) body.get("filename");
        return mediaBaseUrl + "/api/media/" + filename;
    }

    @SuppressWarnings("unchecked")
    public String uploadBase64(String base64Data) {
        String data = base64Data.replaceAll("^data:image/[^;]+;base64,", "");
        var body = restClient.post()
                .uri("/upload-base64")
                .body(Map.of("data", data))
                .retrieve()
                .body(Map.class);
        var filename = (String) body.get("filename");
        return mediaBaseUrl + "/api/media/" + filename;
    }

    public void deleteFile(String filename) {
        restClient.delete()
                .uri("/{filename}", filename)
                .retrieve();
    }

    private org.springframework.http.HttpEntity<?> createMultipartBody(MultipartFile file) {
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);
        var body = new org.springframework.util.LinkedMultiValueMap<String, Object>();
        body.add("file", file.getResource());
        return new org.springframework.http.HttpEntity<>(body, headers);
    }
}
