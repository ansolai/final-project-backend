package fi.academy.springauth.appUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Service
public class AppUserService {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AppUserRepository appUserRepository;

    public ResponseEntity<?> signUp(AppUserEntity user) {
        if (appUserRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
        } else if (appUserRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email is taken", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        appUserRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // TODO: Authorize request
    public AppUserEntity getFriendUser(Principal user) {
        return new AppUserEntity("not@do.ne", "test", "test");
    }

    public ResponseEntity<?> editDetails(@RequestParam long id,
                                                     @RequestBody AppUserEntity appUser,
                                                     Principal user) {
        Optional<AppUserEntity> currentUser = appUserRepository.findByUsername(user.getName());
        if (currentUser.get().getId() != id || !currentUser.isPresent()) {
            return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
        }
        appUser.setId(id);
        appUserRepository.save(appUser);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(long id, Principal user) {
        Optional<AppUserEntity> currentUser = appUserRepository.findByUsername(user.getName());
        System.out.println(user.getName());
        if (currentUser.isPresent() && currentUser.get().getId() == id) {
            appUserRepository.delete(currentUser.get());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
    }
}
