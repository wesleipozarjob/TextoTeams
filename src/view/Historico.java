package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import controller.DatabaseHelper;

public class Historico extends JFrame {
	 private static final long serialVersionUID = 1L;

    private JTable table;
    private JTextField textFieldPesquisar;

    public Historico() {
        setTitle("Histórico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 664, 434);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblHistorico = new JLabel("HISTÓRICO");
        lblHistorico.setBounds(262, 5, 103, 21);
        lblHistorico.setFont(new Font("Times New Roman", Font.BOLD, 18));
        contentPane.add(lblHistorico);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 90, 628, 283);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        table.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"ID", "N\u00B0 CHAMADO", "CATEGORIA", "REQUERENTE", "STATUS"
        	}
        ));
        table.getColumnModel().getColumn(0).setPreferredWidth(36);
        table.getColumnModel().getColumn(3).setPreferredWidth(86);
        table.getColumnModel().getColumn(4).setPreferredWidth(141);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(10, 6, 89, 23);
        contentPane.add(btnVoltar);

        textFieldPesquisar = new JTextField();
        textFieldPesquisar.setBounds(189, 62, 366, 20);
        contentPane.add(textFieldPesquisar);
        textFieldPesquisar.setColumns(10);

        JButton btnPesquisar = new JButton("PESQUISAR");
        btnPesquisar.setBounds(42, 61, 137, 23);
        contentPane.add(btnPesquisar);

        // Carregar dados na tabela inicialmente sem filtro
        carregarDados("");

        // Adicionar listener ao botão "PESQUISAR"
        btnPesquisar.addActionListener(e -> {
            String textoPesquisa = textFieldPesquisar.getText();
            carregarDados(textoPesquisa);  // Atualizar os dados com base na pesquisa
        });

        btnVoltar.addActionListener(e -> {
            dispose();
            TelaPrincipal telaPrincipal = new TelaPrincipal();
            telaPrincipal.setLocationRelativeTo(null);
            telaPrincipal.setVisible(true);
        });
    }

    // Modifiquei o método carregarDados para aceitar um parâmetro de pesquisa
    private void carregarDados(String pesquisa) {
        String sql = "SELECT * FROM Historico WHERE numero_chamado LIKE ? OR categoria LIKE ? OR requerente LIKE ? OR status LIKE ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String query = "%" + pesquisa + "%";  // Adicionar % para permitir pesquisa parcial
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);
            pstmt.setString(4, query);

            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Limpar dados existentes

            while (rs.next()) {
                int id = rs.getInt("id");
                String numeroChamado = rs.getString("numero_chamado");
                String categoria = rs.getString("categoria");
                String requerente = rs.getString("requerente");
                String status = rs.getString("status");

                model.addRow(new Object[]{id, numeroChamado, categoria, requerente, status});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Historico frame = new Historico();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
