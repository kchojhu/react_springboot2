package learn.code.ppmtool.services;

import learn.code.ppmtool.domain.User;
import learn.code.ppmtool.exceptions.UsernameAlreadyExistsException;
import learn.code.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        try {
            if (userRepository.findByUsername(user.getUsername()) != null) {
                throw new UsernameAlreadyExistsException("Username already exists");
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
    }

}
