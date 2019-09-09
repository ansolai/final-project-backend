package fi.academy.springauth.images.metadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import fi.academy.springauth.images.ImageRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetadataService {

    @Autowired
    private ImageRepository imageRepository;

    /**
     * Returns list of string containing metadata of checked image
     * @param image Image to be checked
     * @return List of metadata-strings
     */
    public JSONObject metadataReader(File image) {

        List<String> metadatalist = new ArrayList<>();
            JSONObject jo = new JSONObject();
        try {

            Metadata metadata = ImageMetadataReader.readMetadata(image);
            for (Directory directory : metadata.getDirectories()) {
                if (directory.getName().contains("Exif")) {
                    for (Tag tag : directory.getTags()) {
                        jo.put(tag.getTagName(), tag.getDescription());
                        metadatalist.add(tag.getTagName() + " : " + tag.getDescription());
                    }
                }
                for (String error : directory.getErrors()) {
                    System.err.println("ERROR: " + error);
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jo;
    }

    public JSONObject inputstremMetaReader(InputStream image) {

        List<String> metadatalist = new ArrayList<>();
        JSONObject jo = new JSONObject();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(image);
            for (Directory directory : metadata.getDirectories()) {
                if (directory.getName().contains("Exif")) {
                    for (Tag tag : directory.getTags()) {
                        jo.put(tag.getTagName(), tag.getDescription());
                        metadatalist.add(tag.getTagName() + " : " + tag.getDescription());
                    }
                }
                for (String error : directory.getErrors()) {
                    System.err.println("ERROR: " + error);
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jo;
    }

}
