package com.PastPest.competition1.API;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DetailAPIData {
    NodeList dataList;
    public NodeList load_Data(String URL) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


            Document doc = builder.parse(URL);

            doc.getDocumentElement().normalize();
            dataList=doc.getElementsByTagName("service");
            return dataList;
        }
        catch (Exception e){
            System.out.println(e.getClass());
            return null;
        }
    }
}
