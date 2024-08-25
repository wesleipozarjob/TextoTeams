package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.DatabaseHelper;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldChamado;
    private JTextField textFieldCategoria;
    private JTextField textFieldRequerente;
    private JComboBox<String> comboBoxStatus;

    public TelaPrincipal() {
        setTitle("Tela Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 628, 433);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setForeground(Color.WHITE);
        panel.setBackground(new Color(128, 128, 255));
        panel.setBounds(10, 11, 602, 372);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("GERADOR DE TEXTO TEAMS");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        lblNewLabel.setBounds(195, 11, 223, 18);
        panel.add(lblNewLabel);

        JLabel lblChamado = new JLabel("N° CHAMADO");
        lblChamado.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblChamado.setForeground(Color.WHITE);
        lblChamado.setBounds(132, 77, 87, 14);
        panel.add(lblChamado);

        textFieldChamado = new JTextField();
        textFieldChamado.setBounds(229, 75, 86, 20);
        panel.add(textFieldChamado);
        textFieldChamado.setColumns(10);

        JLabel lblCategoria = new JLabel("CATEGORIA");
        lblCategoria.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblCategoria.setForeground(Color.WHITE);
        lblCategoria.setBounds(132, 109, 87, 14);
        panel.add(lblCategoria);

        textFieldCategoria = new JTextField();
        textFieldCategoria.setBounds(229, 109, 189, 20);
        panel.add(textFieldCategoria);
        textFieldCategoria.setColumns(10);

        JLabel lblRequerente = new JLabel("REQUERENTE");
        lblRequerente.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblRequerente.setForeground(Color.WHITE);
        lblRequerente.setBounds(132, 148, 87, 14);
        panel.add(lblRequerente);

        textFieldRequerente = new JTextField();
        textFieldRequerente.setBounds(229, 140, 189, 20);
        panel.add(textFieldRequerente);
        textFieldRequerente.setColumns(10);

        JLabel lblStatus = new JLabel("STATUS");
        lblStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBounds(132, 183, 87, 14);
        panel.add(lblStatus);

        comboBoxStatus = new JComboBox<>();
        comboBoxStatus.setModel(new DefaultComboBoxModel<>(new String[]{
            "Solucionado",
            "Escalonado - N2 Corporativo",
            "Escalonado - N2 Conteúdo",
            "Escalonado - Triagem de Sistemas",
            "Escalonado - Dados",
            "Cancelado/Fechado"
        }));
        comboBoxStatus.setBounds(229, 171, 189, 22);
        panel.add(comboBoxStatus);

        JButton btnLimpar = new JButton("LIMPAR");
        btnLimpar.setBackground(Color.WHITE);
        btnLimpar.setForeground(Color.BLACK);
        btnLimpar.setBounds(132, 246, 136, 32);
        panel.add(btnLimpar);
        btnLimpar.addActionListener(e -> {
            textFieldChamado.setText("");
            textFieldCategoria.setText("");
            textFieldRequerente.setText("");
            comboBoxStatus.setSelectedIndex(0);
        });

        JButton btnGerar = new JButton("GERAR");
        btnGerar.setForeground(Color.BLACK);
        btnGerar.setBackground(Color.WHITE);
        btnGerar.setBounds(306, 246, 136, 32);
        panel.add(btnGerar);

        JButton btnHistorico = new JButton("Histórico");
        btnHistorico.setBounds(10, 338, 89, 23);
        panel.add(btnHistorico);

        // Adicionando ação de abrir a tela de histórico
        btnHistorico.addActionListener(e -> {
            dispose();
            Historico historico = new Historico();
            historico.setLocationRelativeTo(null);
            historico.setVisible(true);
        });

        // Ação para gerar texto ao clicar no botão
        btnGerar.addActionListener(e -> gerarTexto());

        // Adiciona o KeyListener para o botão "Gerar"
        btnGerar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gerarTexto();
                }
            }
        });
    }

    private void gerarTexto() {
        // Validações
        if (textFieldChamado.getText().isEmpty() || textFieldCategoria.getText().isEmpty() ||
                textFieldRequerente.getText().isEmpty() || comboBoxStatus.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String chamado = textFieldChamado.getText();
        if (!chamado.matches("\\d{1,6}")) {
            JOptionPane.showMessageDialog(this, "O N° de chamado deve ser um número inteiro com no máximo 6 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String requerente = textFieldRequerente.getText();
        if (!requerente.matches("^[\\p{L}]+\\s[\\p{L}]+$")) {
            JOptionPane.showMessageDialog(this, "O requerente deve conter nome e sobrenome.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String categoria = textFieldCategoria.getText();
        String status = comboBoxStatus.getSelectedItem().toString();

        // Texto gerado com emojis
        String textoGerado = String.format("📋 Chamado: https://servicos.redebahia.com.br/front/ticket.form.php?id=%s\n📂 Categoria: %s\n👤 Requerente: %s\n📝 Status: %s",
                chamado, categoria, requerente, status);

        // Copiar para a área de transferência
        StringSelection stringSelection = new StringSelection(textoGerado);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        // Exibir mensagem de sucesso
        JOptionPane.showMessageDialog(this, "Texto copiado com sucesso! ✨", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        // Salvar no banco de dados
        salvarNoHistorico(chamado, categoria, requerente, status);
    }

    private void salvarNoHistorico(String chamado, String categoria, String requerente, String status) {
        String sql = "INSERT INTO Historico (numero_chamado, categoria, requerente, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, chamado);
            pstmt.setString(2, categoria);
            pstmt.setString(3, requerente);
            pstmt.setString(4, status);
            pstmt.executeUpdate();

            System.out.println("Dados salvos com sucesso no histórico!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TelaPrincipal frame = new TelaPrincipal();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

