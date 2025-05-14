package com.github.igorcossta.domain;

class AccountSettings {
    private boolean receiveTransactions = true;
    private boolean receiveNotifications = false;
    private boolean showBalanceOnJoin = true;

    public AccountSettings(boolean receiveTransactions,
                           boolean receiveNotifications,
                           boolean showBalanceOnJoin) {
        this.receiveTransactions = receiveTransactions;
        this.receiveNotifications = receiveNotifications;
        this.showBalanceOnJoin = showBalanceOnJoin;
    }

    public AccountSettings() {
    }

    public boolean isReceiveTransactions() {
        return receiveTransactions;
    }

    public boolean isReceiveNotifications() {
        return receiveNotifications;
    }

    public boolean isShowBalanceOnJoin() {
        return showBalanceOnJoin;
    }
}
