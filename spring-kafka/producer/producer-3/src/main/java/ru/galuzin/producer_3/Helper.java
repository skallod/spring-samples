package ru.galuzin.producer_3;

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

public class Helper {

    private static final Log log = LogFactory.getLog(Helper.class);

    public static byte[] getBeforeBytes() throws IOException, URISyntaxException {
        return Files.readAllBytes(Paths.get(Producer3Main.class.getClassLoader().getResource("build.log").toURI()));
    }

    public static byte[] gzipCompress(byte[] uncompressedData) {
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

    public static byte[] gzipUncompress(byte[] compressedData) {
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
