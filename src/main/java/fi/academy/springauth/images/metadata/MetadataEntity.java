//package fi.academy.springauth.images.metadata;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import fi.academy.springauth.images.ImageEntity;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//public class MetadataEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @JsonIgnore
//    private long id;
//
//    @ManyToOne
//    @JoinColumn(name = "image_id")
//    @JsonIgnoreProperties("metadatalist")
//    @JsonIgnore
//    private ImageEntity image;
//    private String metadata;
//
//    public MetadataEntity() {
//    }
//
//    public ImageEntity getImage() {
//        return image;
//    }
//
//    public void setImage(ImageEntity image) {
//        this.image = image;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getMetadata() {
//        return metadata;
//    }
//
//    public void setMetadata(String metadata) {
//        this.metadata = metadata;
//    }
//}
//