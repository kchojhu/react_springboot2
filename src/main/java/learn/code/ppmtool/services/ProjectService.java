package learn.code.ppmtool.services;

import learn.code.ppmtool.domain.Backlog;
import learn.code.ppmtool.domain.Project;
import learn.code.ppmtool.domain.User;
import learn.code.ppmtool.exceptions.ProjectIdException;
import learn.code.ppmtool.exceptions.ProjectNotFoundException;
import learn.code.ppmtool.repositories.BacklogRepository;
import learn.code.ppmtool.repositories.ProjectRepository;
import learn.code.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdate(Project project, String userName) {
        try {

            if (project.getId() != null) {
                Project existingProject = projectRepository.findById(project.getId()).get();
                if (existingProject != null && !existingProject.getProjectLeader().equals(userName)) {
                    throw new ProjectNotFoundException("Project not found in your account");
                } else if (existingProject == null) {
                    throw new ProjectNotFoundException("Project ID:" + project.getId() + " not found");
                }
            }

            User user = userRepository.findByUsername(userName);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());

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

    public Project findByProjectIdentifier(String projectIdentifier, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project Id '" + projectIdentifier + "' does not exists.");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not in your account");
        }

        return project;

    }

    public Iterable<Project> findAll(String name) {
        return projectRepository.findAllByProjectLeader(name);
    }

    public void deleteByProjectIdentifier(String projectIdentifier, String username) {
        Project project = findByProjectIdentifier(projectIdentifier, username);

        projectRepository.delete(project);
    }
}
