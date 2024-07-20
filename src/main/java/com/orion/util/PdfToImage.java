//package com.orion.util;
//
//import lombok.extern.log4j.Log4j2;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.rendering.ImageType;
//import org.apache.pdfbox.rendering.PDFRenderer;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Log4j2
//@Service
//public class PdfToImage {
//
//    public List<MultipartFile> convert(byte[] pdf) {
//
//        log.info("Converting pdf to image...");
//
//        List<MultipartFile> images = new ArrayList<>();
//
//        try {
//
//            PDDocument document = PDDocument.load(pdf);
//            PDFRenderer pdfRenderer = new PDFRenderer(document);
//
//            int numberOfPages = document.getNumberOfPages();
//            log.info("Total pages to be converted: {}", numberOfPages);
//
//            int dpi = 125;
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            for (int i = 0; i < numberOfPages; i++) {
//                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
//                ImageIO.write(bufferedImage, "png", baos);
//                baos.flush();
//                MultipartFile multipartFile = new MockMultipartFile("image" + i, baos.toByteArray());
//                images.add(multipartFile);
//                baos.reset();
//                log.info("Converted page: {}", i);
//            }
//            document.close();
//
//        } catch (Exception e) {
//            log.error("Error during pdf to image conversion!");
//            e.printStackTrace();
//        }
//
//        return images;
//    }
//}