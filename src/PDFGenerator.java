import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PDFGenerator {
    public static void generateInvoice(Order order, Customer customer) throws IOException {
        float x=0,y=0;
        ArrayList<Item> items = order.getItems();
        int invNum = order.getInvoice();
        PDDocument doc = PDDocument.load(new File("itemplate.pdf"));
        doc.save("Invoice_"+invNum+".pdf");
        doc.close();
        doc = PDDocument.load(new File("Invoice_"+invNum+".pdf"));
        PDPage page = doc.getPage(0);
        PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND,false,true);
        cs.beginText();
        cs.newLineAtOffset(180, 703);
        x+= 180;
        y+= 703;
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.showText(""+invNum);
        cs.newLineAtOffset(-105, -73);
        x -= 105; y -= 73;
        for(Item item : items) {
            cs.showText("" +item.getSKU());
            cs.newLineAtOffset(73,0);
            cs.showText("" + item.getStock());
            cs.newLineAtOffset(60, 0);
            cs.showText(item.getName());
            cs.newLineAtOffset(251, 0);
            cs.showText("$ "+item.getPrice());
            cs.newLineAtOffset(-384, -24);
            y -= 24;
        }
        cs.newLineAtOffset(-x,-y);
        cs.newLineAtOffset(465,185);
        cs.showText("$ "+order.getTotal());
        cs.newLineAtOffset(0,-24);
        cs.showText(((order.getTax() - 1)*100)+"%");
        cs.newLineAtOffset(0,-24);
        cs.showText("$ "+order.getShippingCost());
        cs.newLineAtOffset(0,-24);
        cs.showText("$ "+(order.getTotal()+order.getShippingCost()+((order.getTax() - 1)*100)));
        cs.endText();
        cs.close();

        doc.save("Invoice_"+invNum+".pdf");
        doc.close();
        generateShippingLabel(invNum, customer, order);
    }
    public static void generateShippingLabel(int invNum, Customer customer, Order order) throws IOException {
        PDDocument doc = PDDocument.load(new File("stemplate.pdf"));
        doc.save("Shipping_"+invNum+".pdf");
        doc.close();
        doc = PDDocument.load(new File("Shipping_"+invNum+".pdf"));
        PDPage page = doc.getPage(0);
        PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND,false,true);
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.beginText();
        cs.newLineAtOffset(72,667);
        cs.showText(customer.getLastName() + ", " + customer.getFirstName());
        cs.newLineAtOffset(0,-12);
        cs.showText(customer.getAddress().replaceAll("\\Q+\\E", " "));
        cs.newLineAtOffset(55,-178);
        cs.showText(""+order.getCarrier());
        cs.endText();
        cs.close();
        doc.save("Shipping_"+invNum+".pdf");
        doc.close();
    }
}
