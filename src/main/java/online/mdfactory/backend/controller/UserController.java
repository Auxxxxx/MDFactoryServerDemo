package online.mdfactory.backend.controller;

import online.mdfactory.backend.model.User;
import online.mdfactory.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test_response1")
    public ResponseEntity<Boolean> testResponse1() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/test_response2")
    public ResponseEntity<Boolean> testResponse2() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAll")
    public List<User> list() {
        return userService.listAll();
    }

    @PostMapping("/add")
    public String add(@RequestBody User user) {
        userService.save(user);
        return "new_user_added";
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Integer id) {
        try {
            User user = userService.get(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable Integer id) {
        try {
            user.setId(id);
            userService.save(user);
            return new ResponseEntity<User>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        userService.delete(id);
        return "Deleted user by id: " + id;
    }
}
