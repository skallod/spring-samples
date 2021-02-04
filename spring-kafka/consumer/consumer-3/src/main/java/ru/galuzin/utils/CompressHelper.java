package ru.galuzin.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressHelper {

    private static final Log log = LogFactory.getLog(CompressHelper.class);

    public byte[] getBeforeBytes() throws IOException, URISyntaxException {
        return Files.readAllBytes(Paths.get(CompressHelper.class.getClassLoader().getResource("build.log").toURI()));
    }

    public byte[] gzipCompress(byte[] uncompressedData) {
//         bos = null;
//        GZIPOutputStream gzipOS = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(uncompressedData);
            gzipOS.close();
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("", e);
            throw new IllegalStateException();
        }
    }

    public byte[] gzipUncompress(byte[] compressedData) {
//        ByteArrayInputStream bis = null;
//        ByteArrayOutputStream bos = null;
//        GZIPInputStream gzipIS = null;

        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
            byte[] buffer = new byte[1024];
            int len;
            while((len = gzipIS.read(buffer)) != -1){
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }
}
