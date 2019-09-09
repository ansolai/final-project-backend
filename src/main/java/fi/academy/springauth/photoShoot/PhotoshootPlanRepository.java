package fi.academy.springauth.photoShoot;

import fi.academy.springauth.appUser.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoshootPlanRepository extends JpaRepository<PhotoshootPlanEntity, Long> {

    List<PhotoshootPlanEntity> findByCreator(Optional<AppUserEntity> user);
    PhotoshootPlanEntity findByCreator_Username(String username);
    //PhotoshootPlanRepository findById(long id);
}
