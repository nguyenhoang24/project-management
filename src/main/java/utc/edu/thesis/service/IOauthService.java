package utc.edu.thesis.service;

import utc.edu.thesis.domain.dto.TokenDto;


public interface IOauthService {
    TokenDto getTokenFacebook(TokenDto tokenDto);

}
