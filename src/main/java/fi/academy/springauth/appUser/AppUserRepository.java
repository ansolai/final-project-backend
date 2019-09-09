package fi.academy.springauth.appUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends CrudRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findByUsername(String user);
//    AppUserEntity findByUsername(String user);
    boolean existsByUsername(String Username);
    boolean existsByEmail(String email);
}
