package fi.academy.springauth.content;

import fi.academy.springauth.images.ImageEntity;
import fi.academy.springauth.images.metadata.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Profile("dev")
public class LocalImageService implements fi.academy.springauth.utils.ContentImageService {

    @Value(value = "${UPLOAD_ROOT}")
    private String UPLOAD_ROOT;

    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private ContentImageRepository contentImageRepository;
    @Autowired
    private MetadataService metadataService;

    /**
     * Creates new ContentImageEntity, reads metadata from picture and returns created object, used in local saving
     *
     * @param file Multipartfile to be saved
     * @return ContentImageEntity with metadata
     * @throws IOException
     */
    @Override
    public ContentImageEntity createContentImage(MultipartFile file) throws IOException {
        ContentImageEntity created = null;
        long time = System.currentTimeMillis();

        if (!file.isEmpty()) {
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, time + file.getOriginalFilename()));
            created = contentImageRepository.save(new ContentImageEntity(time + file.getOriginalFilename()));
            readContentImageMetadata(file, created, created.getUrl());
            System.out.println(created.getMetadata());

        }
        return created;
    }

    private void readContentImageMetadata(MultipartFile multipartFile, ContentImageEntity created, String fileName) {

        try {
            File file = new File(fileName);
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
            created.setMetadata(metadataService.metadataReader(file));
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<?> deleteContentImage(long id, Principal user) {
        Optional<ContentEntity> currentContent = contentRepository.findById(id);
        System.out.println(id);
        System.out.println(currentContent.get().getId());
        if (!currentContent.isPresent()) {
            return new ResponseEntity<>("Error: there is no content with chosen id", HttpStatus.NOT_FOUND);

        }
        if (currentContent.get().getCreator().getUsername().equals(user.getName())) {
            System.out.println(currentContent.get().getCreator().getUsername());
            contentRepository.delete(currentContent.get());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
    }
}
