package likelion.dotoread.auth.oauth2.service;

import likelion.dotoread.auth.oauth2.CustomOAuth2User;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.web.dto.UserDto.GoogleResponse;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        GoogleResponse googleResponse = null;
        if (registrationId.equals("google")) {

            googleResponse = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }
        String username = googleResponse.getProvider()+" "+googleResponse.getProviderId();
        User existData = userRepository.findByUsername(username);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            user = User.builder()
                    .name(googleResponse.getName())
                    .username(username)
                    .email(googleResponse.getEmail())
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user);
        } else {
            user.setEmail(googleResponse.getEmail());
            user.setName(googleResponse.getName());
            userRepository.save(user);
        }

        return new CustomOAuth2User(user);
    }
}