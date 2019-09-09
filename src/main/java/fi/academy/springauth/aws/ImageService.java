package fi.academy.springauth.aws;

import fi.academy.springauth.appUser.AppUserRepository;
import fi.academy.springauth.images.ImageEntity;
import fi.academy.springauth.images.ImageRepository;
import fi.academy.springauth.images.metadata.MetadataService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
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
public class ImageService implements fi.academy.springauth.utils.ImageService {

    @Autowired
    private AmazonS3Client amazonS3Client;

    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;
    private final MetadataService metadataService;

    @Autowired
    public ImageService(ImageRepository imageRepository, ResourceLoader resourceLoader, AppUserRepository appUserRepository, MetadataService metadataService, AmazonS3Client amazonClient) {
        this.imageRepository = imageRepository;
        this.resourceLoader = resourceLoader;
        this.metadataService = metadataService;
        this.amazonS3Client = amazonClient;
    }

    /**
     * Creates new image, uploads it to S3-bucket and reads and saves metadaa from picture
     * @param file to be saved
     * @return ImageEntity created ImageEntity with saved metadata
     * @throws IOException
     */
    public ImageEntity createImage(MultipartFile file) throws IOException {
        ImageEntity created = null;
        long time = System.currentTimeMillis();

        if (!file.isEmpty()) {
            String newFile = amazonS3Client.uploadFileToS3Bucket(file, true);
            created = imageRepository.save(new ImageEntity(newFile));
            JSONObject metadatalist = metadataService.inputstremMetaReader(file.getInputStream());
//            readMetadata(file, created, newFile);
            created.setMetadatalist(metadatalist);
        }
        return created;

    }

    /**
     * Converts multipartfile to new File, reads metadata from picture, sets metadata to ImageEntity and
     * deletes local copy of new file
     * @param multipartFile File to be read/converted
     * @param created Imageentity created at
     * @param fileName Filename (string) of created image
     */
    private void readMetadata(MultipartFile multipartFile, ImageEntity created, String fileName) {

        try {
            File file = new File(fileName);
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
            created.setMetadatalist(metadataService.metadataReader(file));
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes picture from DB and S3-bucket
     * @param id Id of deletable image
     * @param user Principal user to validation
     * @return ResponseEntity.noContent if deleted, Bad request or not found on bad request
     */
    public ResponseEntity<?> deleteImage(long id, Principal user) {
        Optional<ImageEntity> currentImage = Optional.ofNullable(imageRepository.findById(id));

        if (!currentImage.isPresent()){
            return new ResponseEntity<>("Error: there is no picture with chosen id", HttpStatus.NOT_FOUND);

        }
        if (currentImage.get().getPhotoshoot().getCreator().getUsername().equals(user.getName())) {
            ImageEntity deleteImage = currentImage.get();
            imageRepository.delete(deleteImage);
            amazonS3Client.deleteFileFromS3Bucket(deleteImage.getUrl());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
    }



}

