package io.github.stealingdapenta.idletd.idleplayer.stats;

import io.github.stealingdapenta.idletd.idleplayer.IdlePlayer;
import io.github.stealingdapenta.idletd.idleplayer.IdlePlayerService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceHandler {

    private static final String MONEY_SYMBOL = "₰";

    private final IdlePlayerService idlePlayerService;

    public boolean pay(IdlePlayer source, IdlePlayer target, double amount) {
        double sourceFunds = source.getBalance();

        if (isLessOrEqualThan(sourceFunds, amount)) {
            // can't afford to pay
            return false;
        }

        source.setBalance(sourceFunds - amount);
        target.setBalance(target.getBalance() + amount);

        return true;
    }

    public boolean pay(IdlePlayer target, double amount) {
        target.setBalance(target.getBalance() + amount);
        return true;
    }

    private boolean isLessOrEqualThan(double input, double validation) {
        return input <= validation;
    }
}
