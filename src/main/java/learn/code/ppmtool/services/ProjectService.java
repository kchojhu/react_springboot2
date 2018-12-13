package learn.code.ppmtool.services;

import learn.code.ppmtool.domain.Project;
import learn.code.ppmtool.exceptions.ProjectIdException;
import learn.code.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdate(Project project) {
        try {
            project.setProjectName(project.getProjectName().toUpperCase());
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
