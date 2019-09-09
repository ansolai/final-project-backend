package fi.academy.springauth.photoShoot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fi.academy.springauth.appUser.AppUserEntity;
import fi.academy.springauth.images.ImageEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.management.Query.value;

@Entity
public class PhotoshootPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String header;
    private Date date;
    private String location;
    private Double latitude;
    private Double longitude;
    @Column(length = 8000)
    private String description;
    @Column(length = 8000)
    private String notes;
    private String participants;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photoshoot")
    @JsonIgnoreProperties("photoshoot")
    @Where(clause = "reference = true")
    private List<ImageEntity> referencePictures = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photoshoot")
    @JsonIgnoreProperties("photoshoot")
    @Where(clause = "reference = false")
    private List<ImageEntity> readyPictures = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "app_user_entity_id")
    @JsonIgnoreProperties("plans")
    private AppUserEntity creator;

    public PhotoshootPlanEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUserEntity getCreator() {
        return creator;
    }

    public void setCreator(AppUserEntity creator) {
        this.creator = creator;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getParticipants() {
        return participants; }

    public void setParticipants(String participants) {
        this.participants = participants; }

    public List<ImageEntity> getReferencePictures() {
        return referencePictures; }

    public void setReferencePictures(List<ImageEntity> referencePictures) {
        this.referencePictures = referencePictures;
    }

    public List<ImageEntity> getReadyPictures() {
        return readyPictures;
    }

    public void setReadyPictures(List<ImageEntity> readyPictures) {
        this.readyPictures = readyPictures;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
