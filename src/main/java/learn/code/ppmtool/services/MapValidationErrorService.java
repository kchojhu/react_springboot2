package learn.code.ppmtool.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapValidationErrorService {

    public ResponseEntity<Map<String, String>> getErrorMap(BindingResult bindingResult) {
        Map<String, String> errorMaps = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMaps.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        return new ResponseEntity<>(errorMaps, HttpStatus.BAD_REQUEST);
    }
}
