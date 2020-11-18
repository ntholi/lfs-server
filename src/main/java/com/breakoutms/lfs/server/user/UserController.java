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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.user.model.LoginDTO;
import com.breakoutms.lfs.server.user.model.LoginResponse;
import com.breakoutms.lfs.server.user.model.User;
import com.breakoutms.lfs.server.user.model.UserDTO;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController implements ViewModelController<User, UserDTO>{

    private UserService service;
    private final PagedResourcesAssembler<UserDTO> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Product", id));
	}
	
	@GetMapping 
	public ResponseEntity<PagedModel<EntityModel<UserDTO>>> all(
			@SearchSpec Specification<User> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginDTO loginDto) {
       return service.login(loginDto.getUsername(), 
    		   loginDto.getPassword(), 
    		   loginDto.getBranchId());
    }

    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDto){
    	var entity = UserMapper.INSTANCE.map(userDto);
		return new ResponseEntity<>(
				toViewModel(service.register(entity)), 
				HttpStatus.CREATED
		);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody @Valid UserDTO userDto){
    	var entity = UserMapper.INSTANCE.map(userDto);
    	return new ResponseEntity<>(
    			toViewModel(service.update(id, entity)), 
    			HttpStatus.CREATED);
    }

	@Override
	public UserDTO toViewModel(User entity) {
		return UserMapper.INSTANCE.map(entity);
	}
}