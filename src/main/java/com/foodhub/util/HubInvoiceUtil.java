package com.foodhub.util;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.foodhub.constants.HubConstants;
import com.foodhub.entity.Order;
import com.foodhub.entity.OrderItem;
import com.foodhub.exception.InvoiceGenerationException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class HubInvoiceUtil implements HubConstants {

    Logger logger = LoggerFactory.getLogger(HubInvoiceUtil.class.getName());

    @Autowired
    HubUtil hubUtil;

    public ByteArrayInputStream generatePdfStream(Order order){
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            Font headFont = createFont(FontFactory.COURIER_BOLD, BaseColor.BLUE, 15.0f);
            Font subHeadFont = createFont(FontFactory.COURIER_BOLD, BaseColor.BLACK, 13.0f);
            Font itemHeadFont = createFont(FontFactory.COURIER_BOLD, BaseColor.BLUE, 13.0f);
            Font priceFont = createFont(FontFactory.COURIER_BOLD, BaseColor.BLUE, 12.0f);

            PdfPTable headingTable = createHeading(order, headFont, subHeadFont);
            PdfPTable bodyTable = createBody(order, itemHeadFont, priceFont);
            PdfPTable footerTable = createFooter(headFont);

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headingTable);
            document.add(bodyTable);
            document.add(footerTable);
            document.close();

        }catch(DocumentException exception){
            logger.error(" [ "+ order.getOrderId() +" ] Pdf generation failed : "+ exception.getMessage());
            throw new InvoiceGenerationException(hubUtil.readMessage("hub.order.invoice.failure"));
        }
        logger.debug("Pdf generated for order "+ order.getOrderId());
        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPTable createHeading(Order order, Font headFont, Font subHeadFont)
            throws DocumentException {
        PdfPTable headingTable = createPdfTable(1, 70, new int[]{1});
        headingTable.addCell(createPdfTableCell(hubUtil.readMessage(DEFAULT_TITLE),
                headFont, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, Rectangle.BOX));
        headingTable.addCell(createPdfTableCell(order.getRestaurant().getRestaurantAddress(),
                subHeadFont, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, Rectangle.BOX));
        headingTable.addCell(createPdfTableCell(EMPTY_STRING,
                subHeadFont, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, Rectangle.BOX));
        return headingTable;
    }

    private PdfPTable createFooter(Font headFont)
            throws DocumentException {
        PdfPTable footerTable = createPdfTable(1, 70, new int[]{1});
        footerTable.addCell(createPdfTableCell(EMPTY_STRING,
                null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, Rectangle.BOX));
        footerTable.addCell(createPdfTableCell(hubUtil.readMessage(FOOTER_TEXT),
                headFont, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, Rectangle.BOX));
        return footerTable;
    }

    private PdfPTable createBody(Order order, Font headFont, Font priceFont)
            throws DocumentException {

        PdfPTable bodyTable = createPdfTable(3, 70, new int[]{6, 1, 3});
        bodyTable.addCell(createPdfTableCell(TABLE_HEAD_ITEMS, headFont, Element.ALIGN_LEFT,
                NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(TABLE_HEAD_QTY, headFont, Element.ALIGN_CENTER,
                NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(TABLE_HEAD_PRICE, headFont, Element.ALIGN_RIGHT,
                NEGATIVE_ONE, Rectangle.BOX));

        for(OrderItem item: order.getOrderItemsList()){
            bodyTable.addCell(createPdfTableCell(item.getItem().getName(), null, Element.ALIGN_LEFT,
                    Element.ALIGN_MIDDLE, Rectangle.BOX));
            bodyTable.addCell(createPdfTableCell(String.valueOf(item.getQty()), null, Element.ALIGN_CENTER,
                    Element.ALIGN_MIDDLE, Rectangle.BOX));
            bodyTable.addCell(createPdfTableCell(hubUtil.getItemPrice(item), null, Element.ALIGN_RIGHT,
                    Element.ALIGN_MIDDLE, Rectangle.BOX));
        }
        bodyTable.addCell(createPdfTableCell(EMPTY_STRING, null, Element.ALIGN_RIGHT,
                NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(EMPTY_STRING, null, Element.ALIGN_RIGHT,
                NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(EMPTY_STRING, null, Element.ALIGN_RIGHT,
                NEGATIVE_ONE, Rectangle.BOX));

        bodyTable.addCell(createPdfTableCell(TABLE_SUB_TOTAL,
                null, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(EMPTY_STRING,
                null, Element.ALIGN_CENTER, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(order.getItemTotal().toString(),
                priceFont, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));

        bodyTable.addCell(createPdfTableCell(TABLE_TAX_AMNT,
                null, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(EMPTY_STRING,
                null, Element.ALIGN_CENTER, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(order.getTax().toString(),
                priceFont, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));

        bodyTable.addCell(createPdfTableCell(TABLE_DELIVERY_CHARGE,
                null, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(EMPTY_STRING,
                null, Element.ALIGN_CENTER, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(order.getDeliveryCharge().toString(),
                priceFont, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));

        bodyTable.addCell(createPdfTableCell(TABLE_TOTAL_AMNT,
                null, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(EMPTY_STRING,
                null, Element.ALIGN_CENTER, NEGATIVE_ONE, Rectangle.BOX));
        bodyTable.addCell(createPdfTableCell(order.getTotal().toString(),
                priceFont, Element.ALIGN_RIGHT, NEGATIVE_ONE, Rectangle.BOX));

        return bodyTable;
    }

    private PdfPCell createPdfTableCell(String value, Font font,
                                        Integer horizontalAlignment,
                                        Integer verticalAlignment,
                                        Integer disableBorderSize){
        PdfPCell cell = new PdfPCell(font==null? new Phrase(value): new Phrase(value, font));
        if(horizontalAlignment != -1) cell.setHorizontalAlignment(horizontalAlignment);
        if(verticalAlignment != -1)  cell.setVerticalAlignment(verticalAlignment);
        if(disableBorderSize != -1) cell.disableBorderSide(disableBorderSize);
        return cell;
    }

    private PdfPTable createPdfTable(Integer numColumns, Integer width, int[] columns)
            throws DocumentException {
        PdfPTable hTable = new PdfPTable(numColumns);
        hTable.setWidthPercentage(width);
        hTable.setWidths(columns);
        return hTable;
    }

    private Font createFont(String fontFamily, BaseColor color, float fontSize){
        Font font = FontFactory.getFont(fontFamily);
        font.setColor(color);
        font.setSize(fontSize);
        return font;
    }
}
