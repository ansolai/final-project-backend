package fi.academy.springauth.content;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ContentRepository extends CrudRepository<ContentEntity, Long> {

    Iterable<ContentEntity> findAll(Pageable pageable);

}
