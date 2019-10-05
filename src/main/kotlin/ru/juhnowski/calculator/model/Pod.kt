package ru.juhnowski.calculator.model

import javax.xml.bind.annotation.*

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Pod {
    @XmlAttribute
    var scanner: String? = null

    @XmlElement
    var subpod: Subpod? = null

    @XmlAttribute
    var numsubpods: Int? = null

    @XmlElement
    var expressiontypes: Expressiontypes? = null

    @XmlAttribute
    var id: String? = null

    @XmlAttribute
    var position: Int? = null

    @XmlAttribute
    var title: String? = null

    @XmlAttribute
    var error: Boolean? = null

    override fun toString(): String {
        return "ClassPojo [scanner = $scanner, subpod = $subpod, numsubpods = $numsubpods, expressiontypes = $expressiontypes, id = $id, position = $position, title = $title, error = $error]"
    }
}
