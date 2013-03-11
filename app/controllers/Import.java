package controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import models.Child;
import models.Home;
import models.Interview;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utils.ImportUtils;
import utils.ImportUtils.ChildUtil;
import views.html.admin.child;

// intentionally un-secured to facilitate import
public class Import extends Controller {
	
	private static final String UNKNOWN_HOME_NAME = "UN-KNOWN";
	
	private static String[] importFieldNames = {
		"name","age","gender",
		"cwcId","homeAdmissionId","parent",
		"nativeTown","nativeState"
	};
	
	public static Result importChild()
	{	
		return badRequest("Depricated. use /import/bulk instead");
	}
	private static final String[] IMPORT_NAME_KEYS = ImportUtils.IMPORT_NAME_KEYS;
	public static Result importBulk()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		StringBuilder statusReport = new StringBuilder();
		
		MultipartFormData formdata = request().body().asMultipartFormData();
		FilePart bulkXml = formdata.getFile("BULK_FILE");
		
		if(bulkXml ==null)
		{
			return badRequest("expected xml file with child information");
		}
		
		Document document = null;
		try
		{
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bulkXml.getFile());			
			NodeList children = document.getElementsByTagName("child");
			int childCount = (children == null)?0:children.getLength();
			
			
			
			for(int i=0;i<childCount;i++)
			{
				Node child = children.item(i);
				if(child.getNodeType() == Node.ELEMENT_NODE)
				{
					Element e = (Element) child;
					String[] params = new String[IMPORT_NAME_KEYS.length];
					int index =0;
					for(String key : IMPORT_NAME_KEYS){
						params[index] = e.getElementsByTagName(key).item(0).getTextContent();
						if(params[index]!=null){
							params[index] = params[index].trim();
						}
						index++;
					}
					
					String error = ImportUtils.validateChildInput(params);
					
					if(error == null)
					{
						ChildUtil cu = new ChildUtil(params);
						
						Child c = Child.create(cu.getName(),
								   cu.getAge(), 
								   new Date(), 
								   cu.getGender(),
								   Home.findByName(UNKNOWN_HOME_NAME),
								   cu.getCwcId(),
								   cu.getHomeAdmissionId(),
								   cu.getParent(),
								   cu.getTown(),
								   cu.getState()
								  );
						c.fill();
						
						//Logger.info(String.format("[%s][%s][%s][%s][%s][%s][%s][%s][%s]",params));
						
						NodeList interviews = e.getElementsByTagName("interview");
						int intCount = (interviews == null)?0:interviews.getLength();
						for(int j=0;j<intCount;j++)
						{
							Node interview = interviews.item(j);
							
							if(interview!=null && (interview.getNodeType() == Node.ELEMENT_NODE))
							{
								Element eI = (Element) interview;
								Date d = null;
								try{
									d = sdf.parse(eI.getElementsByTagName("date").item(0).getTextContent().trim());
									String text = eI.getElementsByTagName("text").item(0).getTextContent().trim();
									if(!text.isEmpty())
									{
										Interview.create(d, text, c);
									}
								}catch(ParseException pe){
									statusReport.append((String.format("%s ",params[0]))+"\n"+"interview skipped. invalid date,expected MM/DD/yyyy "+"\n");
								}
							}
						}
					}else{
						statusReport.append((String.format("[%s][%s][%s][%s][%s][%s][%s][%s][%s]",params))+"\n"+error+"\n");
					}
				}
			}
			
		}catch(Exception ioe){
			Logger.error("ex", ioe);
			return badRequest("malformed input");
		}
		
		return ok(statusReport.toString());
	}

}

/**
  	<?xml version="1.0" encoding="UTF-8"?>
  	<import>
  		<child>
  			<name><name>
  			<age></age>
  			<dob></dob>
  			<gender></gender>  			  			
  			<cwcId></cwcId>
  			<homeAdmissionId></homeAdmissionId>
  			<parent></parent>
  			<town></town>
  			<state></state>
  			<interviews>
  				<interview>
  					<date/>
  					<text/>
  				</interview>
  			</interviews>
  		</child>
  	</import> 
**/