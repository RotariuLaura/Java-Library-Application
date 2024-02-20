package controller;

import model.Book;
import model.Order;
import model.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import repository.user.UserRepository;
import service.book.BookService;

import java.io.IOException;
import java.util.List;

public class PDFReportGenerator {

    public void generateEmployeeBookReport(List<Order> orders, BookService bookService) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

            contentStream.beginText();
            contentStream.newLineAtOffset(250, 700);
            contentStream.showText("Employee Book Sales Report");
            contentStream.newLineAtOffset(0, -20);
            contentStream.newLineAtOffset(0, -20);

            for (Order order : orders) {
                Book book = bookService.findById(order.getBookId());

                String title = "Title: " + book.getTitle();
                String author = "Author: " + book.getAuthor();
                String orderDate = "Order Date: " + order.getOrderDate().toString();
                String quantity = "Quantity: " + order.getQuantity();

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(title);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(author);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(orderDate);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(quantity);
                contentStream.newLineAtOffset(0, -20);
            }

            contentStream.endText();
            contentStream.close();

            document.save("EmployeeBookSalesReport.pdf");
        }
    }

    public void generateEmployeesActivityReport(List<Order> orders, BookService bookService, UserRepository userRepository) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

            contentStream.beginText();
            contentStream.newLineAtOffset(250, 700);
            contentStream.showText("Employees Activity Report");
            contentStream.newLineAtOffset(0, -20);
            contentStream.newLineAtOffset(-200, -20);

            for (Order order : orders) {
                Book book = bookService.findById(order.getBookId());
                User employee = userRepository.findById(order.getEmployeeId());
                User customer = userRepository.findById(order.getCustomerId());

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.format("Employee %s sold %s copies of book %s to customer",
                        employee.getUsername(), order.getQuantity(), book.getTitle()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.format("%s on %s.", customer.getUsername(), order.getOrderDate()));
            }

            contentStream.endText();
            contentStream.close();

            document.save("EmployeesActivityReport.pdf");
        }
    }
}






