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

public class XMLValidatorGUI_Name extends JFrame implements ActionListener {
    private JTextField xsdField, xmlField;
    private JButton xsdBrowseButton, xmlBrowseButton, validateButton;
    private JTextArea resultArea;
    private JLabel nameLabel;

    public XMLValidatorGUI_Name() {
        setTitle("XML Validator");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        xsdField = new JTextField(30);
        xsdBrowseButton = new RoundedButton("Browse");
        xsdBrowseButton.addActionListener(this);
        inputPanel.add(new JLabel("XSD File Path:"), gbc);
        gbc.gridx++;
        inputPanel.add(xsdField, gbc);
        gbc.gridx++;
        inputPanel.add(xsdBrowseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        xmlField = new JTextField(30);
        xmlBrowseButton = new RoundedButton("Browse");
        xmlBrowseButton.addActionListener(this);
        inputPanel.add(new JLabel("XML File Path:"), gbc);
        gbc.gridx++;
        inputPanel.add(xmlField, gbc);
        gbc.gridx++;
        inputPanel.add(xmlBrowseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        validateButton = new RoundedButton("Validate");
        validateButton.addActionListener(this);
        inputPanel.add(validateButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        try {
            ImageIcon imageIcon = new ImageIcon("Logo/logo_new.png");
            Image originalImage = imageIcon.getImage();
            int newWidth = 80;
            int newHeight = -1;
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(scaledIcon);
            bottomPanel.add(imageLabel, BorderLayout.WEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        nameLabel = new JLabel("Powered By DWBI ");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setFont(new Font("Corbel Light", Font.BOLD, 12));
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
        new XMLValidatorGUI_Name();
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

    static class RoundedButton extends JButton {
        private int arcWidth = 100;
        private int arcHeight = 75;
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(true);
            setFocusPainted(true);
            setBorderPainted(false);
            setOpaque(true);
            setBackground(Color.lightGray);
            setForeground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            super.paintComponent(g2d);
            g2d.dispose();
        }
    }
}
