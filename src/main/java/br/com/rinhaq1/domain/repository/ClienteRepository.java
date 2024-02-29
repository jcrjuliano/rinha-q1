package br.com.rinhaq1.domain.repository;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.exception.NotFoundException;
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

    public void save(ClienteEntity cliente) {
        String sql = "UPDATE tb_clientes SET name=?, limite=?, saldo=?, transactions=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNome());
            statement.setLong(2, cliente.getLimite());
            statement.setLong(3, cliente.getSaldo());
            statement.setArray(4, connection.createArrayOf("text", cliente.getTransactions().toArray()));
            statement.setLong(5, cliente.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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