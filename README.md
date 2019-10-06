# Description
Run application by:
- Idea IDE: run button 
- Maven: mvn spring-boot:run

Open URL http://localhost:8080/ in browser

Input expression in webform and press button.

The result will be response as list of 2 pods.

# REST API
- GET /alive - check that backend is a live

- GET /calc?expr="x+1" - return XML qeryResponse in wolframalpha's XML format
 
- GET /{filename:.+} - return image for pod

- GET / - for demo purpose only, demo html page

- GET /calc_test - for demo purpose only, response  html page with 2 pods 
 

# Development Requirements


- BackEnd Output
divided into rectangular regions - pods - corresponds to one category of results
each pod has a title and content - GIF image

API provides a number of formats in which results can be returned:


- Image

- Mathematica Cell

- Text:
 	* plaintext
	* MathML
	* Mathematica Input
	* Mathematica Output

- Query
 * http://api.wolframalpha.com/v2/query?input="plot 2x"&appid=K28L54-3UGRQA7TL7

- Response:
```xml
<queryresult success="true" error="false" numpods="2" datatypes="Plot" timedout="" timedoutpods="" timing="0.627" parsetiming="0.246" parsetimedout="false" recalculate="" id="MSPa1356244hb1999h19b3f0000010ahfh0ha9d50891" host="https://www4d.wolframalpha.com" server="32" related="https://www4d.wolframalpha.com/api/v1/relatedQueries.jsp?id=MSPa1357244hb1999h19b3f0000026c853b76916bihc3331039829427880662" version="2.6">
    <pod title="Input interpretation" scanner="Identity" id="Input" position="100" error="false" numsubpods="1">
        <subpod title="">
            <img src="https://www4d.wolframalpha.com/Calculate/MSP/MSP1358244hb1999h19b3f0000033d4ii3i6e959i03?MSPStoreType=image/gif&s=32" alt="plot | 2 x" title="plot | 2 x" width="103" height="32" type="Grid" themes="1,2,3,4,5,6,7,8,9,10,11,12" colorinvertable="true"/>
            <plaintext>plot | 2 x</plaintext>
        </subpod>
    <expressiontypes>
        <expressiontype name="Grid"/>
        </expressiontypes>
    </pod>
    
    <pod title="Plot" scanner="Plot" id="Plot" position="200" error="false" numsubpods="1">
        <subpod title="">
            <img src="https://www4d.wolframalpha.com/Calculate/MSP/MSP1359244hb1999h19b3f00000402b062hi2fb1i35?MSPStoreType=image/gif&s=32" alt="" title="" width="429" height="215" type="2DMathPlot_1" themes="1,2,3,4,5,6,7,8,9,10,11,12" colorinvertable="true"/>
            <plaintext/>
        </subpod>
        <expressiontypes>
            <expressiontype name="2DMathPlot"/>
        </expressiontypes>
    </pod>
</queryresult>
```
