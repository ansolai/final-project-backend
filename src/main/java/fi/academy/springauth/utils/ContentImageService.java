package fi.academy.springauth.utils;

import fi.academy.springauth.content.ContentImageEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface ContentImageService {

    ContentImageEntity createContentImage(MultipartFile file) throws IOException;
    ResponseEntity<?> deleteContentImage(long id, Principal user);

}
