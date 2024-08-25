package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:historico.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // Cria a tabela Historico se não existir
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Historico (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "numero_chamado TEXT NOT NULL," +
                    "categoria TEXT NOT NULL," +
                    "requerente TEXT NOT NULL," +
                    "status TEXT NOT NULL" +
                    ");";
            stmt.execute(createTableSQL);
            System.out.println("Tabela criada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
