package utc.edu.thesis.service;

import utc.edu.thesis.domain.entity.Token;
import utc.edu.thesis.domain.entity.User;

public interface TokenService {

    Token createToken(User user, String token, String refreshToken);

//    TokenDto findByToken(String jwt);

//    TokenDto updateToken(TokenDto tokenDto);
}
