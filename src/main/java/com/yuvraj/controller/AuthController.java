package com.yuvraj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuvraj.config.JwtProvider;
import com.yuvraj.model.AuthenticationRequest;
import com.yuvraj.model.AuthenticationResponse;
import com.yuvraj.model.User;
import com.yuvraj.repository.UserRepository;
import com.yuvraj.service.EventService;
import com.yuvraj.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private UserService userService;
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired 
	private EventService eventService;	
	
	private AuthenticationResponse authenticationResponse;
	
	@Autowired
	private JwtProvider jwtProvider; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/dummy")
	public String dummy() {
		return "Why this code is fukced up ?";
	}
	
//	@PostMapping("/register")
//	public ResponseEntity<?> registerUser(@RequestBody User user) {
//		User newUser = userService.registerUser(user);
//		if (newUser!=null) {
//			System.out.println("Hello succesfull");
//			return ResponseEntity.status(HttpStatus.CREATED).body(user);
//		}
//		else {
//			System.out.println("Hello fukced up");
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not possible");
//		}
//	}
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody User user) {
		
		String username = user.getUsername();
		String password = user.getPassword();
		String email = user.getEmail();
		String role = user.getRole();
		
		User isUsernameExist = userRepository.findByUsername(username);
		
		if (isUsernameExist!=null)
			throw new UsernameNotFoundException("Username is already used with another account");
		
		User createdUser = new User();
		createdUser.setUsername(username);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setEmail(email);
		createdUser.setRole(role);
		
		User savedUser = userRepository.save(createdUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthenticationResponse authResponse = new AuthenticationResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signup successfull");
		
		return new ResponseEntity<AuthenticationResponse>(authResponse, HttpStatus.CREATED);
		
	}
	
//	@PostMapping("/login")
//	public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//		boolean isAuthenticated = userService.authenticateUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//			if (isAuthenticated) {
//				return ResponseEntity.status(HttpStatus.OK).body("Login successful") ;
//			}
//			else {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Username or Password");
//			}
//		}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authenticationRequest) {
		
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		
		Authentication authentication = authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthenticationResponse authResponse = new AuthenticationResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login successfull");
//		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		
//		String token = jwtProvider.generateToken(authentication);
//		
//		AuthenticationResponse authResponse = new AuthenticationResponse();
//		authResponse.setJwt(token);
//		authResponse.setMessage("Login successfull");
		
		return new ResponseEntity<AuthenticationResponse>(authResponse, HttpStatus.OK);
	}
	
	private Authentication authenticate(String username, String password) {
		User user = userService.getUserByUsername(username);
		
		if (user==null) {
			throw new BadCredentialsException("Invalid Username");
		}
		
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Invalid Password...");
		}
		
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}
	
	@GetMapping("/user/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
		User user = userService.getUserByUsername(username);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtProvider.invalidateToken(token); // Implement this method to invalidate the token if necessary
        }
        return ResponseEntity.ok().build();
    }
	
}