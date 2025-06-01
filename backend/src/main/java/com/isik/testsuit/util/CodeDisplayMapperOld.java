package com.isik.testsuit.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class CodeDisplayMapperOld {
    private Map<String, String> codeToDisplay = new HashMap<>();
    private Map<String, String> displayToCode = new HashMap<>();

    public CodeDisplayMapperOld(String filePath) {
        loadMappings(filePath);
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
        String test = "";
    }

    public String getDisplay(String code) {
        return codeToDisplay.getOrDefault(code, "Unbekannter Code");
    }

    public String getCode(String display) {
        return displayToCode.getOrDefault(display, "Unbekannter Display-Wert");
    }

    public static void main(String[] args) {
        CodeDisplayMapperOld mapper = new CodeDisplayMapperOld("src/main/resources/code.xml");
        System.out.println("Display für VL160110: " + mapper.getDisplay("VL160110"));
        System.out.println("Code für Molekularpathologiebefund: " + mapper.getCode("Molekularpathologiebefund"));
    }
}
