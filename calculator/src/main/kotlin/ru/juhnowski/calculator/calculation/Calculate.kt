package ru.juhnowski.calculator.calculation

import ru.juhnowski.calculator.model.*
import java.io.StringWriter
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

fun calculate(expr:String):String{
    if (expr.length==0) return stub();

    val jaxbContext = JAXBContext.newInstance(Queryresult::class.java)
    val marshaller = jaxbContext.createMarshaller()
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)

    val query = Queryresult();

    //==========================================================================

    query.success = true;
    query.error = false;
    query.numpods = 2;
    query.datatypes = "Plot";
    query.timedout="";
    query.timedoutpods="";
    query.timing = "0.627";
    query.parsetiming = "0.246"
    query.parsetimedout = false;
    query.recalculate = "";
    query.id = "MSPa1356244hb1999h19b3f0000010ahfh0ha9d50891";
    query.host = "https://www4d.wolframalpha.com";
    query.server = 32;
    query.related = "https://www4d.wolframalpha.com/api/v1/relatedQueries.jsp?id=MSPa1357244hb1999h19b3f0000026c853b76916bihc3331039829427880662";
    query.version = "2.6";

    //pod 1
    val pod1 = Pod();
    pod1.title = "Input interpretation";
    pod1.scanner="Identity";
    pod1.id="Input";
    pod1.position=100;
    pod1.error=false;
    pod1.numsubpods=1;

    val subpod1 = Subpod();
    val img1 = Img();
    img1.src="https://www4d.wolframalpha.com/Calculate/MSP/MSP1358244hb1999h19b3f0000033d4ii3i6e959i03?MSPStoreType=image/gif&amp;s=32";
    img1.alt="plot | 2 x";
    img1.title="plot | 2 x";
    img1.width=103;
    img1.height=32;
    img1.type="Grid";
    img1.themes="1,2,3,4,5,6,7,8,9,10,11,12";
    img1.colorinvertable=true;

    subpod1.img = img1;
    subpod1.plaintext = "plot | 2 x";
    subpod1.title = "";
    pod1.subpod = subpod1;

    val expressiontype1 = Expressiontype();
    expressiontype1.name = "Grid";

    val expressiontypes1 = Expressiontypes();
    expressiontypes1.expressiontype = expressiontype1;

    pod1.subpod = subpod1;
    pod1.expressiontypes = expressiontypes1;

    //pod 2
    val pod2 = Pod();
    pod2.title="Plot";
    pod2.scanner="Plot";
    pod2.id="Plot";
    pod2.position=200;
    pod2.error=false;
    pod2.numsubpods=1;

    val subpod2 = Subpod();
    subpod2.title = "";

    val img2 = Img();
    img2.src="https://www4d.wolframalpha.com/Calculate/MSP/MSP1359244hb1999h19b3f00000402b062hi2fb1i35?MSPStoreType=image/gif&amp;s=32";
    img2.alt="";
    img2.title="";
    img2.width=429;
    img2.height=215;
    img2.type="2DMathPlot_1";
    img2.themes="1,2,3,4,5,6,7,8,9,10,11,12";
    img2.colorinvertable=true;

    subpod2.img = img2;
    subpod2.plaintext="";

    pod2.subpod = subpod2;

    val expressiontypes2 = Expressiontypes();
    val expressiontype2 = Expressiontype();

    expressiontypes2.expressiontype = expressiontype2;

    pod2.expressiontypes = expressiontypes2;

    query.pod = arrayOf(pod1, pod2);

    //==========================================================================
    query.success = true;
    query.error = false;
    query.numpods = 2;
    query.datatypes = "Plot";
    query.timedout="";
    query.timedoutpods="";
    query.timing = "0.627";
    query.parsetiming = "0.246"
    query.parsetimedout = false;
    query.recalculate = "";
    query.id = expr.hashCode().toString();
    query.host = "localhost:8080"; //TODO: replace spring configuration parameter
    query.server = 32;
    query.related = "https://www4d.wolframalpha.com/api/v1/relatedQueries.jsp?id=MSPa1357244hb1999h19b3f0000026c853b76916bihc3331039829427880662";
    query.version = "2.6";
    //==========================================================================
    val stringWriter = StringWriter()
    stringWriter.use {
        marshaller.marshal(query, stringWriter)
    }

    return stringWriter.toString();
}
