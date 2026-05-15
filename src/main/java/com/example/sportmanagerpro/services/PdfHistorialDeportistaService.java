package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.Deportista;
import com.example.sportmanagerpro.models.HistorialDeportistaResumen;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class PdfHistorialDeportistaService {

    public void generarPdf(
            Deportista deportista,
            HistorialDeportistaResumen resumen,
            File archivo
    ) {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();

            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);

            PDType1Font tituloFont =
                    new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDType1Font textoFont =
                    new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            float y = 750;

            escribir(content, tituloFont, 20, 50, y,
                    "Historial individual de deportista");

            y -= 40;

            escribir(content, textoFont, 12, 50, y,
                    "Nombre: " + deportista.getNombreCompleto());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Posición: " + deportista.getPosicionPrincipal());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Pierna dominante: " + deportista.getPiernaDominante());

            y -= 40;

            escribir(content, tituloFont, 14, 50, y,
                    "Resumen deportivo");

            y -= 30;

            escribir(content, textoFont, 12, 50, y,
                    "Entrenamientos registrados: "
                            + resumen.getTotalEntrenamientos());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Asistencias: "
                            + resumen.getAsistencias());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Faltas: "
                            + resumen.getFaltas());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Retardos: "
                            + resumen.getRetardos());

            y -= 30;

            escribir(content, tituloFont, 14, 50, y,
                    "Rendimiento competitivo");

            y -= 30;

            escribir(content, textoFont, 12, 50, y,
                    "Partidos jugados: "
                            + resumen.getPartidosJugados());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Minutos jugados: "
                            + resumen.getMinutosJugados());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Goles: "
                            + resumen.getGoles());

            y -= 20;

            escribir(content, textoFont, 12, 50, y,
                    "Asistencias: "
                            + resumen.getAsistenciasPartido());

            content.close();

            document.save(archivo);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al generar el PDF.",
                    e
            );
        }
    }

    private void escribir(
            PDPageContentStream content,
            PDType1Font font,
            int size,
            float x,
            float y,
            String texto
    ) throws IOException {

        content.beginText();

        content.setFont(font, size);

        content.newLineAtOffset(x, y);

        content.showText(texto);

        content.endText();
    }
}