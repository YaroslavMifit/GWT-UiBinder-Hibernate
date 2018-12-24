package com.example.filedemo.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.example.filedemo.model.UserData;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDataReport {

    private final Collection<UserData> list;

    public UserDataReport(Collection<UserData> c) {
        list = new ArrayList<>(c);
    }

    public JasperPrint getReport(List<Long> userDataTitle) throws ColumnBuilderException, JRException, ClassNotFoundException {
        Style headerStyle = createHeaderStyle();
        Style detailTextStyle = createDetailTextStyle();
        Style detailNumberStyle = createDetailNumberStyle();

        DynamicReport dynaReport = getReport(headerStyle, detailTextStyle, detailNumberStyle, userDataTitle);
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(),
                new JRBeanCollectionDataSource(list));
        return jp;
    }

    private Style createHeaderStyle() {
        return new StyleBuilder(true)
                .setFont(new Font(14, "Arial", true))
                .setBorder(Border.DOTTED())
                .setBorderBottom(Border.PEN_2_POINT())
                .setBorderColor(Color.BLACK)
                .setBackgroundColor(Color.ORANGE)
                .setTextColor(Color.BLACK)
                .setHorizontalAlign(HorizontalAlign.CENTER)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setTransparency(Transparency.OPAQUE)
                .setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT)
                .setPaddingTop(15)
                .setPaddingBottom(15)
                .build();
    }

    private Style createDetailTextStyle() {
        return new StyleBuilder(true)
                .setFont(new Font(12, "Arial", false))
                .setBorder(Border.DOTTED())
                .setBorderColor(Color.BLACK)
                .setTextColor(Color.BLACK)
                .setHorizontalAlign(HorizontalAlign.RIGHT)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT)
                .setPaddingTop(15)
                .setPaddingBottom(15)
                .build();
    }

    private Style createDetailNumberStyle() {
        return new StyleBuilder(true)
                .setFont(Font.VERDANA_MEDIUM)
                .setBorder(Border.DOTTED())
                .setBorderColor(Color.BLACK)
                .setTextColor(Color.BLACK)
                .setHorizontalAlign(HorizontalAlign.RIGHT)
                .setVerticalAlign(VerticalAlign.MIDDLE)
                .setPaddingRight(5)
                .setPattern("#,##0.00")
                .build();
    }

    private AbstractColumn createColumn(String property, Class<?> type, String title, int width, Style headerStyle, Style detailStyle)
            throws ColumnBuilderException {
        return ColumnBuilder.getNew()
                .setColumnProperty(property, type.getName())
                .setTitle(title)
                .setWidth(Integer.valueOf(width))
                .setStyle(detailStyle)
                .setHeaderStyle(headerStyle)
                .build();
    }

    private DynamicReport getReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle, List<Long> userDataTitle)
            throws ColumnBuilderException, ClassNotFoundException {

        DynamicReportBuilder report = new DynamicReportBuilder();
        if (userDataTitle.contains(0L)) {
            AbstractColumn creditOrganizationName = createColumn("creditOrganization.creditOrganizationName", String.class, "Наименование кредитной организации", 200, headerStyle, detailTextStyle);
            report.addColumn(creditOrganizationName);
        }
        if (userDataTitle.contains(1L)) {
            AbstractColumn scoreName = createColumn("score.scoreName", String.class, "Наименование счета второго порядка", 200, headerStyle, detailTextStyle);
            report.addColumn(scoreName);
        }
        if (userDataTitle.contains(2L)) {
            AbstractColumn incomingBalancesInRubles = createColumn("incomingBalancesInRubles", String.class, "Входящие остатки «в рублях», тыс. руб", 200, headerStyle, detailTextStyle);
            report.addColumn(incomingBalancesInRubles);
        }
        if (userDataTitle.contains(3L)) {
            AbstractColumn incomingBalancesDragMetals = createColumn("incomingBalancesDragMetals", String.class, "Входящие остатки «ин. вал., драг. металлы», тыс. руб", 200, headerStyle, detailTextStyle);
            report.addColumn(incomingBalancesDragMetals);
        }
        if (userDataTitle.contains(4L)) {
            AbstractColumn incomingBalancesOfTotal = createColumn("incomingBalancesOfTotal", String.class, "Входящие остатки «итого», тыс. руб.; счета Депо – в штуках", 200, headerStyle, detailTextStyle);
            report.addColumn(incomingBalancesOfTotal);
        }
        if (userDataTitle.contains(5L)) {
            AbstractColumn turnoversDebitInRubles = createColumn("turnoversDebitInRubles", String.class, "Обороты за отчетный период по дебету (активу) «в рублях», тыс. руб", 200, headerStyle, detailTextStyle);
            report.addColumn(turnoversDebitInRubles);
        }
        if (userDataTitle.contains(6L)) {
            AbstractColumn turnoversDebitDragMetals = createColumn("turnoversDebitDragMetals", String.class, "Обороты за отчетный период по дебету (активу) «ин. вал., драг. металлы», тыс. руб", 200, headerStyle, detailTextStyle);
            report.addColumn(turnoversDebitDragMetals);
        }
        if (userDataTitle.contains(7L)) {
            AbstractColumn turnoversDebitOfTotal = createColumn("turnoversDebitOfTotal", String.class, "Обороты за отчетный период по дебету (активу) «итого», тыс. руб.; счета Депо – в штуках", 200, headerStyle, detailTextStyle);
            report.addColumn(turnoversDebitOfTotal);
        }
        if (userDataTitle.contains(8L)) {
            AbstractColumn turnoversCreditInRubles = createColumn("turnoversCreditInRubles", String.class, "Обороты за отчетный период по кредиту (пассиву) «в рублях», тыс. руб.", 200, headerStyle, detailTextStyle);
            report.addColumn(turnoversCreditInRubles);
        }
        if (userDataTitle.contains(9L)) {
            AbstractColumn turnoversCreditDragMetals = createColumn("turnoversCreditDragMetals", String.class, "Обороты за отчетный период по кредиту (пассиву) «ин. вал., драг. металлы», тыс. руб.", 200, headerStyle, detailTextStyle);
            report.addColumn(turnoversCreditDragMetals);
        }
        if (userDataTitle.contains(10L)) {
            AbstractColumn turnoversCreditOfTotal = createColumn("turnoversCreditOfTotal", String.class, "Обороты за отчетный период по кредиту (пассиву) «итого», тыс. руб.;", 200, headerStyle, detailTextStyle);
            report.addColumn(turnoversCreditOfTotal);
        }
        if (userDataTitle.contains(11L)) {
            AbstractColumn outgoingBalancesInRubles = createColumn("outgoingBalancesInRubles", String.class, "Исходящие остатки «в рублях», тыс. руб.", 200, headerStyle, detailTextStyle);
            report.addColumn(outgoingBalancesInRubles);
        }
        if (userDataTitle.contains(12L)) {
            AbstractColumn outgoingBalancesDragMetals = createColumn("outgoingBalancesDragMetals", String.class, "Исходящие остатки «ин. вал., драг. металлы», тыс. руб.", 200, headerStyle, detailTextStyle);
            report.addColumn(outgoingBalancesDragMetals);
        }
        if (userDataTitle.contains(13L)) {
            AbstractColumn outgoingBalancesOfTotal = createColumn("outgoingBalancesOfTotal", String.class, "Исходящие остатки «итого», тыс. руб.;", 200, headerStyle, detailTextStyle);
            report.addColumn(outgoingBalancesOfTotal);
        }

        StyleBuilder titleStyle = new StyleBuilder(true);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setFont(new Font(20, "Arial", true));
        titleStyle.setStretchWithOverflow(false);

     /*   StyleBuilder subTitleStyle = new StyleBuilder(true);
        subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        subTitleStyle.setFont(new Font(Font.MEDIUM, "Arial", true));
        subTitleStyle.setStretchWithOverflow(false);*/

        report.setTitle("Пользовательские данные");
        report.setTitleStyle(titleStyle.build());
       // report.setSubtitleStyle(subTitleStyle.build());
        return report.build();
    }
}
