import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLValidator_GUI extends JFrame implements ActionListener {
    private JTextField xsdField, xmlField;
    private JButton xsdBrowseButton, xmlBrowseButton, validateButton;
    private JTextArea resultArea;
    private JPanel bottomPanel;
    private JLabel nameLabel;

    public XMLValidator_GUI() {
        setTitle("XML Validator");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        xsdField = new JTextField(20);
        xsdBrowseButton = new JButton("Browse");
        xsdBrowseButton.addActionListener(this);
        inputPanel.add(new JLabel("XSD File Path:"), gbc);
        gbc.gridx++;
        inputPanel.add(xsdField, gbc);
        gbc.gridx++;
        inputPanel.add(xsdBrowseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        xmlField = new JTextField(20);
        xmlBrowseButton = new JButton("Browse");
        xmlBrowseButton.addActionListener(this);
        inputPanel.add(new JLabel("XML File Path:"), gbc);
        gbc.gridx++;
        inputPanel.add(xmlField, gbc);
        gbc.gridx++;
        inputPanel.add(xmlBrowseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        validateButton = new JButton("Validate");
        validateButton.addActionListener(this);
        inputPanel.add(validateButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        bottomPanel = new JPanel(new BorderLayout());

        // Load the image from a file or replace it with your own path
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\kasunhe\\Documents\\logo.jpg");
        Image image = imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);

        JLabel imageLabel = new JLabel(imageIcon);
        bottomPanel.add(imageLabel, BorderLayout.WEST);

        nameLabel = new JLabel("Powered By DWBI");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(nameLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == xsdBrowseButton) {
            selectFile(xsdField);
        } else if (e.getSource() == xmlBrowseButton) {
            selectFile(xmlField);
        } else if (e.getSource() == validateButton) {
            String xsdFilePath = xsdField.getText();
            String xmlFilePath = xmlField.getText();
            validateXML(xsdFilePath, xmlFilePath);
        }
    }

    private void selectFile(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void validateXML(String xsdFilePath, String xmlFilePath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdFilePath));
            Validator validator = schema.newValidator();
            CustomErrorHandler errorHandler = new CustomErrorHandler();
            validator.setErrorHandler(errorHandler);
            validator.validate(new StreamSource(new File(xmlFilePath)));

            if (errorHandler.hasErrors()) {
                resultArea.setText("XML is not valid. Validation errors:\n" + errorHandler.getErrors());
            } else {
                resultArea.setText("XML is valid.");
            }

        } catch (SAXException | IOException ex) {
            resultArea.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new XMLValidator_GUI();
    }

    static class CustomErrorHandler implements org.xml.sax.ErrorHandler {
        private List<String> errors = new ArrayList<>();

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            addError("Warning", exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            addError("Error", exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            addError("Fatal Error", exception);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public String getErrors() {
            return String.join("\n", errors);
        }

        private void addError(String type, SAXParseException exception) {
            String error = type + ": Line " + exception.getLineNumber() + ", Column " + exception.getColumnNumber() + " - " + exception.getMessage();
            errors.add(error);
        }
    }
}
