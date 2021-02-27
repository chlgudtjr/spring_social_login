package edu.bit.ex.controller;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bit.ex.service.KakaoServiceImpt;
import edu.bit.ex.vo.KakaoAuth;
import edu.bit.ex.vo.KakaoProfile;
import edu.bit.ex.service.KakaoServiceImpt;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
@Controller
public class KakaoController {
	
	private KakaoServiceImpt kakaoService;

	@GetMapping("/social/sociallogin")
	public String home(Model model) {
		log.info("social..");
		model.addAttribute("kakaoUrl", kakaoService.getAuthorizationUrl());
		
		return "sociallogin";
	}

	@GetMapping("/kakaologin")	
	public String kakaoCallback(String code,Model model) {
		log.info("kakaoCallback ..");
		
		KakaoAuth kakaoAuth = kakaoService.getKakaoTokenInfo(code);
		KakaoProfile profile= kakaoService.getKakaoProfile(kakaoAuth.getAccess_token());
		model.addAttribute("user", profile);
		
				
		//user.kakao_account.profile.nickname
		return "social_home";
	}

}
