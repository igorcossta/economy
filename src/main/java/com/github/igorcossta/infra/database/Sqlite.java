package com.github.igorcossta.infra.database;

import com.github.igorcossta.domain.TransactionLog;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class Sqlite {
    private final Connection connection;

    public Sqlite(File dataFolder) {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            File dbFile = new File(dataFolder, "economy.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            connection = DriverManager.getConnection(url);

            System.out.println("[SQLite] Connected to " + dbFile.getName());
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[SQLite] Connection closed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() {
        final String CREATE_ACCOUNT_TABLE = """
                 CREATE TABLE IF NOT EXISTS Account (
                     account_id CHAR(36) PRIMARY KEY,
                     player_uuid CHAR(36) NOT NULL UNIQUE,
                     amount DECIMAL(19, 4) NOT NULL,
                     username CHAR(255) NOT NULL,
                     receive_transactions BOOLEAN NOT NULL DEFAULT 1,
                     receive_notifications BOOLEAN NOT NULL DEFAULT 1,
                     show_balance_on_join BOOLEAN NOT NULL DEFAULT 1
                 );
                """;
        final String CREATE_TRANSACTION_LOG_TABLE = """
                CREATE TABLE IF NOT EXISTS Transaction_log (
                    id CHAR(36) PRIMARY KEY,
                    action CHAR(50),
                    sender CHAR(255),
                    receiver CHAR(255),
                    amount DECIMAL(19, 4),
                    previous_receiver_balance DECIMAL(19, 4),
                    new_receiver_balance DECIMAL(19, 4),
                    previous_sender_balance DECIMAL(19, 4),
                    new_sender_balance DECIMAL(19, 4),
                    timestamp TEXT
                );
                """;
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(CREATE_ACCOUNT_TABLE);
            stmt.executeUpdate(CREATE_TRANSACTION_LOG_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void save(TransactionLog transaction) {
        String SAVE_TRANSACTION_LOG = """
                    INSERT INTO transaction_log (
                        id,
                        action,
                        sender,
                        receiver,
                        amount,
                        previous_receiver_balance,
                        new_receiver_balance,
                        previous_sender_balance,
                        new_sender_balance,
                        timestamp
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var stmt = getConnection().prepareStatement(SAVE_TRANSACTION_LOG)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, transaction.transactionType().toString());
            stmt.setString(3, transaction.sender());
            stmt.setString(4, transaction.receiver());
            stmt.setBigDecimal(5, transaction.amount());
            stmt.setBigDecimal(6, transaction.previousReceiverBalance());
            stmt.setBigDecimal(7, transaction.newReceiverBalance());
            stmt.setBigDecimal(8, transaction.previousSenderBalance());
            stmt.setBigDecimal(9, transaction.newSenderBalance());
            stmt.setString(10, transaction.timestamp().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void save(AccountEntity account) {
        String insertAccount = """
                INSERT INTO Account (
                    account_id,
                    player_uuid,
                    amount,
                    username,
                    receive_transactions,
                    receive_notifications,
                    show_balance_on_join
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(player_uuid)
                DO UPDATE SET
                    amount = excluded.amount,
                    username = excluded.username,
                    receive_transactions = excluded.receive_transactions,
                    receive_notifications = excluded.receive_notifications,
                    show_balance_on_join = excluded.show_balance_on_join;
                """;

        try (var st = getConnection().prepareStatement(insertAccount)) {
            st.setString(1, account.getAccountId().toString());
            st.setString(2, account.getIdentifier().toString());
            st.setBigDecimal(3, account.getAmount());
            st.setString(4, account.getUsername());
            st.setBoolean(5, account.isReceiveTransactions());
            st.setBoolean(6, account.isReceiveNotifications());
            st.setBoolean(7, account.isShowBalanceOnJoin());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AccountEntity> findByUUID(UUID uuid) {
        final String query = """
                SELECT
                    account_id,
                    player_uuid,
                    amount,
                    username,
                    receive_transactions,
                    receive_notifications,
                    show_balance_on_join
                FROM Account
                WHERE player_uuid = ?
                """;
        try (var st = getConnection().prepareStatement(query)) {
            st.setString(1, uuid.toString());

            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    UUID accountId = UUID.fromString(rs.getString("account_id"));
                    UUID identifier = UUID.fromString(rs.getString("player_uuid"));
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String username = rs.getString("username");

                    boolean receiveTransactions = rs.getBoolean("receive_transactions");
                    boolean receiveNotifications = rs.getBoolean("receive_notifications");
                    boolean showBalanceOnJoin = rs.getBoolean("show_balance_on_join");

                    AccountEntity entity = new AccountEntity(
                            accountId,
                            identifier,
                            amount,
                            username,
                            receiveTransactions,
                            receiveNotifications,
                            showBalanceOnJoin
                    );

                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch account: " + e.getMessage(), e);
        }

        return Optional.empty();
    }
}
