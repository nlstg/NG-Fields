package tg.ngstars.ng_fields_api.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tg.ngstars.ng_fields_api.model.Intervention;
import tg.ngstars.ng_fields_api.model.InterventionPhoto;
import tg.ngstars.ng_fields_api.repository.InterventionPhotoRepository;
import tg.ngstars.ng_fields_api.repository.InterventionRepository;

import java.awt.Color;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);
    private static final DateTimeFormatter DATE_FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.of("Africa/Lome"));

    private static final Color COLOR_PRIMARY   = new Color(0x1A, 0x56, 0xDB);
    private static final Color COLOR_SECONDARY = new Color(0x23, 0x2B, 0x3E);
    private static final Color COLOR_LIGHT     = new Color(0xF3, 0xF4, 0xF6);

    private final InterventionRepository interventionRepo;
    private final InterventionPhotoRepository photoRepo;
    private final StorageService storageService;

    @Value("${storage.upload-dir:./uploads}")
    private String uploadDir;

    @Value("${storage.base-url:http://localhost:8081}")
    private String baseUrl;

    public PdfService(
            InterventionRepository interventionRepo,
            InterventionPhotoRepository photoRepo,
            StorageService storageService) {
        this.interventionRepo = interventionRepo;
        this.photoRepo        = photoRepo;
        this.storageService   = storageService;
    }

    public byte[] generatePdf(UUID interventionId) throws IOException {
        Intervention inv = interventionRepo.findById(interventionId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Intervention introuvable : " + interventionId));

        List<InterventionPhoto> photos = photoRepo.findByInterventionId(interventionId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 40, 40, 60, 40);

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, baos);
            writer.setPageEvent(new HeaderFooter(inv));
            doc.open();

            addTitle(doc, inv);

            addSectionHeader(doc, "1. INFORMATIONS GÉNÉRALES");
            addRow(doc, "Référence",    inv.getId().toString().substring(0, 8).toUpperCase());
            addRow(doc, "Date",         inv.getDate() != null ? DATE_FMT.format(inv.getDate()) : "—");
            addRow(doc, "Statut",       inv.getStatus() != null ? inv.getStatus().name() : "—");
            addRow(doc, "Type",         orEmpty(inv.getType()));
            addRow(doc, "Technicien",   orEmpty(inv.getTechnicianName()));
            addRow(doc, "Client",       inv.getClient() != null ? inv.getClient().getCompanyName() : "—");
            if (inv.getClient() != null) {
                addRow(doc, "Adresse client", orEmpty(inv.getClient().getAddress()));
                addRow(doc, "Contact site",   orEmpty(inv.getClient().getContactName()));
            }

            addSectionHeader(doc, "2. HORAIRES");
            addRow(doc, "Sortie société",     orEmpty(inv.getDepartureTime()));
            addRow(doc, "Arrivée client",     orEmpty(inv.getArrivalTime()));
            addRow(doc, "Début intervention", orEmpty(inv.getInterventionStartTime()));
            addRow(doc, "Fin intervention",   orEmpty(inv.getInterventionEndTime()));
            addRow(doc, "Retour société",     orEmpty(inv.getReturnTime()));
            addRow(doc, "Durée sur site",
                inv.getDurationMinutes() != null
                    ? inv.getDurationMinutes() + " minutes"
                    : "Non calculée");

            addSectionHeader(doc, "3. DIAGNOSTIC");
            addRow(doc, "Description du problème", orEmpty(inv.getProblemDesc()));
            addRow(doc, "Ticket OpenProject",      orEmpty(inv.getOpenProjectTicketId()));
            addRow(doc, "Diagnostic",              orEmpty(inv.getDiagnosis()));

            addSectionHeader(doc, "4. ÉQUIPEMENT ET TRAVAUX");
            addRow(doc, "Type équipement",  orEmpty(inv.getEquipmentType()));
            addRow(doc, "Marque / Modèle",
                orEmpty(inv.getEquipmentBrand()) + " " + orEmpty(inv.getEquipmentModel()));
            addRow(doc, "N° série",         orEmpty(inv.getEquipmentSerial()));
            addRow(doc, "Localisation",     orEmpty(inv.getEquipmentLocation()));
            addRow(doc, "Travaux réalisés", orEmpty(inv.getWorkDone()));

            if (!inv.getItems().isEmpty()) {
                addSectionHeader(doc, "5. PIÈCES ET CONSOMMABLES");
                for (var item : inv.getItems()) {
                    addRow(doc, "• " + item.getName(), "Qté : " + item.getQuantity());
                }
            }

            addSectionHeader(doc, "6. RÉSULTAT ET RECOMMANDATIONS");
            addRow(doc, "Résultat",
                inv.getResult() != null ? inv.getResult().name() : "Non renseigné");
            addRow(doc, "Recommandations", orEmpty(inv.getRecommendations()));

            addSectionHeader(doc, "7. FACTURATION");
            addRow(doc, "Facturable",
                Boolean.TRUE.equals(inv.getBillable()) ? "OUI" : "NON");
            addRow(doc, "Notes facturation", orEmpty(inv.getBillingNotes()));

            addPhotos(doc, photos);
            addSignatures(doc, inv);
            addQrCode(doc, writer, inv);

            doc.close();

        } catch (DocumentException e) {
            throw new IOException("Erreur génération PDF : " + e.getMessage(), e);
        }

        String pdfFilename = "rapport-" + interventionId + ".pdf";
        Path pdfDir = Paths.get(uploadDir, "pdfs");
        Files.createDirectories(pdfDir);
        Files.write(pdfDir.resolve(pdfFilename), baos.toByteArray());

        String pdfUrl = baseUrl + "/api/files/pdfs/" + pdfFilename;
        inv.setPdfUrl(pdfUrl);
        interventionRepo.save(inv);

        log.info("PDF généré : {} ({} octets)", pdfFilename, baos.size());
        return baos.toByteArray();
    }

    private void addTitle(Document doc, Intervention inv)
            throws DocumentException {
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.WHITE);
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("RAPPORT D'INTERVENTION\n"
            + "Réf. " + inv.getId().toString().substring(0, 8).toUpperCase(), titleFont));
        cell.setBackgroundColor(COLOR_PRIMARY);
        cell.setPadding(15);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.addCell(cell);
        doc.add(header);
        doc.add(Chunk.NEWLINE);
    }

    private void addSectionHeader(Document doc, String title)
            throws DocumentException {
        doc.add(Chunk.NEWLINE);
        Font f = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        PdfPCell c = new PdfPCell(new Phrase(title, f));
        c.setBackgroundColor(COLOR_SECONDARY);
        c.setPadding(6);
        c.setBorder(Rectangle.NO_BORDER);
        t.addCell(c);
        doc.add(t);
    }

    private void addRow(Document doc, String label, Object value)
            throws DocumentException {
        Font labelFont = new Font(Font.HELVETICA, 9, Font.BOLD,  COLOR_SECONDARY);
        Font valueFont = new Font(Font.HELVETICA, 9, Font.NORMAL, COLOR_SECONDARY);

        PdfPTable t = new PdfPTable(new float[]{35f, 65f});
        t.setWidthPercentage(100);

        PdfPCell lCell = new PdfPCell(new Phrase(label, labelFont));
        lCell.setBackgroundColor(COLOR_LIGHT);
        lCell.setPadding(5);
        lCell.setBorderColor(Color.LIGHT_GRAY);

        PdfPCell vCell = new PdfPCell(
            new Phrase(value != null ? value.toString() : "—", valueFont));
        vCell.setPadding(5);
        vCell.setBorderColor(Color.LIGHT_GRAY);

        t.addCell(lCell);
        t.addCell(vCell);
        doc.add(t);
    }

    private void addPhotos(Document doc, List<InterventionPhoto> photos)
            throws DocumentException, IOException {
        if (photos.isEmpty()) return;

        addSectionHeader(doc, "8. PHOTOS");
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        for (InterventionPhoto photo : photos) {
            try {
                byte[] imgBytes = storageService.loadFromUrl(photo.getUrl());
                Image img = Image.getInstance(imgBytes);
                img.scaleToFit(250, 180);

                Font capFont = new Font(Font.HELVETICA, 8, Font.ITALIC, COLOR_SECONDARY);
                PdfPCell cell = new PdfPCell();
                cell.addElement(img);
                cell.addElement(new Phrase(
                    photo.getType().name() + " — " + (
                        photo.getTakenAt() != null
                            ? DATE_FMT.format(photo.getTakenAt()) : ""),
                    capFont));
                cell.setPadding(5);
                cell.setBorderColor(Color.LIGHT_GRAY);
                table.addCell(cell);
            } catch (Exception e) {
                log.warn("Photo ignorée dans le PDF (chargement impossible) : {}",
                    photo.getUrl());
            }
        }

        if (photos.size() % 2 != 0) {
            PdfPCell empty = new PdfPCell(new Phrase(" "));
            empty.setBorderColor(Color.LIGHT_GRAY);
            table.addCell(empty);
        }

        doc.add(table);
    }

    private void addSignatures(Document doc, Intervention inv)
            throws DocumentException, IOException {
        addSectionHeader(doc, "9. SIGNATURES");
        doc.add(Chunk.NEWLINE);

        PdfPTable sigTable = new PdfPTable(3);
        sigTable.setWidthPercentage(100);

        addSignatureCell(sigTable, "Client",      inv.getSignatureClientUrl());
        addSignatureCell(sigTable, "Technicien",  inv.getSignatureTechnicianUrl());
        addSignatureCell(sigTable, "Responsable", inv.getSignatureManagerUrl());

        doc.add(sigTable);
    }

    private void addSignatureCell(PdfPTable table, String label, String url)
            throws DocumentException {
        Font f = new Font(Font.HELVETICA, 9, Font.BOLD, COLOR_SECONDARY);
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Phrase(label, f));

        if (url != null) {
            try {
                byte[] imgBytes = storageService.loadFromUrl(url);
                Image img = Image.getInstance(imgBytes);
                img.scaleToFit(140, 60);
                cell.addElement(img);
            } catch (Exception e) {
                Font italicFont = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY);
                cell.addElement(new Phrase("[signature]", italicFont));
            }
        } else {
            Font italicFont = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY);
            cell.addElement(new Phrase("En attente...", italicFont));
        }

        cell.setPadding(8);
        cell.setMinimumHeight(80f);
        cell.setBorderColor(Color.LIGHT_GRAY);
        table.addCell(cell);
    }

    private void addQrCode(Document doc, PdfWriter writer, Intervention inv)
            throws DocumentException, IOException {
        addSectionHeader(doc, "10. QR CODE — FICHE NUMÉRIQUE");
        doc.add(Chunk.NEWLINE);

        String qrContent = baseUrl + "/api/interventions/" + inv.getId();

        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(
                qrContent, BarcodeFormat.QR_CODE, 150, 150,
                Map.of(EncodeHintType.MARGIN, 1)
            );

            ByteArrayOutputStream qrBaos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrBaos);
            Image qrImage = Image.getInstance(qrBaos.toByteArray());
            qrImage.scaleToFit(100, 100);

            Font urlFont = new Font(Font.HELVETICA, 7, Font.ITALIC, Color.GRAY);

            PdfPTable qrTable = new PdfPTable(2);
            qrTable.setWidthPercentage(60);
            qrTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell imgCell = new PdfPCell(qrImage, false);
            imgCell.setPadding(5);
            imgCell.setBorderColor(Color.LIGHT_GRAY);
            qrTable.addCell(imgCell);

            PdfPCell textCell = new PdfPCell();
            textCell.addElement(new Phrase("Scannez pour accéder\nà la fiche numérique :\n" + qrContent, urlFont));
            textCell.setPadding(5);
            textCell.setBorderColor(Color.LIGHT_GRAY);
            textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qrTable.addCell(textCell);

            doc.add(qrTable);

        } catch (WriterException e) {
            log.warn("QR Code non généré : {}", e.getMessage());
        }
    }

    private String orEmpty(Object val) {
        return val != null ? val.toString() : "—";
    }

    private static class HeaderFooter extends PdfPageEventHelper {
        private final Intervention inv;
        private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.of("Africa/Lome"));

        HeaderFooter(Intervention inv) { this.inv = inv; }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Font f = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY);

            String footer = "NG-STARs — Rapport d'intervention — "
                + "Page " + writer.getPageNumber()
                + " — Généré le " + FMT.format(java.time.Instant.now());

            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                new Phrase(footer, f),
                document.getPageSize().getWidth() / 2, 25, 0);

            cb.setColorStroke(new Color(0x1A, 0x56, 0xDB));
            cb.setLineWidth(0.5f);
            cb.moveTo(document.leftMargin(), 35);
            cb.lineTo(document.getPageSize().getWidth() - document.rightMargin(), 35);
            cb.stroke();
        }
    }
}
