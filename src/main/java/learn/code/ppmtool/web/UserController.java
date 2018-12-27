package learn.code.ppmtool.web;

import learn.code.ppmtool.domain.User;
import learn.code.ppmtool.payload.JWTLoginSuccessResponse;
import learn.code.ppmtool.payload.LoginRequest;
import learn.code.ppmtool.security.JwtAuthenticationEntryPoint;
import learn.code.ppmtool.security.JwtTokenProvider;
import learn.code.ppmtool.security.SecurityConstants;
import learn.code.ppmtool.services.MapValidationErrorService;
import learn.code.ppmtool.services.UserService;
import learn.code.ppmtool.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.getErrorMap(bindingResult);
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return mapValidationErrorService.getErrorMap(bindingResult);
        }
        User newUser = userService.saveUser(user);
        newUser.setConfirmPassword("");
        return new ResponseEntity<>(newUser,HttpStatus.CREATED);
    }
}
