package baekjoonAlgorithmStudy;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class XmlParsingTest {
	public static void main (String[] args) throws ParserConfigurationException, TransformerException {
		//GSON 으로 xml 파싱해서 값 추출해내기 작업하자.
		String str = "{\"medpntList\":[{\"empl_numb\":\"20310\",\"medi_pntx_sum\":\"40000\"}],\"rescode\":\"0\",\"resmsg\":\"\"}";
		
		JsonParser Parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) Parser.parse(str);
		JsonArray memberArray = (JsonArray) jsonObj.get("medpntList");
		      
		List<NcfSelMedpntResVO> resList = new ArrayList<NcfSelMedpntResVO>(); 
		
		for (int i = 0; i < memberArray.size(); i++) {          
			JsonObject object = (JsonObject) memberArray.get(i);
			
			NcfSelMedpntResVO SelResVO = new NcfSelMedpntResVO();
			SelResVO.setEmpl_numb(object.get("empl_numb").getAsString());
			SelResVO.setMedi_pntx_sum(object.get("medi_pntx_sum").getAsString());
			SelResVO.setRescode(jsonObj.get("rescode").getAsString());
			SelResVO.setResmsg(jsonObj.get("resmsg").getAsString());

			resList.add(SelResVO);   
		}
		
		// XML 문서 파싱
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	
	    // 새로운 XML 생성! //
	    // 새로운 Document 객체 생성
	    Document document = documentBuilder.newDocument();
	    
	    // root 생성
	    Element root = document.createElement("nurionXml");
	    
	    // 자식 노드 생성
	    Element resultCode = document.createElement("resultCode");

	    // 텍스트 설정
	    resultCode.setTextContent("200");
	    
	    Element resultXml = document.createElement("resultXml");
	    
	    Element xmlMain = document.createElement("xml");

	    xmlMain.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:s","uuid:BDC6E3F0-6DA3-11d1-A2A3-00AA00C14882");
	    xmlMain.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:dt","uuid:C2F41010-65B3-11d1-A29F-00AA00C14882");
	    xmlMain.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:rs","urn:schemas-microsoft-com:rowset");
	    xmlMain.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:z","#RowsetSchema");
	    
	    Element sSchema = document.createElement("s:Schema");
	    sSchema.setAttribute("id", "RowsetSchema");

	    Element sElementType = document.createElement("s:ElementType");
	    sElementType.setAttribute("name", "'row'");
	    sElementType.setAttribute("content", "'eltOnly'");
	    sElementType.setAttribute("rs:updatable", "'true'");
	    
	
	    document.appendChild(root);
	    root.appendChild(resultCode);
	    root.appendChild(resultXml);
	    resultXml.appendChild(xmlMain);
	   
	    xmlMain.appendChild(sSchema);
	    sSchema.appendChild(sElementType);
	    
	    //반복 스타트 
	    List<String> fieldList = Arrays.asList("empl_numb", "medi_pntx_sum", "rescode", "resmsg");

	    for (String FieldValue : fieldList) {
	    	Element sAttributeType = document.createElement("s:AttributeType");
		    sAttributeType.setAttribute("name", FieldValue);
		    sAttributeType.setAttribute("rs:number", String.valueOf(fieldList.indexOf(FieldValue)+1));
		    sAttributeType.setAttribute("rs:nullable", "true");
		    sAttributeType.setAttribute("rs:basetable", "DUAL");
		    sAttributeType.setAttribute("rs:basecolumn", FieldValue);
		    sAttributeType.setAttribute("rs:keycolumn", "true");
	
		    Element sDataType = document.createElement("s:dataType");
		    sDataType.setAttribute("dt:type", "string");
		    sDataType.setAttribute("dt:dbtype", "str");
		    sDataType.setAttribute("dt:maxLength", "100");
		    sDataType.setAttribute("dt:scale", "0");
		    sDataType.setAttribute("dt:fixedlength", "true");
		    

		    sElementType.appendChild(sAttributeType);
		    sAttributeType.appendChild(sDataType);
	    }
	    Element sExtends = document.createElement("s:extends");
	    sExtends.setAttribute("type", "rs:rowbase");
	    sElementType.appendChild(sExtends);
	    
	    
	    for (NcfSelMedpntResVO rtnVO : resList) {
	    	Element rsData = document.createElement("rs:data");
		    
		    Element zRow = document.createElement("z:row");
		    zRow.setAttribute("empl_numb", rtnVO.getEmpl_numb());
		    zRow.setAttribute("medi_pntx_sum", rtnVO.getMedi_pntx_sum());
		    zRow.setAttribute("rescode", rtnVO.getRescode());
		    zRow.setAttribute("resmsg", rtnVO.getResmsg());
		    
		    xmlMain.appendChild(rsData);
		    rsData.appendChild(zRow);
	    }
	    
	    //반복엔드
	    
	    
	    // XML 문자열로 변환하기! //
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    
	    DOMSource domsrc = new DOMSource(document);
	    StreamResult result = new StreamResult(baos);
	    
	    TransformerFactory transFactory = TransformerFactory.newInstance();
	    Transformer transformer = transFactory.newTransformer();
	    
	    // 출력 시 문자코드: UTF-8
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    // 들여 쓰기 있음
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.transform(domsrc, result);
	    
	    System.out.println(new String(baos.toByteArray(), StandardCharsets.UTF_8));
	    
	}
}
