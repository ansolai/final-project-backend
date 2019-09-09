package fi.academy.springauth.photoShoot;

import fi.academy.springauth.appUser.AppUserEntity;
import fi.academy.springauth.appUser.AppUserRepository;
import fi.academy.springauth.images.ImageRepository;
import fi.academy.springauth.images.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/plans")
public class PhotoshootPlanController {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PhotoshootPlanRepository photoshootPlanRepository;
    @Autowired
    private PhotoshootPlanService photoshootPlanService;

    /**
     * Metodien toteutus PhotoshootPlanServicessa.
     */
    @GetMapping("")
    public List<PhotoshootPlanEntity> findbyCreator(Principal user){
        Optional<AppUserEntity> appuser = appUserRepository.findByUsername(user.getName());
        return photoshootPlanRepository.findByCreator(appuser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOnebyId(@PathVariable long id, Principal user){
        return photoshootPlanService.findOneById(id, user);
    }

    @PostMapping("")
    @Transactional
    public ResponseEntity<?> createPlan(@RequestParam(required = false) String header,
                                        @RequestParam(required = false) Date date,
                                        @RequestParam(required = false) String location,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(required = false) String notes,
                                        @RequestParam(required = false) String participants,
                                        @RequestParam(required = false) Double longitude,
                                        @RequestParam(required = false) Double latitude,
                                        @RequestParam(required = false) MultipartFile image1,

                                        @RequestParam(required = false) MultipartFile image2,
                                        @RequestParam(required = false) MultipartFile image3,
                                        @RequestParam(required = false) MultipartFile image4,
                                        @RequestParam(required = false) MultipartFile image5,
                                        Principal user) throws IOException {
        return photoshootPlanService.createPlan(header, date, location, description, notes, participants, longitude, latitude, image1, image2, image3, image4, image5, user);
    }

    @PutMapping("/{id}/pictures")
    public ResponseEntity<?> addPictures(@PathVariable long id, @RequestParam(required = false) MultipartFile image1,
                                         @RequestParam(required = false) MultipartFile image2,
                                         @RequestParam(required = false) MultipartFile image3,
                                         @RequestParam(required = false) MultipartFile image4,
                                         @RequestParam(required = false) MultipartFile image5, Principal user) throws IOException {
        return photoshootPlanService.addPictures(id, image1, image2, image3, image4, image5, user);
    }

    @PutMapping("/{id}/readypictures")
    public ResponseEntity<?> addReadyPictures(@PathVariable long id, @RequestParam(required = false) MultipartFile image1,
                                         @RequestParam(required = false) MultipartFile image2,
                                         @RequestParam(required = false) MultipartFile image3,
                                         @RequestParam(required = false) MultipartFile image4,
                                         @RequestParam(required = false) MultipartFile image5, Principal user) throws IOException {
        return photoshootPlanService.addReadyPictures(id, image1, image2, image3, image4, image5, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable long id, Principal user) {
        return photoshootPlanService.deletePlan(id, user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPlan(@PathVariable long id, @RequestBody PhotoshootPlanEntity plan, Principal user) {
        return photoshootPlanService.editPlan(id, plan, user);
    }

}
