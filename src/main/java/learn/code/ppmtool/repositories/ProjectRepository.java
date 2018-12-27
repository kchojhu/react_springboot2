package learn.code.ppmtool.repositories;

import learn.code.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectIdentifier(String projectIdentifier);

    Iterable<Project> findAllByProjectLeader(String projectLeader);
}
