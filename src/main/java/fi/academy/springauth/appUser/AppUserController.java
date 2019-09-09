package fi.academy.springauth.appUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserService appUserService;

    @GetMapping("")
    public Optional<AppUserEntity> getUser(Principal user) {
        return appUserRepository.findByUsername(user.getName());
    }

    // TODO: custom exception
    @GetMapping("/{id}")
    public AppUserEntity getOneUser(Principal user) {
        return appUserService.getFriendUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOwnDetails(@PathVariable long id,
                                            @RequestBody AppUserEntity appUser,
                                            Principal user) {

        return appUserService.editDetails(id, appUser, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id, Principal user) {
        return appUserService.deleteUser(id, user);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody AppUserEntity user) {
        return appUserService.signUp(user);
    }


}
