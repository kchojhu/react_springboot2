package learn.code.ppmtool.services;

import learn.code.ppmtool.domain.Backlog;
import learn.code.ppmtool.domain.Project;
import learn.code.ppmtool.exceptions.ProjectIdException;
import learn.code.ppmtool.repositories.BacklogRepository;
import learn.code.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdate(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier());
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
            }



            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project Id '" + project.getProjectIdentifier().toUpperCase() + "' already exists.");
        }

    }

    public Project findByProjectIdentifier(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project Id '" + projectIdentifier + "' does not exists.");
        }

        return project;

    }

    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }

    public void deleteByProjectIdentifier(String projectIdentifier) {
        Project project = findByProjectIdentifier(projectIdentifier);

        projectRepository.delete(project);
    }
}
