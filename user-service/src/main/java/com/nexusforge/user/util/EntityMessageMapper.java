package com.nexusforge.user.util;

import com.nexusforge.user.Holding;
import com.nexusforge.user.UserInformation;
import com.nexusforge.user.entity.PorfolioItem;
import com.nexusforge.user.entity.User;

import java.util.List;

public class EntityMessageMapper {

    public static UserInformation toUserInformation(User user, List<PorfolioItem> items){
       var holdings = items.stream()
                .map(i -> Holding.newBuilder()
                        .setTicker(i.getTicker()).setQuantity(i.getQuantity()).build())
                .toList();
       return UserInformation.newBuilder()
               .setUserId(user.getId())
               .setName(user.getName())
               .setBalance(user.getBalance())
               .addAllHoldings(holdings)
               .build();
    }

}
