package fi.academy.springauth.appUser.friend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fi.academy.springauth.appUser.AppUserEntity;

import javax.persistence.*;

@Entity
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    @JsonIgnoreProperties("friends")
    private AppUserEntity friend;

    @ManyToOne
    @JsonIgnoreProperties("friends")
    private AppUserEntity friendWith;

    public FriendEntity() {
    }

    public AppUserEntity getFriendWith() {
        return friendWith;
    }

    public FriendEntity(AppUserEntity friend, AppUserEntity friendWith) {
        this.friend = friend;
        this.friendWith = friendWith;
    }

    public void setFriendWith(AppUserEntity friendWith) {
        this.friendWith = friendWith;
    }

    public FriendEntity(AppUserEntity friend) {
        this.friend = friend;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserEntity getFriend() {
        return friend;
    }

    public void setFriend(AppUserEntity friend) {
        this.friend = friend;
    }
}
