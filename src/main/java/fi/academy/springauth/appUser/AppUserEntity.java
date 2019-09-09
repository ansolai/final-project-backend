package fi.academy.springauth.appUser;

import com.fasterxml.jackson.annotation.*;
import fi.academy.springauth.appUser.friend.FriendEntity;
import fi.academy.springauth.content.ContentEntity;
import fi.academy.springauth.photoShoot.PhotoshootPlanEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
@Table()
public class AppUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Not valid email")
    @Email
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "creator")
    @JsonIgnoreProperties(value = {"creator", "content"})
    private List<PhotoshootPlanEntity> plans;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "creator")
    @JsonIgnoreProperties(value = {"content", "creator"})
    private List<ContentEntity> content;
//
//    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("friend")
//    private List<FriendEntity> friends;

    public AppUserEntity() {
    }

    public AppUserEntity(@NotBlank @Email String email, String username, @NotBlank String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public List<ContentEntity> getContent() {
        return content;
    }

    public void setContent(List<ContentEntity> content) {
        this.content = content;
    }

    public AppUserEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PhotoshootPlanEntity> getPlans() {
        return plans;
    }

    public void setPlans(List<PhotoshootPlanEntity> plans) {
        this.plans = plans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUserEntity)) return false;
        AppUserEntity that = (AppUserEntity) o;
        return getId() == that.getId() &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getPlans(), that.getPlans());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getUsername(), getPassword(), getPlans());
    }
}
