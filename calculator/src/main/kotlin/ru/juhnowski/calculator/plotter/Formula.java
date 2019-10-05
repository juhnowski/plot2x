package ru.juhnowski.calculator.plotter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class Formula {

    public enum Line { NONE, SOLID, DASHED };
    public enum Marker { NONE, CIRCLE, SQUARE, DIAMOND, COLUMN, BAR };
    public enum AxisFormat { NUMBER, NUMBER_KGM, NUMBER_INT, TIME_HM, TIME_HMS, DATE, DATETIME_HM, DATETIME_HMS }
    public enum LegendFormat { NONE, TOP, RIGHT, BOTTOM }

    private enum HorizAlign { LEFT, CENTER, RIGHT }
    private enum VertAlign { TOP, CENTER, BOTTOM }

    private Formula.FormulaOptions opts = new Formula.FormulaOptions();

    private Rectangle boundRect;
    private Formula.FormulaArea formulaArea;
    private Map<String, Formula.Axis> xAxes = new HashMap<String, Formula.Axis>(3);
    private Map<String, Formula.Axis> yAxes = new HashMap<String, Formula.Axis>(3);
    private Map<String, Formula.DataSeries> dataSeriesMap = new LinkedHashMap<String, Formula.DataSeries>(5);

    public static Formula formula(Formula.FormulaOptions opts) {
        return new Formula(opts);
    }

    public static Formula.FormulaOptions FormulaOpts() {
        return new Formula.FormulaOptions();
    }

    public static class FormulaOptions {

        private String title = "";
        private int width = 800;
        private int height = 20;
        private Color backgroundColor = Color.BLACK;
        private Color foregroundColor = Color.WHITE;
        private Font titleFont = new Font("Arial", Font.BOLD, 16);
        private int padding = 10; // padding for the entire image
        private int FormulaPadding = 5; // padding for Formula area (to have min and max values padded)
        private int labelPadding = 10;
        private int defaultLegendSignSize = 10;
        private int legendSignSize = 10;
        private Point grids = new Point(10 ,10); // grid lines by x and y
        private Color gridColor = Color.GRAY;
        private Stroke gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, new float[] { 5.0f }, 0.0f);
        private int tickSize = 5;
        private Font labelFont = new Font("Arial", 0, 12);
        private Formula.LegendFormat legend = Formula.LegendFormat.NONE;

        private FormulaOptions() {}

        public Formula.FormulaOptions title(String title) {
            this.title = title;
            return this;
        }

        public Formula.FormulaOptions width(int width) {
            this.width = width;
            return this;
        }

        public Formula.FormulaOptions height(int height) {
            this.height = height;
            return this;
        }

        public Formula.FormulaOptions bgColor(Color color) {
            this.backgroundColor = color;
            return this;
        }

        public Formula.FormulaOptions fgColor(Color color) {
            this.foregroundColor = color;
            return this;
        }

        public Formula.FormulaOptions titleFont(Font font) {
            this.titleFont = font;
            return this;
        }

        public Formula.FormulaOptions padding(int padding) {
            this.padding = padding;
            return this;
        }

        public Formula.FormulaOptions FormulaPadding(int padding) {
            this.FormulaPadding = padding;
            return this;
        }

        public Formula.FormulaOptions labelPadding(int padding) {
            this.labelPadding = padding;
            return this;
        }

        public Formula.FormulaOptions labelFont(Font font) {
            this.labelFont = font;
            return this;
        }

        public Formula.FormulaOptions grids(int byX, int byY) {
            this.grids = new Point(byX, byY);
            return this;
        }

        public Formula.FormulaOptions gridColor(Color color) {
            this.gridColor = color;
            return this;
        }

        public Formula.FormulaOptions gridStroke(Stroke stroke) {
            this.gridStroke = stroke;
            return this;
        }

        public Formula.FormulaOptions tickSize(int value) {
            this.tickSize = value;
            return this;
        }

        public Formula.FormulaOptions legend(Formula.LegendFormat legend) {
            this.legend = legend;
            return this;
        }

    }

    private Formula(Formula.FormulaOptions opts) {
        if (opts != null)
            this.opts = opts;
        boundRect = new Rectangle(0, 0, this.opts.width, this.opts.height);
        formulaArea = new Formula.FormulaArea();
    }

    public Formula.FormulaOptions opts() {
        return opts;
    }

    public Formula xAxis(String name, Formula.AxisOptions opts) {
        xAxes.put(name, new Formula.Axis(name, opts));
        return this;
    }

    public Formula yAxis(String name, Formula.AxisOptions opts) {
        yAxes.put(name, new Formula.Axis(name, opts));
        return this;
    }

    public Formula series(String name, Formula.Data data, Formula.DataSeriesOptions opts) {
        Formula.DataSeries series = dataSeriesMap.get(name);
        if (opts != null)
            opts.setFormula(this);
        if (series == null) {
            series = new Formula.DataSeries(name, data, opts);
            dataSeriesMap.put(name, series);
        } else {
            series.data = data;
            series.opts = opts;
        }
        return this;
    }

    public Formula series(String name, Formula.DataSeriesOptions opts) {
        Formula.DataSeries series = dataSeriesMap.get(name);
        if (opts != null)
            opts.setFormula(this);
        if (series != null)
            series.opts = opts;
        return this;
    }

    private void calc(Graphics2D g) {
        formulaArea.calc(g);
    }

    private void clear() {
        formulaArea.clear();
        for (Formula.DataSeries series : dataSeriesMap.values())
            series.clear();
    }

    public String formulaText;

    private BufferedImage draw() {
        BufferedImage image = new BufferedImage(opts.width, opts.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            calc(g);
            drawLabel(g,formulaText,20,0, HorizAlign.LEFT, Formula.VertAlign.TOP);
            return image;
        } finally {
            g.dispose();
        }
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(opts.backgroundColor);
        g.fillRect(0, 0, opts.width, opts.height);
    }

    public void save(String fileName, String type) throws IOException {
        clear();
        BufferedImage bi = draw();
        File outputFile = new File(fileName + "." + type);
        ImageIO.write(bi, type, outputFile);
    }

    private class Legend {
        Rectangle rect;
        Rectangle2D labelRect;
        public int entryWidth;
        public int entryWidthPadded;
        public int entryCount;
        public int xCount;
        public int yCount;
    }

    private class FormulaArea {

        private Rectangle FormulaBorderRect = new Rectangle(); // boundRect | labels/legend | FormulaBorderRect | FormulaPadding | FormulaRect/clipRect
        private Rectangle FormulaRect = new Rectangle();
        private Rectangle FormulaClipRect = new Rectangle();
        private Formula.Legend legend = new Formula.Legend();

        private Formula.Range xFormulaRange = new Formula.Range(0, 0);
        private Formula.Range yFormulaRange = new Formula.Range(0, 0);

        public FormulaArea() {
            clear();
        }

        private void clear() {
            FormulaBorderRect.setBounds(boundRect);
            FormulaRectChanged();
        }

        private void offset(int dx, int dy, int dw, int dh) {
            FormulaBorderRect.translate(dx, dy);
            FormulaBorderRect.setSize(FormulaBorderRect.width - dx - dw, FormulaBorderRect.height - dy - dh);
            FormulaRectChanged();
        }

        private void FormulaRectChanged() {
            FormulaRect.setBounds(FormulaBorderRect.x + opts.FormulaPadding, FormulaBorderRect.y + opts.FormulaPadding,
                    FormulaBorderRect.width - opts.FormulaPadding * 2, FormulaBorderRect.height - opts.FormulaPadding * 2);
            xFormulaRange.setMin(FormulaRect.getX());
            xFormulaRange.setMax(FormulaRect.getX() + FormulaRect.getWidth());
            yFormulaRange.setMin(FormulaRect.getY());
            yFormulaRange.setMax(FormulaRect.getY() + FormulaRect.getHeight());

            FormulaClipRect.setBounds(FormulaBorderRect.x + 1, FormulaBorderRect.y + 1, FormulaBorderRect.width - 1, FormulaBorderRect.height - 1);
        }

        private void calc(Graphics2D g) {
            calcAxes(g);
            calcRange(true);
            calcRange(false);
            calcAxisLabels(g, true);
            calcAxisLabels(g, false);
            g.setFont(opts.titleFont);
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D titleRect = fm.getStringBounds(opts.title, g);
            g.setFont(opts.labelFont);
            fm = g.getFontMetrics();
            int xAxesHeight = 0, xAxesHalfWidth = 0;
            for (Map.Entry<String, Formula.Axis> entry : xAxes.entrySet()) {
                Formula.Axis xAxis = entry.getValue();
                xAxesHeight += toInt(xAxis.labelRect.getHeight()) + opts.labelPadding * 2;
                if (xAxis.labelRect.getWidth() > xAxesHalfWidth)
                    xAxesHalfWidth = toInt(xAxis.labelRect.getWidth());
            }
            int yAxesWidth = 0;
            for (Map.Entry<String, Formula.Axis> entry : yAxes.entrySet())
                yAxesWidth += toInt(entry.getValue().labelRect.getWidth()) + opts.labelPadding * 2;
            int dx = opts.padding + yAxesWidth;
            int dy = opts.padding + toInt(titleRect.getHeight() + opts.labelPadding);
            int dw = opts.padding;
            if (opts.legend != Formula.LegendFormat.RIGHT)
                dw += xAxesHalfWidth; // half of label goes beyond a Formula in right bottom corner
            int dh = opts.padding + xAxesHeight;
            // offset for legend
            Rectangle temp = new Rectangle(FormulaBorderRect); // save FormulaRect
            offset(dx, dy, dw, dh);
            calcLegend(g); // use FormulaRect
            FormulaBorderRect.setBounds(temp); // restore FormulaRect
            switch (opts.legend) {
                case TOP: dy += legend.rect.height + opts.labelPadding; break;
                case RIGHT: dw += legend.rect.width + opts.labelPadding; break;
                case BOTTOM: dh += legend.rect.height; break;
                default:
            }
            offset(dx, dy, dw, dh);
        }

        private void draw(Graphics2D g) {
            drawFormulaArea(g);
            drawGrid(g);
            drawAxes(g);
            drawLegend(g);
            // if check needed that content is inside padding
            //g.setColor(Color.GRAY);
            //g.drawRect(boundRect.x + opts.padding, boundRect.y + opts.padding, boundRect.width - opts.padding * 2, boundRect.height - opts.padding * 2);
        }

        private void drawFormulaArea(Graphics2D g) {
            g.setColor(opts.foregroundColor);
            g.drawRect(FormulaBorderRect.x, FormulaBorderRect.y, FormulaBorderRect.width, FormulaBorderRect.height);
            g.setFont(opts.titleFont);
            drawLabel(g, opts.title, FormulaBorderRect.x + toInt(FormulaBorderRect.getWidth() / 2), opts.padding, Formula.HorizAlign.CENTER, Formula.VertAlign.TOP);
        }

        private void drawGrid(Graphics2D g) {
            Stroke stroke = g.getStroke();
            g.setStroke(opts.gridStroke);
            g.setColor(opts.gridColor);

            int leftX = FormulaBorderRect.x + 1;
            int rightX = FormulaBorderRect.x + FormulaBorderRect.width - 1;
            int topY = FormulaBorderRect.y + 1;
            int bottomY = FormulaBorderRect.y + FormulaBorderRect.height - 1;

            for (int i = 0; i < opts.grids.x + 1; i++) {
                int x = toInt(FormulaRect.x + (FormulaRect.getWidth() / opts.grids.x) * i);
                g.drawLine(x, topY, x, bottomY);
            }

            for (int i = 0; i < opts.grids.y + 1; i++) {
                int y = toInt(FormulaRect.y + (FormulaRect.getHeight() / opts.grids.y) * i);
                g.drawLine(leftX, y, rightX, y);
            }

            g.setStroke(stroke);
        }

        private void calcAxes(Graphics2D g) {
            Formula.Axis xAxis = xAxes.isEmpty() ? new Formula.Axis("", null) : xAxes.values().iterator().next();
            Formula.Axis yAxis = yAxes.isEmpty() ? new Formula.Axis("", null) : yAxes.values().iterator().next();
            int xCount = 0, yCount = 0;
            for (Formula.DataSeries series : dataSeriesMap.values()) {
                if (series.opts.xAxis == null) {
                    series.opts.xAxis = xAxis;
                    xCount++;
                }
                if (series.opts.yAxis == null) {
                    series.opts.yAxis = yAxis;
                    yCount++;
                }
                series.addAxesToName();
            }
            if (xAxes.isEmpty() && xCount > 0)
                xAxes.put("x", xAxis);
            if (yAxes.isEmpty() && yCount > 0)
                yAxes.put("y", yAxis);
        }

        private void calcAxisLabels(Graphics2D g, boolean isX) {
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D rect = null;
            double w = 0, h = 0;
            Map<String, Formula.Axis> axes = isX ? xAxes : yAxes;
            int grids = isX ? opts.grids.x : opts.grids.y;
            for (Map.Entry<String, Formula.Axis> entry : axes.entrySet()) {
                Formula.Axis axis = entry.getValue();
                axis.labels = new String[grids + 1];
                axis.labelRect = fm.getStringBounds("", g);
                double xStep = axis.opts.range.diff / grids;
                for (int j = 0; j < grids + 1; j++) {
                    axis.labels[j] = formatDouble(axis.opts.range.min + xStep * j, axis.opts.format);
                    rect = fm.getStringBounds(axis.labels[j], g);
                    if (rect.getWidth() > w)
                        w = rect.getWidth();
                    if (rect.getHeight() > h)
                        h = rect.getHeight();
                }
                axis.labelRect.setRect(0, 0, w, h);
            }
        }

        private void calcRange(boolean isX) {
            for (Formula.DataSeries series : dataSeriesMap.values()) {
                Formula.Axis axis = isX ? series.opts.xAxis : series.opts.yAxis;
                if (axis.opts.dynamicRange) {
                    Formula.Range range = isX ? series.xRange() : series.yRange();
                    if (axis.opts.range == null)
                        axis.opts.range = range;
                    else {
                        if (range.max > axis.opts.range.max)
                            axis.opts.range.setMax(range.max);
                        if (range.min < axis.opts.range.min)
                            axis.opts.range.setMin(range.min);
                    }
                }
            }
            Map<String, Formula.Axis> axes = isX ? xAxes : yAxes;
            for (Iterator<Formula.Axis> it = axes.values().iterator(); it.hasNext(); ) {
                Formula.Axis axis = it.next();
                if (axis.opts.range == null)
                    it.remove();
            }
        }

        private void drawAxes(Graphics2D g) {
            g.setFont(opts.labelFont);
            g.setColor(opts.foregroundColor);

            int leftXPadded = FormulaBorderRect.x - opts.labelPadding;
            int rightX = FormulaBorderRect.x + FormulaBorderRect.width;
            int bottomY = FormulaBorderRect.y + FormulaBorderRect.height;
            int bottomYPadded = bottomY + opts.labelPadding;

            int axisOffset = 0;
            for (Map.Entry<String, Formula.Axis> entry : xAxes.entrySet()) {
                Formula.Axis axis = entry.getValue();
                double xStep = axis.opts.range.diff / opts.grids.x;

                drawLabel(g, axis.name, rightX + opts.labelPadding, bottomY + axisOffset, Formula.HorizAlign.LEFT, Formula.VertAlign.CENTER);
                g.drawLine(FormulaRect.x, bottomY + axisOffset, FormulaRect.x + FormulaRect.width, bottomY + axisOffset);

                for (int j = 0; j < opts.grids.x + 1; j++) {
                    int x = toInt(FormulaRect.x + (FormulaRect.getWidth() / opts.grids.x) * j);
                    drawLabel(g, formatDouble(axis.opts.range.min + xStep * j, axis.opts.format), x, bottomYPadded + axisOffset, Formula.HorizAlign.CENTER, Formula.VertAlign.TOP);
                    g.drawLine(x, bottomY + axisOffset, x, bottomY + opts.tickSize + axisOffset);
                }
                axisOffset += toInt(axis.labelRect.getHeight() + opts.labelPadding * 2);
            }

            axisOffset = 0;
            for (Map.Entry<String, Formula.Axis> entry : yAxes.entrySet()) {
                Formula.Axis axis = entry.getValue();
                double yStep = axis.opts.range.diff / opts.grids.y;

                drawLabel(g, axis.name, leftXPadded - axisOffset, FormulaBorderRect.y - toInt(axis.labelRect.getHeight() + opts.labelPadding), Formula.HorizAlign.RIGHT, Formula.VertAlign.CENTER);
                g.drawLine(FormulaBorderRect.x - axisOffset, FormulaRect.y + FormulaRect.height, FormulaBorderRect.x - axisOffset, FormulaRect.y);

                for (int j = 0; j < opts.grids.y + 1; j++) {
                    int y = toInt(FormulaRect.y + (FormulaRect.getHeight() / opts.grids.y) * j);
                    drawLabel(g, formatDouble(axis.opts.range.max - yStep * j, axis.opts.format), leftXPadded - axisOffset, y, Formula.HorizAlign.RIGHT, Formula.VertAlign.CENTER);
                    g.drawLine(FormulaBorderRect.x - axisOffset, y,  FormulaBorderRect.x - opts.tickSize - axisOffset, y);
                }
                axisOffset += toInt(axis.labelRect.getWidth() + opts.labelPadding * 2);
            }
        }

        private void calcLegend(Graphics2D g) {
            legend.rect = new Rectangle(0, 0);
            if (opts.legend == Formula.LegendFormat.NONE)
                return;
            int size = dataSeriesMap.size();
            if (size == 0)
                return;

            FontMetrics fm = g.getFontMetrics();
            Iterator<Formula.DataSeries> it = dataSeriesMap.values().iterator();
            legend.labelRect = fm.getStringBounds(it.next().nameWithAxes, g);
            int legendSignSize = opts.defaultLegendSignSize;
            while (it.hasNext()) {
                Formula.DataSeries series = it.next();
                Rectangle2D rect = fm.getStringBounds(series.nameWithAxes, g);
                if (rect.getWidth() > legend.labelRect.getWidth())
                    legend.labelRect.setRect(0, 0, rect.getWidth(), legend.labelRect.getHeight());
                if (rect.getHeight() > legend.labelRect.getHeight())
                    legend.labelRect.setRect(0, 0, legend.labelRect.getWidth(), rect.getHeight());
                switch (series.opts.marker) {
                    case CIRCLE: case SQUARE:
                        if (series.opts.markerSize + opts.defaultLegendSignSize > legendSignSize)
                            legendSignSize = series.opts.markerSize + opts.defaultLegendSignSize;
                        break;
                    case DIAMOND:
                        if (series.getDiagMarkerSize() + opts.defaultLegendSignSize > legendSignSize)
                            legendSignSize = series.getDiagMarkerSize() + opts.defaultLegendSignSize;
                        break;
                    default:
                }
            }
            opts.legendSignSize = legendSignSize;

            legend.entryWidth = legendSignSize + opts.labelPadding + toInt(legend.labelRect.getWidth());
            legend.entryWidthPadded = legend.entryWidth + opts.labelPadding;

            switch (opts.legend) {
                case TOP: case BOTTOM:
                    legend.entryCount = (int) Math.floor((double) (FormulaBorderRect.width - opts.labelPadding) / legend.entryWidthPadded);
                    legend.xCount = size <= legend.entryCount ? size : legend.entryCount;
                    legend.yCount = size <= legend.entryCount ? 1 : (int) Math.ceil((double) size / legend.entryCount);
                    legend.rect.width = opts.labelPadding + (legend.xCount * legend.entryWidthPadded);
                    legend.rect.height = opts.labelPadding + toInt(legend.yCount * (opts.labelPadding + legend.labelRect.getHeight()));
                    legend.rect.x = FormulaBorderRect.x + (FormulaBorderRect.width - legend.rect.width) / 2;
                    if (opts.legend == Formula.LegendFormat.TOP)
                        legend.rect.y = FormulaBorderRect.y;
                    else
                        legend.rect.y = boundRect.height - legend.rect.height - opts.padding;
                    break;
                case RIGHT:
                    legend.rect.width = opts.labelPadding * 3 + legendSignSize + toInt(legend.labelRect.getWidth());
                    legend.rect.height = opts.labelPadding * (size + 1) + toInt(legend.labelRect.getHeight() * size);
                    legend.rect.x = boundRect.width - legend.rect.width - opts.padding;
                    legend.rect.y = FormulaBorderRect.y + FormulaBorderRect.height / 2 - legend.rect.height / 2;
                    break;
                default:
            }
        }

        private void drawLegend(Graphics2D g) {
            if (opts.legend == Formula.LegendFormat.NONE)
                return;

            g.drawRect(legend.rect.x, legend.rect.y, legend.rect.width, legend.rect.height);
            int labelHeight = toInt(legend.labelRect.getHeight());
            int x = legend.rect.x + opts.labelPadding;
            int y = legend.rect.y + opts.labelPadding + labelHeight / 2;

            switch (opts.legend) {
                case TOP: case BOTTOM:
                    int i = 0;
                    for (Formula.DataSeries series : dataSeriesMap.values()) {
                        drawLegendEntry(g, series, x, y);
                        x += legend.entryWidthPadded;
                        if ((i + 1) % legend.xCount == 0) {
                            x = legend.rect.x + opts.labelPadding;
                            y += opts.labelPadding + labelHeight;
                        }
                        i++;
                    }
                    break;
                case RIGHT:
                    for (Formula.DataSeries series : dataSeriesMap.values()) {
                        drawLegendEntry(g, series, x, y);
                        y += opts.labelPadding + labelHeight;
                    }
                    break;
                default:
            }
        }

        private void drawLegendEntry(Graphics2D g, Formula.DataSeries series, int x, int y) {
            series.fillArea(g, x, y, x + opts.legendSignSize, y, y + opts.legendSignSize / 2);
            series.drawLine(g, x, y, x + opts.legendSignSize, y);
            series.drawMarker(g, x + opts.legendSignSize / 2, y, x, y + opts.legendSignSize / 2);
            g.setColor(opts.foregroundColor);
            drawLabel(g, series.nameWithAxes, x + opts.legendSignSize + opts.labelPadding, y, Formula.HorizAlign.LEFT, Formula.VertAlign.CENTER);
        }

    }

    public static class Range {

        private double min;
        private double max;
        private double diff;

        public Range(double min, double max) {
            this.min = min;
            this.max = max;
            this.diff = max - min;
        }

        public Range(Formula.Range range) {
            this.min = range.min;
            this.max = range.max;
            this.diff = max - min;
        }

        public void setMin(double min) {
            this.min = min;
            this.diff = max - min;
        }

        public void setMax(double max) {
            this.max = max;
            this.diff = max - min;
        }

        @Override
        public String toString() {
            return "Range [min=" + min + ", max=" + max + "]";
        }

    }

    public static Formula.AxisOptions axisOpts() {
        return new Formula.AxisOptions();
    }

    public static class AxisOptions {

        private Formula.AxisFormat format = Formula.AxisFormat.NUMBER;
        private boolean dynamicRange = true;
        private Formula.Range range;

        public Formula.AxisOptions format(Formula.AxisFormat format) {
            this.format = format;
            return this;
        }

        public Formula.AxisOptions range(double min, double max) {
            this.range = new Formula.Range(min, max);
            this.dynamicRange = false;
            return this;
        }

    }

    private class Axis {

        private String name;
        private Formula.AxisOptions opts = new Formula.AxisOptions();
        private Rectangle2D labelRect;
        private String[] labels;

        public Axis(String name, Formula.AxisOptions opts) {
            this.name = name;
            if (opts != null)
                this.opts = opts;
        }

        @Override
        public String toString() {
            return "Axis [name=" + name + ", opts=" + opts + "]";
        }

    }

    public static Formula.DataSeriesOptions seriesOpts() {
        return new Formula.DataSeriesOptions();
    }

    public static class DataSeriesOptions {

        private Color seriesColor = Color.BLUE;
        private Formula.Line line = Formula.Line.SOLID;
        private int lineWidth = 2;
        private float[] lineDash = new float[] { 3.0f, 3.0f };
        private Formula.Marker marker = Formula.Marker.NONE;
        private int markerSize = 10;
        private Color markerColor = Color.WHITE;
        private Color areaColor = null;
        private String xAxisName;
        private String yAxisName;
        private Formula.Axis xAxis;
        private Formula.Axis yAxis;

        public Formula.DataSeriesOptions color(Color seriesColor) {
            this.seriesColor = seriesColor;
            return this;
        }

        public Formula.DataSeriesOptions line(Formula.Line line) {
            this.line = line;
            return this;
        }

        public Formula.DataSeriesOptions lineWidth(int width) {
            this.lineWidth = width;
            return this;
        }

        public Formula.DataSeriesOptions lineDash(float[] dash) {
            this.lineDash = dash;
            return this;
        }

        public Formula.DataSeriesOptions marker(Formula.Marker marker) {
            this.marker = marker;
            return this;
        }

        public Formula.DataSeriesOptions markerSize(int markerSize) {
            this.markerSize = markerSize;
            return this;
        }

        public Formula.DataSeriesOptions markerColor(Color color) {
            this.markerColor = color;
            return this;
        }

        public Formula.DataSeriesOptions areaColor(Color color) {
            this.areaColor = color;
            return this;
        }

        public Formula.DataSeriesOptions xAxis(String name) {
            this.xAxisName = name;
            return this;
        }

        public Formula.DataSeriesOptions yAxis(String name) {
            this.yAxisName = name;
            return this;
        }

        private void setFormula(Formula Formula) {
            if (Formula != null)
                this.xAxis = Formula.xAxes.get(xAxisName);
            if (Formula != null)
                this.yAxis = Formula.yAxes.get(yAxisName);
        }

    }

    public static Formula.Data data() {
        return new Formula.Data();
    }

    public static class Data {

        private double[] x1;
        private double[] y1;
        private List<Double> x2;
        private List<Double> y2;

        private Data() {}

        public Formula.Data xy(double[] x, double[] y) {
            this.x1 = x;
            this.y1 = y;
            return this;
        }

        public Formula.Data xy(double x, double y) {
            if (this.x2 == null || this.y2 == null) {
                this.x2 = new ArrayList<Double>(10);
                this.y2 = new ArrayList<Double>(10);
            }
            x2.add(x);
            y2.add(y);
            return this;
        }

        public Formula.Data xy(List<Double> x, List<Double> y) {
            this.x2 = x;
            this.y2 = y;
            return this;
        }

        public int size() {
            if (x1 != null)
                return x1.length;
            if (x2 != null)
                return x2.size();
            return 0;
        }

        public double x(int i) {
            if (x1 != null)
                return x1[i];
            if (x2 != null)
                return x2.get(i);
            return 0;
        }

        public double y(int i) {
            if (y1 != null)
                return y1[i];
            if (y2 != null)
                return y2.get(i);
            return 0;
        }

    }

    public class DataSeries {

        private String name;
        private String nameWithAxes;
        private Formula.DataSeriesOptions opts = new Formula.DataSeriesOptions();
        private Formula.Data data;

        public DataSeries(String name, Formula.Data data, Formula.DataSeriesOptions opts) {
            if (opts != null)
                this.opts = opts;
            this.name = name;
            this.data = data;
            if (this.data == null)
                this.data = data();
        }

        public void clear() {
        }

        private void addAxesToName() {
            this.nameWithAxes = this.name + " (" + opts.yAxis.name +	"/" + opts.xAxis.name + ")";
        }

        private Formula.Range xRange() {
            Formula.Range range = new Formula.Range(0, 0);
            if (data != null && data.size() > 0) {
                range = new Formula.Range(data.x(0), data.x(0));
                for (int i = 1; i < data.size(); i++) {
                    if (data.x(i) > range.max)
                        range.setMax(data.x(i));
                    if (data.x(i) < range.min)
                        range.setMin(data.x(i));
                }
            }
            return range;
        }

        private Formula.Range yRange() {
            Formula.Range range = new Formula.Range(0, 0);
            if (data != null && data.size() > 0) {
                range = new Formula.Range(data.y(0), data.y(0));
                for (int i = 1; i < data.size(); i++) {
                    if (data.y(i) > range.max)
                        range.setMax(data.y(i));
                    if (data.y(i) < range.min)
                        range.setMin(data.y(i));
                }
            }
            return range;
        }

        private void draw(Graphics2D g) {
            g.setClip(formulaArea.FormulaClipRect);
            if (data != null) {
                double x1 = 0, y1 = 0;
                int size = data.size();
                if (opts.line != Formula.Line.NONE)
                    for (int j = 0; j < size; j++) {
                        double x2 = x2x(data.x(j), opts.xAxis.opts.range, formulaArea.xFormulaRange);
                        double y2 = y2y(data.y(j), opts.yAxis.opts.range, formulaArea.yFormulaRange);
                        int ix1 = toInt(x1), iy1 = toInt(y1), ix2 = toInt(x2), iy2 = toInt(y2);
                        int iy3 = formulaArea.FormulaRect.y + formulaArea.FormulaRect.height;
                        // special case for the case when only the first point present
                        if (size == 1) {
                            ix1 = ix2;
                            iy1 = iy2;
                        }
                        if (j != 0 || size == 1) {
                            fillArea(g, ix1, iy1, ix2, iy2, iy3);
                            drawLine(g, ix1, iy1, ix2, iy2);
                        }
                        x1 = x2;
                        y1 = y2;
                    }

                int halfMarkerSize = opts.markerSize / 2;
                int halfDiagMarkerSize = getDiagMarkerSize() / 2;
                g.setStroke(new BasicStroke(2));
                if (opts.marker != Formula.Marker.NONE)
                    for (int j = 0; j < size; j++) {
                        double x2 = x2x(data.x(j), opts.xAxis.opts.range, formulaArea.xFormulaRange);
                        double y2 = y2y(data.y(j), opts.yAxis.opts.range, formulaArea.yFormulaRange);
                        drawMarker(g, halfMarkerSize, halfDiagMarkerSize, x2, y2,
                                formulaArea.FormulaRect.x, formulaArea.FormulaRect.y + formulaArea.FormulaRect.height);
                    }
            }
        }

        private int getDiagMarkerSize() {
            return (int) Math.round(Math.sqrt(2 * opts.markerSize * opts.markerSize));
        }

        private void fillArea(Graphics2D g, int ix1, int iy1, int ix2, int iy2, int iy3) {
            if (opts.areaColor != null) {
                g.setColor(opts.areaColor);
                g.fill(new Polygon(
                        new int[] { ix1, ix2, ix2, ix1 },
                        new int[] { iy1, iy2, iy3, iy3 },
                        4));
                g.setColor(opts.seriesColor);
            }
        }

        private void drawLine(Graphics2D g, int ix1, int iy1, int ix2, int iy2) {
            if (opts.line != Formula.Line.NONE) {
                g.setColor(opts.seriesColor);
                setStroke(g);
                g.drawLine(ix1, iy1, ix2, iy2);
            }
        }

        private void setStroke(Graphics2D g) {
            switch (opts.line) {
                case SOLID:
                    g.setStroke(new BasicStroke(opts.lineWidth));
                    break;
                case DASHED:
                    g.setStroke(new BasicStroke(opts.lineWidth, BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND, 10.0f, opts.lineDash, 0.0f));
                    break;
                default:
            }
        }

        private void drawMarker(Graphics2D g, int x2, int y2, int x3, int y3) {
            int halfMarkerSize = opts.markerSize / 2;
            int halfDiagMarkerSize =  getDiagMarkerSize() / 2;
            g.setStroke(new BasicStroke(2));
            drawMarker(g, halfMarkerSize, halfDiagMarkerSize, x2, y2, x3, y3);
        }

        private void drawMarker(Graphics2D g, int halfMarkerSize, int halfDiagMarkerSize, double x2, double y2, double x3, double y3) {
            switch (opts.marker) {
                case CIRCLE:
                    g.setColor(opts.markerColor);
                    g.fillOval(toInt(x2 - halfMarkerSize), toInt(y2 - halfMarkerSize), opts.markerSize, opts.markerSize);
                    g.setColor(opts.seriesColor);
                    g.drawOval(toInt(x2 - halfMarkerSize), toInt(y2 - halfMarkerSize), opts.markerSize, opts.markerSize);
                    break;
                case SQUARE:
                    g.setColor(opts.markerColor);
                    g.fillRect(toInt(x2 - halfMarkerSize), toInt(y2 - halfMarkerSize), opts.markerSize, opts.markerSize);
                    g.setColor(opts.seriesColor);
                    g.drawRect(toInt(x2 - halfMarkerSize), toInt(y2 - halfMarkerSize), opts.markerSize, opts.markerSize);
                    break;
                case DIAMOND:
                    int[] xpts = { toInt(x2), toInt(x2 + halfDiagMarkerSize), toInt(x2), toInt(x2 - halfDiagMarkerSize) };
                    int[] ypts = { toInt(y2 - halfDiagMarkerSize), toInt(y2), toInt(y2 + halfDiagMarkerSize), toInt(y2) };
                    g.setColor(opts.markerColor);
                    g.fillPolygon(xpts, ypts, 4);
                    g.setColor(opts.seriesColor);
                    g.drawPolygon(xpts, ypts, 4);
                    break;
                case COLUMN:
                    g.setColor(opts.markerColor);
                    g.fillRect(toInt(x2), toInt(y2), opts.markerSize, toInt(y3 - y2));
                    g.setColor(opts.seriesColor);
                    g.drawRect(toInt(x2), toInt(y2), opts.markerSize, toInt(y3 - y2));
                    break;
                case BAR:
                    g.setColor(opts.markerColor);
                    g.fillRect(toInt(x3), toInt(y2), toInt(x2 - x3), opts.markerSize);
                    g.setColor(opts.seriesColor);
                    g.drawRect(toInt(x3), toInt(y2), toInt(x2 - x3), opts.markerSize);
                    break;
                default:
            }
        }

    }

    private static void drawLabel(Graphics2D g, String s, int x, int y, Formula.HorizAlign hAlign, Formula.VertAlign vAlign) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(s, g);

        // by default align by left
        if (hAlign == Formula.HorizAlign.RIGHT)
            x -= rect.getWidth();
        else if (hAlign == Formula.HorizAlign.CENTER)
            x -= rect.getWidth() / 2;

        // by default align by bottom
        if (vAlign == Formula.VertAlign.TOP)
            y += rect.getHeight();
        else if (vAlign == Formula.VertAlign.CENTER)
            y += rect.getHeight() / 2;

        g.drawString(s, x, y);
    }

    public static String formatDouble(double d, Formula.AxisFormat format) {
        switch (format) {
            case TIME_HM: return String.format("%tR", new java.util.Date((long) d));
            case TIME_HMS: return String.format("%tT", new java.util.Date((long) d));
            case DATE: return String.format("%tF", new java.util.Date((long) d));
            case DATETIME_HM: return String.format("%tF %1$tR", new java.util.Date((long) d));
            case DATETIME_HMS: return String.format("%tF %1$tT", new java.util.Date((long) d));
            case NUMBER_KGM: return formatDoubleAsNumber(d, true);
            case NUMBER_INT: return Integer.toString((int) d);
            default: return formatDoubleAsNumber(d, false);
        }
    }

    private static String formatDoubleAsNumber(double d, boolean useKGM) {
        if (useKGM && d > 1000 && d < 1000000000000l) {
            long[] numbers = new long[] { 1000l, 1000000l, 1000000000l };
            char[] suffix = new char[] { 'K', 'M', 'G' };

            int i = 0;
            double r = 0;
            for (long number : numbers) {
                r = d / number;
                if (r < 1000)
                    break;
                i++;
            }
            if (i == suffix.length)
                i--;
            return String.format("%1$,.2f%2$c", r, suffix[i]);
        }
        else
            return String.format("%1$.3G", d);
    }

    private static double x2x(double x, Formula.Range xr1, Formula.Range xr2) {
        return xr1.diff == 0 ? xr2.min + xr2.diff / 2 : xr2.min + (x - xr1.min) / xr1.diff * xr2.diff;
    }

    // y axis is reverse in Graphics
    private static double y2y(double x, Formula.Range xr1, Formula.Range xr2) {
        return xr1.diff == 0 ? xr2.min + xr2.diff / 2 : xr2.max - (x - xr1.min) / xr1.diff * xr2.diff;
    }

    private static int toInt(double d) {
        return (int) Math.round(d);
    }

}

