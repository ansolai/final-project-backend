package fi.academy.springauth.content;

import com.amazonaws.services.rekognition.model.Label;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fi.academy.springauth.appUser.AppUserEntity;
import fi.academy.springauth.images.ImageEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class ContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String message;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("content")
    private ContentImageEntity image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_entity_id")
    @JsonIgnoreProperties(value = {"content", "plans"})
    private AppUserEntity creator;

    private Date date;

    private boolean featured;

    public ContentEntity() {
    }

    public ContentEntity(String message, ContentImageEntity image, AppUserEntity creator) {
        this.message = message;
        this.image = image;
        this.creator = creator;
    }

    public ContentEntity(String message, ContentImageEntity image, AppUserEntity creator, boolean featured) {
        this.message = message;
        this.image = image;
        this.creator = creator;
        this.featured = featured;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContentImageEntity getImage() {
        return image;
    }

    public void setImage(ContentImageEntity image) {
        this.image = image;
    }

    public AppUserEntity getCreator() {
        return creator;
    }

    public void setCreator(AppUserEntity creator) {
        this.creator = creator;
    }
}
