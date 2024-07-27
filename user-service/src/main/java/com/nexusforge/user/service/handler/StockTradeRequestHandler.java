package com.nexusforge.user.service.handler;

import com.nexusforge.common.Ticker;
import com.nexusforge.user.StockTradeRequest;
import com.nexusforge.user.StockTradeResponse;
import com.nexusforge.user.exception.InsufficientBalanceException;
import com.nexusforge.user.exception.InsufficientSharesException;
import com.nexusforge.user.exception.UnknownTickerException;
import com.nexusforge.user.exception.UnknownUserException;
import com.nexusforge.user.repository.PortfolioItemRepository;
import com.nexusforge.user.repository.UserRepository;
import com.nexusforge.user.util.EntityMessageMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StockTradeRequestHandler {
    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public StockTradeRequestHandler(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Transactional
    public StockTradeResponse buyStock(StockTradeRequest request) {
        //validate (ticker is valid, user is valid, balance is valid
        this.validateTicker(request.getTicker());
        var user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));

        var totalPrice = request.getQuantity() * request.getPrice();
        this.validateUserBalance(user.getId(), user.getBalance(), totalPrice);

        //Working on request
        user.setBalance(user.getBalance() - totalPrice);
        this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .ifPresentOrElse(
                        //present
                        item -> item.setQuantity(item.getQuantity() + request.getQuantity()),
                        () -> this.portfolioItemRepository.save(EntityMessageMapper.toPortfolioItem(request))
                );

        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());

    }

    @Transactional
    public StockTradeResponse sellStock(StockTradeRequest request) {
        //validate (ticker is valid, user is valid, balance is valid
        this.validateTicker(request.getTicker());
        var user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));
        //user has the stock to sell
        var portfolio = this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .filter(pi -> pi.getQuantity() >= request.getQuantity())
                .orElseThrow(() -> new InsufficientSharesException(user.getId()));

        var totalPrice = request.getQuantity() * request.getPrice();


        //Working on request
        user.setBalance(user.getBalance() + totalPrice);
        portfolio.setQuantity(portfolio.getQuantity() - request.getQuantity());
        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());
    }

    private void validateTicker(Ticker ticker) {
        if (Ticker.UNKNOWN.equals(ticker)) {
            throw new UnknownTickerException();
        }
    }

    private void validateUserBalance(Integer userId, Integer userBalance, Integer totalPrice) {
        if (totalPrice > userBalance) {
            throw new InsufficientBalanceException(userId);
        }
    }
}
