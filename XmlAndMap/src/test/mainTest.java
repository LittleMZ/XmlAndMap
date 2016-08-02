package test;

import org.dom4j.DocumentException;
import utils.XmlUtil;

import java.util.Map;

/**
 * Created by M on 16/8/1.
 */
public class mainTest {
    public static void main(String[] args) throws DocumentException {
//        XmlUtil xmlUtil = XmlUtil.newInstance();
//        Map map = null;
//        try {
//            map = xmlUtil.xml2Map("request.xml");
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(map);
//        System.out.println("-------------------------------");
//        System.out.println(xmlUtil.map2XmlString(map));


        XmlUtil xmlUtil2 = XmlUtil.newInstance();
        Map map = null;
        try {
            map = xmlUtil2.xml2Map("XmlAndMap/request.xml");
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(map);
        System.out.println("-------------------------------");
        System.out.println(xmlUtil2.map2XmlString(map, "gbk"));
    }
}
