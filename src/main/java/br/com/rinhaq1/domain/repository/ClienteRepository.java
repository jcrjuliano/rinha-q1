package br.com.rinhaq1.domain.repository;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class ClienteRepository {
    private final DataSource dataSource;

    public ClienteRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TransactionDTO searchAndUpdateClient(TransactionParams params, String transactionData, long clientId) {
        long newValue = params.tipo().equalsIgnoreCase("c") ? Long.parseLong(params.valor()) : -Long.parseLong(params.valor());
        String updateQuery =  "UPDATE tb_clientes " +
                "SET saldo = saldo + ?, " +
                "transactions = CASE " +
                "WHEN array_length(transactions, 1) >= 10 " +
                "THEN array_prepend(?, transactions[1:9]) " +
                "ELSE " +
                "array_prepend(?, transactions) " +
                "END " +
                "WHERE id = ? " +
                "RETURNING limite, saldo;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setLong(1, newValue);
            statement.setString(2, transactionData);
            statement.setString(3, transactionData);
            statement.setLong(4, clientId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new TransactionDTO(resultSet.getLong("limite"), resultSet.getLong("saldo"));
                }
            }

            } catch (SQLException ex) {
            throw new UnprocessableEntity(ex.getMessage());
        }
        throw new NotFoundException("Cliente não encontrado");
    }


    public ClienteEntity findById(Long id) {
        String sql = "SELECT id, name, limite, saldo, transactions FROM tb_clientes WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToClienteEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new NotFoundException("Cliente não encontrado");
    }

    private ClienteEntity mapResultSetToClienteEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String nome = resultSet.getString("name");
        Long limite = resultSet.getLong("limite");
        Long saldo = resultSet.getLong("saldo");

        Array transactionsArray = resultSet.getArray("transactions");
        List<String> transactions = transactionsArray != null ?
                Arrays.asList((String[]) transactionsArray.getArray()) : Collections.emptyList();

        return new ClienteEntity(id, nome, limite, saldo, transactions);
    }

}