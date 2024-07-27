package com.nexusforge.user.repository;

import com.nexusforge.common.Ticker;
import com.nexusforge.user.entity.PorfolioItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioItemRepository extends CrudRepository<PorfolioItem, Integer> {
   List<PorfolioItem> findAllByUserId(Integer userId);
   Optional<PorfolioItem> findByUserIdAndTicker(Integer userId, Ticker ticker);
}
