package fi.academy.springauth.images;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<ImageEntity, Long> {
    ImageEntity findByUrl(String url);
    ImageEntity findById(long id);
}
