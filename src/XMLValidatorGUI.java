//import javax.swing.*;
//import javax.xml.XMLConstants;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.validation.Schema;
//import javax.xml.validation.SchemaFactory;
//import javax.xml.validation.Validator;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.IOException;
//
//public class XMLValidatorGUI extends JFrame implements ActionListener {
//    private JTextField xsdField, xmlField;
//    private JButton validateButton;
//
//    public XMLValidatorGUI() {
//        setTitle("XML Validator");
//        setSize(400, 200);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new GridLayout(3, 1));
//
//        xsdField = new JTextField();
//        xmlField = new JTextField();
//        validateButton = new JButton("Validate");
//
//        validateButton.addActionListener(this);
//
//        add(new JLabel("XSD File Path:"));
//        add(xsdField);
//        add(new JLabel("XML File Path:"));
//        add(xmlField);
//        add(validateButton);
//
//        setVisible(true);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == validateButton) {
//            String xsdFilePath = xsdField.getText();
//            String xmlFilePath = xmlField.getText();
//            validateXML(xsdFilePath, xmlFilePath);
//        }
//    }
//
//    private void validateXML(String xsdFilePath, String xmlFilePath) {
//        try {
//            // Load the XSD schema
//            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            Schema schema = factory.newSchema(new File(xsdFilePath));
//
//            // Create a validator
//            Validator validator = schema.newValidator();
//
//            // Set up a custom error handler
//            CustomErrorHandler errorHandler = new CustomErrorHandler();
//            validator.setErrorHandler(errorHandler);
//
//            // Validate the XML file
//            validator.validate(new StreamSource(new File(xmlFilePath)));
//
//            if (errorHandler.hasErrors()) {
//                JOptionPane.showMessageDialog(this, "XML is not valid. Validation errors:\n" + errorHandler.getErrors());
//            } else {
//                JOptionPane.showMessageDialog(this, "XML is valid.", "Validation Success", JOptionPane.INFORMATION_MESSAGE);
//            }
//
//        } catch (SAXException | IOException ex) {
//            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        new XMLValidatorGUI();
//    }
//}
//
//class CustomErrorHandler implements org.xml.sax.ErrorHandler {
//    private StringBuilder errors = new StringBuilder();
//
//    @Override
//    public void warning(SAXParseException exception) throws SAXException {
//        errors.append("Warning: ").append(getErrorLocation(exception)).append(" - ").append(exception.getMessage()).append("\n");
//    }
//
//    @Override
//    public void error(SAXParseException exception) throws SAXException {
//        errors.append("Error: ").append(getErrorLocation(exception)).append(" - ").append(exception.getMessage()).append("\n");
//    }
//
//    @Override
//    public void fatalError(SAXParseException exception) throws SAXException {
//        errors.append("Fatal Error: ").append(getErrorLocation(exception)).append(" - ").append(exception.getMessage()).append("\n");
//    }
//
//    public boolean hasErrors() {
//        return errors.length() > 0;
//    }
//
//    public String getErrors() {
//        return errors.toString();
//    }
//
//    private String getErrorLocation(SAXParseException exception) {
//        return "Line " + exception.getLineNumber() + ", Column " + exception.getColumnNumber();
//    }
//}
//
