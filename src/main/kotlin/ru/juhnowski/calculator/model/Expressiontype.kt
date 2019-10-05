package ru.juhnowski.calculator.model

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Expressiontype {
    @XmlElement
    var name: String? = null

    override fun toString(): String {
        return "ClassPojo [name = $name]"
    }
}
