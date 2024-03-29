package ru.juhnowski.calculator.calculation

import ru.juhnowski.calculator.cache.Cache.FUNCTIONS
import ru.juhnowski.calculator.cache.CachedFunction
import ru.juhnowski.calculator.model.*
import ru.juhnowski.calculator.plotter.Formula
import ru.juhnowski.calculator.plotter.Plot
import java.io.StringWriter
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

class Calculate {

    val query = Queryresult();
    var pod1Title:String ="";
    var pod2Title:String ="";

    fun insertCharacterForEveryNDistance(distance: Int, original: String, c: Char): String {
        val sb = StringBuilder()
        val charArrayOfOriginal = original.trim().toCharArray();
        for (ch in charArrayOfOriginal.indices) {
            if (ch % distance == 0)
                sb.append(c).append(charArrayOfOriginal[ch])
            else
                sb.append(charArrayOfOriginal[ch])
        }
        return sb.toString()
    }

    fun calculate(expr: String): String {
        val h = expr.hashCode();

        if (FUNCTIONS.containsKey(h)){
            val cachedFunction = FUNCTIONS.get(h)!!
            pod1Title = cachedFunction.pod1Title
            pod2Title = cachedFunction.pod2Title
            println("find pod1Title=$pod1Title")
            return cachedFunction.response;
        }

        if (expr.length == 0) {
            val cf = stub();
            pod1Title = cf.pod1Title;
            pod2Title = cf.pod2Title;
            return cf.response;
        };

        val hash = expr.hashCode();

        val formulaUrl = "/formula_${hash}";
        val plotUrl = "/plot_${hash}";

        val plot = Plot.plot(null);

        val data = Evaluate.getData(expr);

        val x = ArrayList<Double>();
        val y = ArrayList<Double>();
        for ( k in data.keys.sorted()){
            x.add(k);
            y.add(data.get(k)!!);
        }


        plot.series(null, plot.data().xy(x,y), null)// setting data
        plot.save("upload-dir$plotUrl", "png")

        val formula = Formula.formula(null).series(null, Formula.data().xy(1.0, 2.0).xy(3.0, 4.0), null)

        val exprWithSpace =insertCharacterForEveryNDistance(1, expr, ' ');

        formula.formulaText = "f ( x ) = $exprWithSpace"
        formula.save("upload-dir$formulaUrl", "png")


        val jaxbContext = JAXBContext.newInstance(Queryresult::class.java)
        val marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)



        //==========================================================================

        query.success = true;
        query.error = false;
        query.numpods = 2;
        query.datatypes = "Plot";
        query.timedout = "";
        query.timedoutpods = "";
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
        pod1.scanner = "Identity";
        pod1.id = "Input";
        pod1.position = 100;
        pod1.error = false;
        pod1.numsubpods = 1;

        val subpod1 = Subpod();
        val img1 = Img();
        img1.src = "https://www4d.wolframalpha.com/Calculate/MSP/MSP1358244hb1999h19b3f0000033d4ii3i6e959i03?MSPStoreType=image/gif&amp;s=32";
        img1.alt = "plot | 2 x";
        img1.title = "plot | 2 x";
        img1.width = 103;
        img1.height = 32;
        img1.type = "Grid";
        img1.themes = "1,2,3,4,5,6,7,8,9,10,11,12";
        img1.colorinvertable = true;

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
        pod2.title = "Plot";
        pod2.scanner = "Plot";
        pod2.id = "Plot";
        pod2.position = 200;
        pod2.error = false;
        pod2.numsubpods = 1;

        val subpod2 = Subpod();
        subpod2.title = "";

        val img2 = Img();
        img2.src = "https://www4d.wolframalpha.com/Calculate/MSP/MSP1359244hb1999h19b3f00000402b062hi2fb1i35?MSPStoreType=image/gif&amp;s=32";
        img2.alt = "";
        img2.title = "";
        img2.width = 429;
        img2.height = 215;
        img2.type = "2DMathPlot_1";
        img2.themes = "1,2,3,4,5,6,7,8,9,10,11,12";
        img2.colorinvertable = true;

        subpod2.img = img2;
        subpod2.plaintext = "";

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
        query.timedout = "";
        query.timedoutpods = "";
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

        val responseXML = stringWriter.toString()
        pod1Title = "${pod1.title.toString()}"
        pod2Title = "${pod2.title.toString()}"
        println("first pod1Title=$pod1Title")
        val cachedFunction = CachedFunction(response=responseXML,pod1Title = pod1Title, pod2Title=pod2Title);
        FUNCTIONS.put(h,cachedFunction);

        return responseXML;
    }

}