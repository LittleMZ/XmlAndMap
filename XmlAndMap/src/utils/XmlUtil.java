package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * xml  map互转时,map类型为:
 * Map<String,Object>
 * 当为单节点，如<version>1.0.1</version>，Object为String类型
 * 非单节点时，Object存放Map<String,Object>类型
 * <p>
 * <p>
 * Created by M on 16/8/2.
 */
public class XmlUtil {
    private static XmlUtil xmlUtil = new XmlUtil();

    private XmlUtil() {
    }

    public static XmlUtil newInstance() {
        return xmlUtil;
    }

    /**
     * 根据xml文件地址组装map
     *
     * @param src
     * @return
     * @throws DocumentException
     */
    public Map<String, Object> xml2Map(String src) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(src);

        return xml2Map(doc);
    }

    /**
     * 根据xml文件对象组装map
     *
     * @param xmlFile
     * @return
     * @throws DocumentException
     */
    public Map<String, Object> xml2Map(File xmlFile) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(xmlFile);

        return xml2Map(doc);
    }

    /**
     * 根据xml的docment对象组装map
     *
     * @param doc
     * @return
     */
    public Map<String, Object> xml2Map(Document doc) {
        Map<String, Object> map = new HashMap<>();
        Element rootEle = doc.getRootElement();
        List rootEles = rootEle.elements();
        if (rootEles == null || rootEles.size() == 0) {
            //单节点Map<String, String>
            map.put(rootEle.getName(), rootEle.getText());
        } else {
            map.put(rootEle.getName(), element2Map(rootEle));
        }
        return map;
    }

    /**
     * 输入节点,返回其子节点的组装map
     *
     * @param element
     * @return
     */
    public Map<String, Object> element2Map(Element element) {
        Map<String, Object> mapEle = new HashMap<>();
        List list = new ArrayList();

        if (element == null) {
            return mapEle;
        }

        List<Element> elements = element.elements();
        if (elements == null || elements.size() == 0) {
            //单节点Map<String, String>
            mapEle.put(element.getName(), element.getText());
        } else {
            //多节点返回Map<String, Map>
            for (Element ele : elements) {
                List<Element> eles = ele.elements();
                if (eles == null || eles.size() == 0) {
                    mapEle.put(ele.getName(), ele.getText());
                } else {
                    mapEle.put(ele.getName(), element2Map(ele));
                }
            }
        }

        return mapEle;
    }


    /**
     * 形如Map<String,List>的集合,转换为对应编码的xmlString
     *
     * @param xmlMap
     * @return
     */
    public String map2XmlString(Map<String, Object> xmlMap, String charset) {
        Document doc = map2Xml(xmlMap);
        doc.setXMLEncoding(charset);
        return doc.asXML();
    }

    /**
     * 形如Map<String,List>的集合,转换为doc
     *
     * @param xmlMap
     * @return
     */
    public Document map2Xml(Map<String, Object> xmlMap) {
        Document doc = DocumentHelper.createDocument();
        Set<String> keys = xmlMap.keySet();
        if (keys.size() > 1) {
            throw new RuntimeException("wrong map");
        } else {
            for (String key : keys) {
                Element rootEle = DocumentHelper.createElement(key);
                Object obj = xmlMap.get(key);
                if (obj instanceof String) {
                    //单节点,形如<version>1.0.1</version>
                    rootEle.setText((String) obj);
                } else if (obj instanceof Map) {
                    //非单节点<key,map>
                    elementadd((Map) obj, rootEle);
                } else {
                    throw new RuntimeException("Wrong Map!");
                }
                doc.add(rootEle);
            }
        }

        return doc;
    }


    /**
     * 输入一个形如Map<String,Object>的集合,和ele节点,将map解析成ele的子树
     *
     * @param xmlMap
     * @param ele
     * @return
     */
    public void elementadd(Map<String, Object> xmlMap, Element ele) {

        if (ele == null || xmlMap == null || xmlMap.size() == 0) {
            return;
        }

        Set<String> keys = xmlMap.keySet();
        //遍历map
        for (String key : keys) {
            Element element = ele.addElement(key);
            Object obj = xmlMap.get(key);
            if (obj instanceof String) {
                //单节点,形如<version>1.0.1</version>
                element.setText((String) obj);
            } else if (obj instanceof Map) {
                //非单节点
                // Map mapSon = ;
                elementadd((Map) obj, element);

            } else {
                throw new RuntimeException("Wrong Map!");
            }
        }

    }

}
