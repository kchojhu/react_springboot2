package learn.code.ppmtool.services;

import learn.code.ppmtool.domain.Backlog;
import learn.code.ppmtool.domain.Project;
import learn.code.ppmtool.domain.ProjectTask;
import learn.code.ppmtool.exceptions.ProjectNotFoundException;
import learn.code.ppmtool.repositories.BacklogRepository;
import learn.code.ppmtool.repositories.ProjectRepository;
import learn.code.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        Backlog backlog = projectService.findByProjectIdentifier(projectIdentifier, username).getBacklog();

        if (backlog == null) {
            throw new ProjectNotFoundException("Project ID:" + projectIdentifier + " not found");
        }

        projectTask.setBacklog(backlog);
        Integer ptSequence = backlog.getPTSequence();
        ptSequence++;

        backlog.setPTSequence(ptSequence);
        projectTask.setProjectSequence(projectIdentifier + "-" + ptSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);

    }

    public List<ProjectTask> findBacklogByProjectIdentifier(String backlogId, String username) {
        projectService.findByProjectIdentifier(backlogId, username);

        Project project = projectRepository.findByProjectIdentifier(backlogId);

        if (project == null) {
            throw new ProjectNotFoundException("Project ID:" + backlogId + " not found");
        }

        return projectTaskRepository.findBacklogByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask findByProjectSequence(String backlogId, String projectSequence, String username) {
        projectService.findByProjectIdentifier(backlogId, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectSequence);

        if (projectTask == null) {
            throw new ProjectNotFoundException("Project TASK:" + projectSequence + " not found");
        }

        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            throw new ProjectNotFoundException("Project TASK:" + projectSequence + " not found in Project ID:" + backlogId);
        }

        return projectTask;
    }

    public ProjectTask updateProjectTask(ProjectTask updatedProjectTask, String backlogId, String projectSequence, String username) {
        findByProjectSequence(backlogId, projectSequence, username);

        return projectTaskRepository.save(updatedProjectTask);
    }

    public void deleteProjectTask(String backlogId, String projectSequence, String username) {
        ProjectTask projectTask = findByProjectSequence(backlogId, projectSequence, username);

        projectTaskRepository.delete(projectTask);
    }
}
