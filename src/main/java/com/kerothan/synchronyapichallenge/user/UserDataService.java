package com.kerothan.synchronyapichallenge.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserDataService {

    @Value("${imgur.client-id}")
    private String clientId;

    private final UserDataRepository repository;

    private final RestTemplate restTemplate;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    UserDataService(RestTemplate restTemplate, UserDataRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    List<User> findAll() {return repository.findAll();}
    // end::get-aggregate-root[]

    // Create New user
    User newUser(User newUserData) {
        String passEncode = encoder.encode(newUserData.getPassword());
        newUserData.setPassword(passEncode);
        return repository.save(newUserData);
    }

    // Find Single User
    User findUserDataById(Long id) {return repository.findById(id)
            .orElseThrow(() -> new UserDataNotFoundException(id));}

    User findByUsername(String authstring) {
        return authenticateUser(authstring);
    }

    User authenticateUser(String authstring) {
        String creds = authstring.substring("Basic".length()).trim();
        byte[] decoded = Base64.getDecoder().decode(creds);
        String decodedString = new String(decoded);
        final String[] values = decodedString.split(":",2);
        User user = repository.findByUsername(values[0]);
        if (user == null) throw new UserDataNotFoundException(values[0]);
        if (encoder.matches(values[1],user.getPassword())) {
            user.setPassword(values[1]);
            return user;
        }
        return null;
    }

    // Update Single user
    User updateUser(String authstring, User newUserData) {
        User user = authenticateUser(authstring);
        user.setFullname(newUserData.getFullname());
        user.setEmail(newUserData.getEmail());
        user.setPassword(encoder.encode(newUserData.getPassword()));
        user.setUsername(newUserData.getUsername());
        return repository.save(user);
    }

    public ResponseEntity<String> newImage(MultipartFile file, String authstring) {
        try {
            byte[] imageData = file.getBytes();
            Map<String, String> imgurResponse = uploadImage(imageData);
            String imageUrl = imgurResponse.get("link");
            String imageDeleteHash = imgurResponse.get("deletehash");

            User user = authenticateUser(authstring);

            String passEncode = encoder.encode(user.getPassword());
            user.setPassword(passEncode);

            user.setImgurl(imageUrl);

            repository.save(user);

            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public Map<String, String> uploadImage(byte[] imageData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID 3b0573a000a34fe");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageData);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject("https://api.imgur.com/3/image", requestEntity, String.class);
        return extractImageDetails(response);
    }

    private Map<String, String> extractImageDetails(String response) {
        Map<String, String> details = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.get("data");
            if (dataNode != null) {
                JsonNode imageUrlNode = dataNode.get("link");
                JsonNode deleteHashNode = dataNode.get("deletehash");
                if (imageUrlNode != null) {
                    details.put("link", imageUrlNode.asText());
                }
                if (deleteHashNode != null) {
                    details.put("deletehash", deleteHashNode.asText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }

    // Delete Single User
    void deleteUserData(long id) { repository.deleteById(id); }

}