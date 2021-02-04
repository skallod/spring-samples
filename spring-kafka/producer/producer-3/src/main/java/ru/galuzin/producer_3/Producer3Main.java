package ru.galuzin.producer_3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

public class Producer3Main {

    private static final Log log = LogFactory.getLog(Producer3Main.class);

    public static void main(String [] args) throws IOException, URISyntaxException {
        subMain(args);
    }
    public static void subMain(String [] args) throws IOException, URISyntaxException {
        System.out.println("hello");
        {
            byte[] beforeBytes;
//            try(FileInputStream fis = new FileInputStream(new File("/tmp/textfile.txt"))) {
//                FileChannel channel = fis.getChannel();
//                ByteBuffer bb = ByteBuffer.allocate((int) channel.size());
//                channel.read(bb);
//                beforeBytes = bb.array();
//            }
            beforeBytes = Helper.getBeforeBytes();

//compress
            System.out.println("Before snappy compress size：" + beforeBytes.length + " bytes");
            long startTime1 = System.currentTimeMillis();
            byte[] afterBytes = snappyCompress(beforeBytes);
//            byte[] afterBytes = gzipCompress(beforeBytes);
            long endTime1 = System.currentTimeMillis();
            System.out.println("after snappy compress size：" + afterBytes.length + " bytes");
            System.out.println("snappy compress time elapsed：" + (endTime1 - startTime1)
                    + "ms");

//uncompress
            long startTime2 = System.currentTimeMillis();
//            byte[] resultBytes = gzipUncompress(afterBytes);
            byte[] resultBytes = snappyUnCompress(afterBytes);
            System.out.println("snappy uncompress size：" + resultBytes.length + " bytes");
            long endTime2 = System.currentTimeMillis();
            System.out.println("uncompress time elapsed：" + (endTime2 - startTime2)
                    + "ms");
            try(FileOutputStream fos = new FileOutputStream("temp.log")){
                fos.write(resultBytes);
            }
        }
    }


    public static byte[] snappyUnCompress(byte[] data) {
        try {
            return Snappy.uncompress(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] snappyCompress(byte[] datas) {
        try {
            datas = Snappy.compress(datas);
        } catch (IOException e) {
            log.error("Compress error!", e);
        }
        return datas;
    }

    public static byte[] deflater() {
        try {
            // Encode a String into bytes
            String inputString = "blahblahblah";
            byte[] input = inputString.getBytes("UTF-8");

            // Compress the bytes
            byte[] output = new byte[100];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);
            compresser.end();
            //return output;

            // Decompress the bytes
            Inflater decompresser = new Inflater();
            decompresser.setInput(output, 0, compressedDataLength);
            byte[] result = new byte[16*1024];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            // Decode the bytes into a String
            String outputString = new String(result, 0, resultLength, "UTF-8");
            return result;
        } catch(java.io.UnsupportedEncodingException ex) {
            // handle
        } catch (java.util.zip.DataFormatException ex) {
            // handle
        }
        return null;
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
//        GZIPInputStream gzipIS = null

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
//    public static byte[] inflater (byte[] compresed) {
//
//    }
}
