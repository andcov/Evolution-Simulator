package evolution.simulator;
/*
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Configuration {
	public static void main(String[] args) {
        String filePath = "Resources/config.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("Colony");
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<Colony> colList = new ArrayList<Colony>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                colList.add(getEmployee(nodeList.item(i)));
            }
            //lets print Employee list information
            for (Colony col : colList) {
                System.out.println(col.toString());
            }
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }

    }


    private static Colony getEmployee(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
    		Colony col = new Colony();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            col.setColor(getTagValue("color", element));
            col.setPopulation(Integer.parseInt(getTagValue("population", element)));
        }

        return col;
    }


    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

	
}
*/

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
 
public class Configuration 
{
    public static ArrayList<Colony> initColonies()
    {
        // parsing file "config.json"
		Object obj = null;
		
		try {
			obj = new JSONParser().parse(new FileReader("Resources/config.json"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
         
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
         
        // getting colonies
        JSONArray ja = (JSONArray) jo.get("colonies");
         
        ArrayList<Colony> colonies = new ArrayList<Colony>();
        
        for (Object objColony : ja) {
        	    JSONObject jsonColony = (JSONObject) objColony;
        	    Colony col = new Colony();
        	    col.setColor(Integer.parseInt((String)jsonColony.get("colorRed")),
        	    		Integer.parseInt((String)jsonColony.get("colorGreen")),
        	    		Integer.parseInt((String) jsonColony.get("colorBlue")));
        	    col.setPopulation(Integer.parseInt((String) jsonColony.get("population")));
        	    col.setxPosition(Integer.parseInt((String) jsonColony.get("xPosition")));
        	    col.setyPosition(Integer.parseInt((String) jsonColony.get("yPosition")));
        	    col.setFemalePopulation(Integer.parseInt((String) jsonColony.get("femalePopulation")));
        	    col.setReproductionCycle(-1/*Integer.parseInt((String)jsonColony.get("reproductionCycle"))*/);
        	    col.setStrength(Integer.parseInt((String) jsonColony.get("strength")));
        	    col.setEnergy(Integer.parseInt((String) jsonColony.get("energy")));
        	    colonies.add(col);
        }
        return colonies;
    }
    
    public static int[][] initWealth()
    {
        // parsing file "config.json"
		Object obj = null;
		
		try {
			obj = new JSONParser().parse(new FileReader("Resources/config.json"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
         
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
         
        // getting colonies
        JSONArray ja = (JSONArray) jo.get("wealthRegions");
         
        int[][] wealthRegions = new int[1357][628];
        int defaultWealth = Integer.parseInt((String) jo.get("defaultWealth"));
        initRegion(wealthRegions, defaultWealth, 0, 0, 1356, 627);
        
        for (Object objRegion : ja) {
        	    JSONObject jsonRegion = (JSONObject) objRegion;
        	    int xTopLeft, yTopLeft, xBottomRight, yBottomLeft;
        	    int localWealth;
        	    localWealth = Integer.parseInt((String) jsonRegion.get("wealth"));
        	    xTopLeft = Integer.parseInt((String) jsonRegion.get("xTopLeft"));
        	    yTopLeft = Integer.parseInt((String) jsonRegion.get("yTopLeft"));
        	    xBottomRight = Integer.parseInt((String) jsonRegion.get("xBottomRight"));
        	    yBottomLeft = Integer.parseInt((String) jsonRegion.get("yBottomLeft"));
        	    initRegion(wealthRegions, localWealth, xTopLeft, yTopLeft, xBottomRight, yBottomLeft);
        }
        return wealthRegions;
    }
    
    public static void initRegion(int[][] wealthRegions, int w, int x1, int y1, int x2, int y2) {
    		for(int i = x1; i <= x2; i++) {
    			for(int j = y1; j <= y2; j++) {
    				wealthRegions[i][j] = w;
        		}
    		}
    }
}









