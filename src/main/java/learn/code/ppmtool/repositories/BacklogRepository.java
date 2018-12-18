package learn.code.ppmtool.repositories;

import learn.code.ppmtool.domain.Backlog;
import learn.code.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface BacklogRepository extends CrudRepository<Backlog, Long> {
    Backlog findByProjectIdentifier(String projectIdentifier);
}
