package ru.juhnowski.calculator.model

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class PlotResponse {
    @XmlElement
    var queryresult: Queryresult? = null

    override fun toString(): String {
        return "ClassPojo [queryresult = $queryresult]"
    }
}