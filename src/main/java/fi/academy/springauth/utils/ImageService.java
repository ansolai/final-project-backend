package fi.academy.springauth.utils;

import fi.academy.springauth.images.ImageEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;

@Service
public interface ImageService {

    ImageEntity createImage(MultipartFile file) throws IOException;
    ResponseEntity<?> deleteImage(long id, Principal user);

}
