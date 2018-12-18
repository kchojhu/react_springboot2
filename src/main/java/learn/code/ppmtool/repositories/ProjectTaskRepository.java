package learn.code.ppmtool.repositories;

import learn.code.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
    List<ProjectTask> findBacklogByProjectIdentifierOrderByPriority(String backlogId);

    ProjectTask findByProjectSequence(String projectSequence);
}
