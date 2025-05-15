package com.github.igorcossta.infra.mapper;

import com.github.igorcossta.domain.*;
import com.github.igorcossta.infra.database.AccountEntity;

public final class Mapper {
    private Mapper() {
    }

    public static AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.getAccountId(),
                account.getIdentifier(),
                account.balance(),
                account.getOwnerUsername(),
                account.receivesTransactions(),
                account.receivesNotifications(),
                account.showsBalanceOnJoin()
        );
    }

    public static Account toDomain(AccountEntity entity) {
        return new Account(
                new AccountId(entity.getAccountId()),
                new Identifier(entity.getIdentifier()),
                new Amount(entity.getAmount()),
                new Username(entity.getUsername()),
                entity.isReceiveTransactions(),
                entity.isReceiveNotifications(),
                entity.isShowBalanceOnJoin()
        );
    }
}
