package com.nexusforge.user.service.handler;

import com.nexusforge.user.UserInformation;
import com.nexusforge.user.UserInformationRequest;
import com.nexusforge.user.exception.UnknownUserException;
import com.nexusforge.user.repository.PortfolioItemRepository;
import com.nexusforge.user.repository.UserRepository;
import com.nexusforge.user.util.EntityMessageMapper;
import org.springframework.stereotype.Service;

@Service
public class UserInformationRequestHandler {
    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public UserInformationRequestHandler(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }


    public UserInformation getUserInformation(UserInformationRequest request) {
        // reactive, zip, merge

        var user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));
        var portfolioItems = this.portfolioItemRepository.findAllByUserId(request.getUserId());
        return EntityMessageMapper.toUserInformation(user, portfolioItems);

    }
}
