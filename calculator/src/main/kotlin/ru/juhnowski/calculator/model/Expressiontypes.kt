package ru.juhnowski.calculator.model

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Expressiontypes {
    @XmlElement
    var expressiontype: Expressiontype? = null

    override fun toString(): String {
        return "ClassPojo [expressiontype = $expressiontype]"
    }
}
