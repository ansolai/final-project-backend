package fi.academy.springauth.content;

import fi.academy.springauth.aws.AmazonS3Client;
import fi.academy.springauth.images.metadata.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Service
@Profile("prod")
public class AwsImageService implements fi.academy.springauth.utils.ContentImageService {

    @Autowired
    private AmazonS3Client amazonS3Client;
    @Autowired
    ContentImageRepository contentImageRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    MetadataService metadataService;

    @Override
    public ContentImageEntity createContentImage(MultipartFile file) throws IOException {
        ContentImageEntity created = null;

        if (!file.isEmpty()) {
            String newFile = amazonS3Client.uploadFileToS3Bucket(file, false);
            created = contentImageRepository.save(new ContentImageEntity(newFile));
            created.setMetadata(metadataService.inputstremMetaReader(file.getInputStream()));
//            readContentImageMetadata(file, created, created.getUrl());
        }
        return created;
    }

    @Override
    public ResponseEntity<?> deleteContentImage(long id, Principal user) {
        Optional<ContentEntity> currentImage = contentRepository.findById(id);

        if (!currentImage.isPresent()){
            return new ResponseEntity<>("Error: there is no content with chosen id", HttpStatus.NOT_FOUND);

        }
        if (currentImage.get().getCreator().getUsername().equals(user.getName())) {
            ContentEntity deleteImage = currentImage.get();
            contentRepository.delete(deleteImage);
            amazonS3Client.deleteFileFromS3Bucket(deleteImage.getImage().getUrl());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);

    }

    public void readContentImageMetadata(MultipartFile multipartFile, ContentImageEntity created, String fileName) {

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
}
