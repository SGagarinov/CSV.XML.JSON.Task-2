import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        String fileName = "data.xml";

        List<Employee> list = parseXML(fileName);
        writeString("result.json", listToJson(list));
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> employees = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName));
        NodeList employeeElements = document.getDocumentElement().getElementsByTagName("employee");

        for (int i = 0; i < employeeElements.getLength(); i++) {
            Element employee = (Element) employeeElements.item(i);

            employees.add(new Employee(Long.parseLong(getTagValue(employee, "id")),
                    getTagValue(employee, "firstName"),
                    getTagValue(employee, "lastName"),
                    getTagValue(employee, "country"),
                    Integer.parseInt(getTagValue(employee, "age"))
            ));
        }

        return employees;
    }

    public static String getTagValue(Element input, String tag) {
        NodeList empNodeList = input.getElementsByTagName(tag);
        Element firstElem = (Element) empNodeList.item(0);
        NodeList first = firstElem.getChildNodes();
        return ((Node) first.item(0)).getNodeValue();
    }

    public static String listToJson(List<Employee> input) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();

        return gson.toJson(input, listType);
    }

    public static void writeString(String fileName, String data) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data);
        } catch (RuntimeException ex) {

        }
    }
}
