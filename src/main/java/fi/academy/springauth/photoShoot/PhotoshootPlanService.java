package fi.academy.springauth.photoShoot;

import fi.academy.springauth.appUser.AppUserEntity;
import fi.academy.springauth.appUser.AppUserRepository;
import fi.academy.springauth.images.ImageEntity;

import fi.academy.springauth.utils.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoshootPlanService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PhotoshootPlanRepository photoshootPlanRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ImageService imageService;


    public ResponseEntity<?> createPlan (@RequestParam(required = false) String header,
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
                            Principal user)throws IOException {
        Optional<AppUserEntity> appuser = appUserRepository.findByUsername(user.getName());
        if (appuser.get().getUsername().equals(user.getName())) {
            PhotoshootPlanEntity plan = new PhotoshootPlanEntity();
            plan.setHeader(header);
            plan.setDate(date);
            plan.setLocation(location);
            plan.setDescription(description);
            plan.setNotes(notes);
            plan.setParticipants(participants);
            plan.setCreator(appuser.get());
            plan.setLatitude(latitude);
            plan.setLongitude(longitude);
            if (image1 != null) {
                ImageEntity i = imageService.createImage(image1);
                i.setPhotoshoot(plan);
                //plan.getReferencePictures().add(i);
            }
            if (image2 != null) {
                ImageEntity i = imageService.createImage(image2);
                i.setPhotoshoot(plan);
                //plan.getReferencePictures().add(i);
            }
            if (image3 != null) {
                ImageEntity i = imageService.createImage(image3);
                i.setPhotoshoot(plan);
                //plan.getReferencePictures().add(i);
            }
            if (image4 != null) {
                ImageEntity i = imageService.createImage(image4);
                i.setPhotoshoot(plan);
                //plan.getReferencePictures().add(i);
            }
            if (image5 != null) {
                ImageEntity i = imageService.createImage(image5);
                i.setPhotoshoot(plan);
                //plan.getReferencePictures().add(i);
            }
            photoshootPlanRepository.save(plan);
            URI loc = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(8080)
                    .path("/plans/{id}")
                    .buildAndExpand(plan.getId())
                    .toUri();
            return ResponseEntity.created(loc).build();
        }
        return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
    }

    /**
     * Metodi poistaa yhden suunnitelman varmistuttuaan siitä, että käyttäjä on poistamassa omaa suunnitelmaansa.
     * Varmistuminen tapatuu vertaamalla suunnitelman kirjoittajan nimeä käyttäjän nimeen, joiden on oltava samat.
     */
    public ResponseEntity<?> deletePlan(long id, Principal user) {
        Optional<PhotoshootPlanEntity> currentPlan = photoshootPlanRepository.findById(id);
        if (currentPlan.get().getCreator().getUsername().equals(user.getName())) {
            photoshootPlanRepository.delete(currentPlan.get());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
    }
    /**
     * Muokkaa jo luotua suunnitelmaa muiden tietojen, paitsi kuvien osalta.
     * Polkuna käytetään id:tä. Suunnitelman luojan nimeä verrataan kirjautuneen käyttäjän nimeen, jotta varmistutaan,
     * että muokkaaja on suunnitelman luoja (voi olla vain yksi käyttäjä samalla usernamella). Haetaan ensin oikea suunnitelma
     * annetulla id:llä. Palauttaa muokatun suunnitelman ja OK statuksen mikäli muokkaus on sallittu.
     */
    public ResponseEntity<?> editPlan(long id, @RequestBody PhotoshootPlanEntity plan, Principal user) {
        Optional<PhotoshootPlanEntity> currentPlan = photoshootPlanRepository.findById(id);
       // Optional<AppUserEntity> currentUser = appUserRepository.findByUsername(user.getName());
        if (currentPlan.get().getCreator().getUsername().equals(user.getName())){
            AppUserEntity creator = currentPlan.get().getCreator();
            List <ImageEntity> images = currentPlan.get().getReferencePictures();
            plan.setId(id);
            plan.setCreator(creator);
            plan.setReferencePictures(images);
            photoshootPlanRepository.save(plan);
            return new ResponseEntity<>(plan, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not authorized", HttpStatus.BAD_REQUEST);
    }

    /** Kuvien lisäys olemassaolevalle suunnitelmalle. Kuvia voi lisätä kunnes kuvia on kaikkiaan 5.
     * Polkuparametrina käytetään suunnitelman id:tä ja verrataan sitä nykyisen suunnitelman id:hen.
     * Jotta tärkeitä tietoja ei häviä päivityksen yhteydessä, talletetaan id ja sunnitelman luoja.
     * Tehdään lisättävistä kuvista lista, ja tarkistetaan mahtuuko kaikki. Jos ei, pyydetään valitsemaan lisättävät.
     */
    public ResponseEntity<?> addPictures(@PathVariable long id, @RequestParam(required = false) MultipartFile image1,
                                         @RequestParam(required = false) MultipartFile image2,
                                         @RequestParam(required = false) MultipartFile image3,
                                         @RequestParam(required = false) MultipartFile image4,
                                         @RequestParam(required = false) MultipartFile image5, Principal user) throws IOException {
        Optional<PhotoshootPlanEntity> currentPlan = photoshootPlanRepository.findById(id);
        if (currentPlan.get().getId() == id) {
            PhotoshootPlanEntity plan = currentPlan.get();
            if (currentPlan.get().getCreator().getUsername().equals(user.getName())) {
                AppUserEntity creator = currentPlan.get().getCreator();
                plan.setId(id);
                plan.setCreator(creator);
                List<MultipartFile> pictures = new ArrayList<>();
                int sallitutKuvat = 5 - currentPlan.get().getReferencePictures().size();
                if (sallitutKuvat == 0) {
                    return new ResponseEntity<>("This plan already has the maximum amount of ready pictures. Please remove pictures if you want to add new ones.", HttpStatus.BAD_REQUEST);
                }
                if (image1 != null) {
                    pictures.add(image1);
                }
                if (image2 != null) {
                    pictures.add(image2);
                }
                if (image3 != null) {
                    pictures.add(image3);
                }
                if (image4 != null) {
                    pictures.add(image4);
                }
                if (image5 != null) {
                    pictures.add(image5);
                }
                if (pictures.size() > sallitutKuvat) {
                    return new ResponseEntity<>("There's space for only " + sallitutKuvat + " pictures. Choose the ones you want to add to your plan.", HttpStatus.BAD_REQUEST);
                } else {
                    for (MultipartFile kuva : pictures) {
                        ImageEntity a = imageService.createImage(kuva);
                        currentPlan.get().getReferencePictures().add(a);
                        a.setPhotoshoot(plan);
                    }
                    photoshootPlanRepository.save(plan);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Toteutuneiden kuvien lisäys, max 5 kuvaa kerrallaan ja kuvia mahtuu 5 per suunnitelma.
     * Toimii täsmälleen kuten referenssikuvien lisäys, paitsi että muutetaan kuvan oletus refekuvasta falseksi.
     */
    public ResponseEntity<?> addReadyPictures(@PathVariable long id, @RequestParam(required = false) MultipartFile image1,
                                         @RequestParam(required = false) MultipartFile image2,
                                         @RequestParam(required = false) MultipartFile image3,
                                         @RequestParam(required = false) MultipartFile image4,
                                         @RequestParam(required = false) MultipartFile image5, Principal user) throws IOException {
        Optional<PhotoshootPlanEntity> currentPlan = photoshootPlanRepository.findById(id);
        if (currentPlan.get().getId() == id) {
            PhotoshootPlanEntity plan = currentPlan.get();
            if (currentPlan.get().getCreator().getUsername().equals(user.getName())) {
                AppUserEntity creator = currentPlan.get().getCreator();
                plan.setId(id);
                plan.setCreator(creator);
                List<MultipartFile> pictures = new ArrayList<>();
                int sallitutKuvat = 5 - currentPlan.get().getReadyPictures().size();
                if (sallitutKuvat == 0) {
                    return new ResponseEntity<>("This plan already has the maximum amount of ready pictures. Please remove pictures if you want to add new ones.", HttpStatus.BAD_REQUEST);
                }
                if (image1 != null) {
                    pictures.add(image1);
                }
                if (image2 != null) {
                    pictures.add(image2);
                }
                if (image3 != null) {
                    pictures.add(image3);
                }
                if (image4 != null) {
                    pictures.add(image4);
                }
                if (image5 != null) {
                    pictures.add(image5);
                }
                if (pictures.size() > sallitutKuvat) {
                    return new ResponseEntity<>("There's space for only " + sallitutKuvat + " pictures. Choose the ones you want to add to your plan.", HttpStatus.BAD_REQUEST);
                } else {
                    for (MultipartFile kuva : pictures) {
                        ImageEntity a = imageService.createImage(kuva);
                        a.setReference(false);
                        currentPlan.get().getReadyPictures().add(a);
                        a.setPhotoshoot(plan);
                    }
                    photoshootPlanRepository.save(plan);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Hakee suunnitelman id:n perusteella. Tarkistetaan käyttäjän oikeus hakuun Principal userin avulla.
     */
    public ResponseEntity<?> findOneById(long id, Principal user){
        Optional<PhotoshootPlanEntity> currentPlan = photoshootPlanRepository.findById(id);
        if (!currentPlan.isPresent()){
            return new ResponseEntity<>("You have no plan with given id. Please check id and try again.", HttpStatus.NOT_FOUND);
        }
        if (currentPlan.get().getCreator().getUsername().equals(user.getName())){
            return  new ResponseEntity<>(currentPlan.get(), HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}