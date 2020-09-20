package com.breakoutms.lfs.server.user;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.user.model.LoginDto;
import com.breakoutms.lfs.server.user.model.LoginResponseDto;
import com.breakoutms.lfs.server.user.model.User;
import com.breakoutms.lfs.server.user.model.UserDTO;
import com.breakoutms.lfs.server.user.model.UserViewModel;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController implements ViewModelController<User, UserViewModel>{

    private UserService service;
    private final PagedResourcesAssembler<UserViewModel> pagedAssembler;

	@GetMapping 
	public ResponseEntity<PagedModel<EntityModel<UserViewModel>>> all(
			@SearchSpec Specification<User> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}
    
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto) {
       return service.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @PostMapping
    public ResponseEntity<UserViewModel> register(@RequestBody @Valid UserDTO userDto){
    	var entity = UserMapper.INSTANCE.map(userDto);
		return new ResponseEntity<>(
				toViewModel(service.register(entity)), 
				HttpStatus.CREATED
		);
    }

	@Override
	public UserViewModel toViewModel(User entity) {
		return UserMapper.INSTANCE.map(entity);
	}
}