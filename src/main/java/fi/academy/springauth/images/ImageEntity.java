package fi.academy.springauth.images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fi.academy.springauth.photoShoot.PhotoshootPlanEntity;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean reference = true;

    private String url;
    

    @ManyToOne
    @JoinColumn(name="photoshoot_plan_entity_id")
    @JsonIgnoreProperties(value = {"referencePictures", "creator"})
    private PhotoshootPlanEntity photoshoot;

//    @ElementCollection
    private JSONObject metadatalist;


    public ImageEntity(String url) {
        this.url = url;

    }

    public ImageEntity() {
    }

    public JSONObject getMetadatalist() {
        return metadatalist;
    }

    public void setMetadatalist(JSONObject metadatalist) {
        this.metadatalist = metadatalist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PhotoshootPlanEntity getPhotoshoot() {
        return photoshoot;
    }

    public void setPhotoshoot(PhotoshootPlanEntity photoshoot) {
        this.photoshoot = photoshoot;
    }
}
