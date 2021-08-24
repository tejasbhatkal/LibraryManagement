package com.mcbirdtechnologies.LibManagement.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mcbirdtechnologies.LibManagement.Constants.LibManagementConstants;
import com.mcbirdtechnologies.LibManagement.UserDetail.MyUserDetailsService;
import com.mcbirdtechnologies.LibManagement.Utils.JwtUtil;

@RestController
public class AuthenticateController {

	@Autowired
	MyUserDetailsService userDetail;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, Object> authenticationRequest,
			HttpServletResponse response) throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.get(LibManagementConstants.USERNAME), authenticationRequest.get(LibManagementConstants.PASSWORD)));
		} catch (BadCredentialsException e) {
			return new ResponseEntity<String>("Bad Credentials", HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
		}

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername((String) authenticationRequest.get(LibManagementConstants.USERNAME));

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put(LibManagementConstants.TOKEN, jwt);

		response.setHeader(HttpHeaders.SET_COOKIE, jwt);

		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);

	}

}
