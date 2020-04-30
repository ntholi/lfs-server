package com.breakoutms.lfs.server.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.user.model.LoginDto;
import com.breakoutms.lfs.server.user.model.LoginResponseDto;
import com.breakoutms.lfs.server.user.model.User;
import com.breakoutms.lfs.server.user.model.UserDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto) {
       return userService.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid UserDTO userDto){
    	User user = UserMapper.INSTANCE.map(userDto);
        return userService.register(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

}