package learn.code.ppmtool.web;

import learn.code.ppmtool.domain.ProjectTask;
import learn.code.ppmtool.services.MapValidationErrorService;
import learn.code.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> createProjectTasksToBacklog(@PathVariable String backlogId, @Valid @RequestBody ProjectTask projectTask,
                                                         BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.getErrorMap(bindingResult);
        }

        return new ResponseEntity<>(projectTaskService.addProjectTask(backlogId, projectTask, principal.getName()),
                HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlogId, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findBacklogByProjectIdentifier(backlogId, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{backlogId}/{taskId}")
    public ResponseEntity<ProjectTask> findByProjectSequence(@PathVariable String backlogId, @PathVariable String taskId, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findByProjectSequence(backlogId, taskId, principal.getName()), HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{taskId}")
    public ResponseEntity<?> updateProjectTask(@PathVariable String backlogId, @PathVariable String taskId,
                                               @Valid @RequestBody ProjectTask projectTask, BindingResult bindingResult,
                                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.getErrorMap(bindingResult);
        }

        return new ResponseEntity<>(projectTaskService.updateProjectTask(projectTask, backlogId, taskId, principal.getName()),
                HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{taskId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String taskId, Principal principal) {
        projectTaskService.deleteProjectTask(backlogId, taskId, principal.getName());

        return new ResponseEntity<>("Project Task:" + taskId + " deleted", HttpStatus.OK);
    }
}
