package easyLearning.model.GUI;

import com.holub.database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static easyLearning.model.GUI.FileTypeFilter.createTableFromCSV;


public class userSelectFrame extends JFrame{

    private JPanel MainPanel;
    private JButton importButton;
    private JTextArea pleaseInsertFileFormatTextArea;
    private JTable table1;
    private JButton dropColumnButton;
    private JButton dropNANButton;
    private JButton submitButton;
    private JScrollPane scrollPane1;
    private JPanel MethodPanel;
    private JPanel EvaluationPanel;
    private JComboBox DMComboBox;

    private File file;

    private Database database;

    public userSelectFrame() {
        SwingUtilities.invokeLater(() -> {
                    setContentPane(MainPanel);

                    setTitle("미리보는 AI체험기");
                    setSize(1500, 1000);
                    setLocationRelativeTo(null);
                    //setResizable(false);
                    setBackground(new Color(83, 88, 76));

                    pack();
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setVisible(true);
                    this.database = new Database();
                    //this.DMPanel = new DMPanel();
                    populateDropdown("src/easyLearning/model/parsing/DistanceMeasurement.txt"); // 텍스트 파일 경로를 설정하세요
                });

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fs = new JFileChooser(new File("c:\\"));
                fs.setDialogTitle("Open a File");
                fs.setFileFilter(new FileTypeFilter(".csv", "CSV File"));
                int result = fs.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fs.getSelectedFile();
                    try {
                        pleaseInsertFileFormatTextArea.setText(file.getAbsolutePath());
                        loadCSVIntoTable(file);

                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, exception.getMessage());
                    }
                }

            }
        });
        dropColumnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    database.dropNaN(file);
                    updateTable();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });
    }

    private void loadCSVIntoTable(File csvFile) {
        try {
            // Read CSV file and create a DefaultTableModel
            DefaultTableModel model = createTableFromCSV(csvFile.getAbsolutePath());
            this.table1.setModel(model);
            scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            updateTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading CSV into table: " + e.getMessage());
        }
    }

    private void updateTable() {
        this.table1.repaint();
        pack();
    }

    private void populateDropdown(String filePath) {
        // 텍스트 파일을 읽어와서 드롭다운 버튼에 추가
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                DMComboBox.addItem(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
