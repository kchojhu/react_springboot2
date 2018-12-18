package learn.code.ppmtool.web;

import learn.code.ppmtool.domain.Project;
import learn.code.ppmtool.services.MapValidationErrorService;
import learn.code.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.getErrorMap(bindingResult);
        }

        return new ResponseEntity<>(projectService.saveOrUpdate(project), HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> findByProjectIdentifier(@PathVariable String projectIdentifier) {
        return new ResponseEntity<>(projectService.findByProjectIdentifier(projectIdentifier), HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> findAll() {
        return projectService.findAll();
    }

    @DeleteMapping("/{projectIdentifier}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectIdentifier) {
        projectService.deleteByProjectIdentifier(projectIdentifier);

        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

}
