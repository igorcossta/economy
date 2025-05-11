package com.github.igorcossta.infra.database;

import com.github.igorcossta.domain.Account;
import com.github.igorcossta.domain.Amount;
import com.github.igorcossta.domain.Identifier;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

// TODO: create repository entity instead of using domain entity
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
                     id CHAR(36) PRIMARY KEY,
                     player_uuid CHAR(36) NOT NULL UNIQUE,
                     amount DECIMAL(19, 4) NOT NULL
                 );
                """;
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(CREATE_ACCOUNT_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void save(Account account) {
        String insertAccount = """
                INSERT INTO Account (id, player_uuid, amount)
                VALUES (?, ?, ?)
                ON CONFLICT(player_uuid)
                DO UPDATE SET amount = excluded.amount;
                """;
        try (var st = getConnection().prepareStatement(insertAccount)) {
            st.setString(1, UUID.randomUUID().toString());
            st.setString(2, account.getIdentifier().toString());
            st.setBigDecimal(3, account.balance());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Account> findByUUID(UUID uuid) {
        final String findUUID = """
                    SELECT player_uuid, amount FROM Account WHERE player_uuid = ?
                """;
        try (var st = getConnection().prepareStatement(findUUID)) {
            st.setString(1, uuid.toString());

            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    Identifier id = new Identifier(UUID.fromString(rs.getString("player_uuid")));
                    Amount amount = new Amount(rs.getBigDecimal("amount"));
                    Account account = new Account(id, amount);
                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch account: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

}
