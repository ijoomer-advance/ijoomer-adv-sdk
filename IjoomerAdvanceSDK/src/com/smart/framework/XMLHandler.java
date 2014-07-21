package com.smart.framework;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XMLHandler extends DefaultHandler {

    private boolean currentNode = false;

    public String result = "";

    public ArrayList<String> nodes = new ArrayList<String>();


    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (nodes.contains(localName))

            //if(localName.equals("IngredientName"))
            currentNode = true;

    }


    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (nodes.contains(localName))
            //if(localName.equals("IngredientName"))
            currentNode = false;

    }


    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentNode)
            result += new String(ch, start, length) + "\n";

    }

}
