package br.com.hbsis.importacao.exportacao;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Service
public class ImportExportCSV {
    public BufferedReader reader(MultipartFile arquivo) throws IOException {
        final String linha = "";
        final BufferedReader reader = new BufferedReader(new InputStreamReader(arquivo.getInputStream()));

        /* Pular 1ª linha */
        reader.readLine();

        return reader;
    }

    public String[] array(String linhaVazia, String delimitador){

        return linhaVazia.split(delimitador);
    }

    public PrintWriter exportar(HttpServletResponse response, String fileName, String headerAttributes) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv");

        PrintWriter writer = new PrintWriter(response.getWriter());
        writer.println(headerAttributes);

        return writer;
    }
}
