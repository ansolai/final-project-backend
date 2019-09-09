package fi.academy.springauth.content;

import com.amazonaws.services.rekognition.model.Label;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.simple.JSONObject;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ContentImageEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String url;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("image")
    private ContentEntity content;

//    @ElementCollection
    JSONObject metadata;

    @ElementCollection
    private List<Label> tags = new ArrayList<>();



    public ContentImageEntity() {
    }

    public ContentImageEntity(String url) {
        this.url = url;
    }


    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public List<Label> getTags() {
        return tags;
    }

    public void setTags(List<Label> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

}
