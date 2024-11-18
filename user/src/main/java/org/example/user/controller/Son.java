package org.example.user.controller;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Component
public class Son<K> extends Pair<Integer> {

    private K k;

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {


        // 创建一个MessageDigest实例:
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 反复调用update输入数据:
        md.update("Hello".getBytes("UTF-8"));
        md.update("World".getBytes("UTF-8"));
        byte[] result = md.digest(); // 16 bytes: 68e109f0f40ca72a15e05cc22786f8e6
//        System.out.println(HexFormat.of().formatHex(result));
        String hexString = HexUtils.toHexString(result);


        byte[] input = new byte[]{0x01, 0x02, 0x7f, 0x00};

        Base64.getEncoder().encodeToString(input);
        String b64encoded = Base64.getUrlEncoder().encodeToString(input);
        System.out.println(b64encoded);
        byte[] output = Base64.getUrlDecoder().decode(b64encoded);
        System.out.println(Arrays.toString(output));


        int dd = 1;
        byte k = 2;
        String a = "k";
        byte[] bytes = a.getBytes();

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encode = encoder.encode(bytes);

//        System.out.println(s1);
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decode = decoder.decode(s1);


        String b = new String("1");
        String s = "the quick brown fox jumps over the lazy dog.";
        Pattern p = Pattern.compile("\\wo\\w");

        Matcher m = p.matcher(s);
        while (m.find()) {
            String sub = s.substring(m.start(), m.end());
            System.out.println(sub);
        }
    }

    public void aa() {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] clone = arr.clone();
//        List.of(arr)
        synchronized (this) {

        }
    }

    public static boolean jieyaZip(File dir, StringBuilder parentPath, ZipOutputStream out) throws IOException {

        byte[] buffer = new byte[1024];
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        parentPath.append("/").append(file.getName());
                        jieyaZip(file, parentPath, out); // 递归删除子目录
                    } else {
                        try (FileInputStream inputStream = new FileInputStream(file);) {
                            out.putNextEntry(new ZipEntry(parentPath + "/" + file.getName()));
                            int len;
                            while ((len = inputStream.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                            }
                            out.closeEntry();
                        }
                    }
                }
            }
        }
        return true;
    }
//
//    public static void main(String[] args) {
//        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(file.getParent(),file.getName()+".zip")));
//
//        boolean b = deleteDirectory(file,builder,out);
//
//        out.close();
//    }

    @PostConstruct
    public void init() throws IOException {
//        File file = new File("/Users/man/Desktop/a");
//        StringBuilder builder = new StringBuilder();
//        builder.append(file.getName());
////        new FileOutputStream()
//
//        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
//        LocalDateTime.now(ZoneId.of(timeZone.getID()));
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy MMMM dd", Locale.CHINA);
//        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy MMMM dd", Locale.US);
//        dateTimeFormatter.format(LocalDateTime.now());
//        Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
//        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(file.getParent(),file.getName()+".zip")));

//        boolean b = jieyaZip(file,builder,out);

//        out.close();


//        BufferedReader br = new BufferedReader(new FileReader(file));
//
//        FileInputStream fis = new FileInputStream(file);
//
//        byte[] data = new byte[1024];
//
//        int n = 0;
//        while ((n = fis.read(data)) > 0){
//
//        }
//
//        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(new File(file,"a.zip")))) {
//            zip.putNextEntry(new ZipEntry("a.txt"));
//            zip.putNextEntry(new ZipEntry("b.txt"));
//            zip.putNextEntry(new ZipEntry("c.txt"));
//            zip.write("你好好".getBytes(StandardCharsets.UTF_8));
//        }

//        byte[] data;
//        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
//            output.write("Hello ".getBytes("UTF-8"));
//            output.write("world!".getBytes("UTF-8"));
//            data = output.toByteArray();
//            FileInputStream fileInputStream = new FileInputStream(file);
//            fileInputStream.read(new byte[10]);
//           BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
//           bufferedInputStream.read(new byte[10]);
//            FileOutputStream fileOutputStream = new FileOutputStream("");
//        }
//        System.out.println(new String(data, "UTF-8"));
//
////        file.exists();
//
//        Charset charset = Charset.defaultCharset();
//
////        FileReader fileReader = new FileReader("");
//        List<Pair<Integer>> pairs = Collections.singletonList(new Pair<>());
//        ArrayList<Pair<Integer>> pairs1 = new ArrayList<>(pairs);
//        pairs1.add(new Pair<>());
//        List<Pair<Integer>> pairs2 = Collections.unmodifiableList(pairs1);
//        List<Pair<Integer>> pairs3 = Collections.synchronizedList(pairs1);
//        List<Object> objects1 = Collections.emptyList();
//        boolean empty = CollectionUtils.isEmpty(objects1);
//        System.out.println(empty);
////        pairs.add(new Pair<>());
//
//        ArrayList<? super Number> numbers = new ArrayList<>();
//        numbers.add(11);
//        numbers.add(12);
//        numbers.add(13);
//        Collections.addAll(numbers,12);
//        List<Object> objects = Collections.emptyList();
//        System.out.println(objects);
////        objects.add("aa");
//        new ArrayList<>(objects);
//
//        System.out.println(objects);
//
//        Class<? extends Son> aClass = new Son().getClass();
////        URL resource = aClass.getClassLoader().getResource("application.yml");
//
//        Properties props = new Properties();
//        InputStream resourceAsStream = aClass.getResourceAsStream("/application.yml");
//        props.load(resourceAsStream);
//        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
//
//        byte[] buffer = new byte[1024];
//        if (resourceAsStream != null) {
//            if(resourceAsStream.read()>0){
//                outputStreamWriter.write(resourceAsStream.read(buffer));
//            }
//
//        }

    }

}
