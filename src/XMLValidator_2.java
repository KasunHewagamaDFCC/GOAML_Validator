//import javax.xml.XMLConstants;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.validation.Schema;
//import javax.xml.validation.SchemaFactory;
//import javax.xml.validation.Validator;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;
//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;
//
//public class XMLValidator_2 {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter the path to the XSD schema file:");
//        String xsdFilePath = scanner.nextLine();
//
//        System.out.println("Enter the path to the XML file to validate:");
//        String xmlFilePath = scanner.nextLine();
//
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
//                System.out.println("XML is not valid. Validation errors:");
//                errorHandler.printErrors();
//            } else {
//                System.out.println("XML is valid.");
//            }
//
//        } catch (SAXException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            scanner.close(); // Close the scanner to prevent resource leaks
//        }
//    }
//}
//
//class CustomErrorHandler implements org.xml.sax.ErrorHandler {
//    private boolean hasErrors = false;
//
//    @Override
//    public void warning(SAXParseException exception) throws SAXException {
//        hasErrors = true;
//        System.err.println("Warning: " + getErrorLocation(exception) + " - " + exception.getMessage());
//    }
//
//    @Override
//    public void error(SAXParseException exception) throws SAXException {
//        hasErrors = true;
//        System.err.println("Error: " + getErrorLocation(exception) + " - " + exception.getMessage());
//    }
//
//    @Override
//    public void fatalError(SAXParseException exception) throws SAXException {
//        hasErrors = true;
//        System.err.println("Fatal Error: " + getErrorLocation(exception) + " - " + exception.getMessage());
//    }
//
//    public boolean hasErrors() {
//        return hasErrors;
//    }
//
//    public void printErrors() {
//        // You can customize how you want to handle the error output here.
//    }
//
//    private String getErrorLocation(SAXParseException exception) {
//        return "Line " + exception.getLineNumber() + ", Column " + exception.getColumnNumber();
//    }
//}
