package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder bCryptPasswordEncoder;
    private UsersRepository usersRepository;
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

//    public ResponseEntity<?> userAuthentication(@RequestBody Users authRequest) {
//        //авторизация пользователя
//
//
//
//        // Проверка наличия пользователя в базе данных
////        System.out.println("userDetails: " + userDetails.getPassword()+"---------------");
//        UserDetails userDetails;
//        System.out.println(authRequest.getPassword());
//        try {
//            Authentication au = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(au);
//
////            // Получение данных пользователя
////            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////           UserDetails userDetails2 = (UserDetails) authentication.getPrincipal();
////            System.out.println(userDetails2);
//             userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
//            System.out.println(userDetails.getPassword()+"User Details");
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>(new BadCredentialsException("Bad credentials"), HttpStatus.UNAUTHORIZED);
//        }
//
////        String token = jwtTokenUtils.generateToken(userDetails);
//        return ResponseEntity.ok(userDetails);
//    }


//    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
//        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
//        }
//        if (userService.findByUsername(registrationUserDto.getEmail()).isPresent()) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
//        }
//        User user = userService.createNewUser(registrationUserDto);
//        return ResponseEntity.ok(new UserDto(user.getId(), user.getEmail(), user.getEmail()));
//    }
}