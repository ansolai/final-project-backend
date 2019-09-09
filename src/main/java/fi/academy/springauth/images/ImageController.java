package fi.academy.springauth.images;

import fi.academy.springauth.photoShoot.PhotoshootPlanEntity;
import fi.academy.springauth.photoShoot.PhotoshootPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import fi.academy.springauth.utils.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    PhotoshootPlanRepository photoshootPlanRepository;


    @PostMapping("/{id}")
    @Transactional
    public ResponseEntity<?> addImage(@RequestBody MultipartFile image, @PathVariable long id, Principal user) throws IOException {
        Optional<PhotoshootPlanEntity> PLE = photoshootPlanRepository.findById(id);

        if (!PLE.isPresent() || PLE.get().getCreator().getId() != id) {
            return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
        }
        ImageEntity newImage = imageService.createImage(image);
//        imageRepository.save(new ImageEntity(image.getOriginalFilename()));

        URI location = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/images/{id}")
                .buildAndExpand(newImage.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable long id, Principal user) {
        return imageService.deleteImage(id, user);
    }

}
