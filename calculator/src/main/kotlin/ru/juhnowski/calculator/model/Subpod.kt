package ru.juhnowski.calculator.model

import javax.xml.bind.annotation.*

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Subpod {
    @XmlElement
    var img: Img? = null

    @XmlElement
    var plaintext: String? = null

    @XmlAttribute
    var title: String? = null

    override fun toString(): String {
        return "ClassPojo [img = $img, plaintext = $plaintext, title = $title]"
    }
}
