package learn.code.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectIdException(ProjectIdException projectIdException, WebRequest webRequest) {
        ProjectExceptionResponse projectExceptionResponse = new ProjectExceptionResponse(projectIdException.getMessage());
        return new ResponseEntity(projectExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException exception, WebRequest webRequest) {
        ProjectNotFoundExceptionResponse response = new ProjectNotFoundExceptionResponse(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception, WebRequest webRequest) {
        UsernameAlreadyExistsResponse response = new UsernameAlreadyExistsResponse(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}
