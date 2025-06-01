package com.isik.testsuit.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.isik.testsuit.exception.CodeDisplayMapperException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class CodeDisplayMapper {
    private static CodeDisplayMapper instance;
    private Map<String, String> codeToDisplay = new HashMap<>();
    private Map<String, String> displayToCode = new HashMap<>();
    private static boolean isLoaded = false;

    private static final String filePath = "src/main/resources/code.xml";

    private CodeDisplayMapper() {
        if (!isLoaded) {
            loadMappings(filePath);
            isLoaded = true;
        }
    }

    public static CodeDisplayMapper getInstance() {
        if (instance == null) {
            synchronized (CodeDisplayMapper.class) {
                if (instance == null) {
                    instance = new CodeDisplayMapper();
                }
            }
        }
        return instance;
    }

    private void loadMappings(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("concept");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String code = element.getElementsByTagName("code").item(0).getAttributes().getNamedItem("value").getNodeValue();
                String display = element.getElementsByTagName("display").item(0).getAttributes().getNamedItem("value").getNodeValue();

                codeToDisplay.put(code, display);
                displayToCode.put(display, code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDisplay(String code) throws CodeDisplayMapperException {
        String output = codeToDisplay.get(code);
        if (output == null) {
            throw new CodeDisplayMapperException("Unbekannter Code");
        }
        return output;
    }

    public String getCode(String display) throws CodeDisplayMapperException {
        String output = displayToCode.get(display);
        if (output == null) {
            throw new CodeDisplayMapperException("Unbekannter Display-Wert");
        }
        return output;
    }
}
