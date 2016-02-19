package com.headhunt.utils.commonutils.hdfsutils;

import com.headhunt.utils.commonutils.fileutils.UtilitiesFile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author sagraw001c
 */
public class UtilitiesHdfs {
    private static final Logger log = Logger.getLogger(UtilitiesHdfs.class);

    private static String hdfsPath;
    private static Configuration configuration = new Configuration();
    private static FileSystem fs = null;

    private static String[] namenodes;

    public UtilitiesHdfs() {
        namenodes = new String[2];
        namenodes[0] = "hdfs://ebdp-ch2-d192p.sys.comcast.net:8020";
        namenodes[1] = "hdfs://ebdp-ch2-d146p.sys.comcast.net:8020";

        // check which is active
        String checkFilePath = "/user/sagraw200";
        this.hdfsPath = namenodes[0];
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(checkFilePath);
            fs = getFileSystem();
            if ( fs.exists(path) ) {
                FileStatus[] fStatus = fs.listStatus(path);
            }
        } catch (Exception ex) {
            this.hdfsPath = namenodes[1];
        }
        System.out.println(hdfsPath + "\t is active..");
        configuration = new Configuration();
        fs = null;

    }

//    public static void main(String[] args) {
//        UtilitiesHdfs uHdfs = new UtilitiesHdfs();
//        Set<String> l = uHdfs.readFileInSetLowerCase("/user/sagraw200/network_analysis/yes/zip.list");
//        System.out.println(l.size());
//
//    }

    public UtilitiesHdfs(String serverUrlPath) {
        this.hdfsPath = serverUrlPath.trim();
//        setConf();
    }

//    private static void setConf() {
//        try {
//            conf = new Configuration();
//            conf.set("fs.default.name", hdfsPath);
//        } catch (Exception ex) {
//            log.error("Error in setting conf: " + ex);
//            System.exit(-1);
//        }
//    }

    private FileSystem getFileSystem() {
        if (fs == null) {
            try {
                fs = DistributedFileSystem.get(configuration);
            } catch (Exception ex) {
            }
        }
        return fs;
    }

//    private Path getFileSystem(String filePath) {
//        if (fs == null) {
//            try {
//                Path path = new Path(filePath);
//                fs = FileSystem.get(path.toUri(), conf);
//                return path;
//            } catch (Exception ex) {
//                log.error("Error in getting File System: " + ex);
//            }
//        }
//        return null;
//    }

    public String readFile(String filePath) {
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
            String line;
            StringBuilder builder = new StringBuilder();
            while ( (line = br.readLine()) != null ) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (Exception ex) {
            log.error("Error in reading file: " + ex);
        }
        return null;
    }

    public Set<String> readFileInSetLowerCase(String filePath) {
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();

            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
            String line;
            Set<String> set = new HashSet<>();
            while ( (line = br.readLine()) != null ) {
                if (!line.startsWith("@Attributes:"))
                    if (!line.trim().isEmpty())
                        set.add(line.trim().toLowerCase());
            }
            return set;
        } catch (Exception ex) {
            log.error("Error in reading file: " + ex);
        }
        return null;
    }

    public List<String> readFileInList(String filePath) {
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();

            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
            String line;
            List<String> list = new ArrayList<>();
            while ( (line = br.readLine()) != null ) {
                if (!line.startsWith("@Attributes:"))
                    list.add(line.trim());
            }
            return list;
        } catch (Exception ex) {
            log.error("Error in reading file: " + ex);
        }
        return null;
    }

    public List<String> readFileInListAsIs(String filePath) {
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();

            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
            String line;
            List<String> list = new ArrayList<>();
            while ( (line = br.readLine()) != null ) {
                if (!line.startsWith("@Attributes:"))
                    list.add(line);
            }
            return list;
        } catch (Exception ex) {
            log.error("Error in reading file: " + ex);
        }
        return null;
    }

    public void uploadFile(String localFile, String remoteFile) {
        String method = "upload(): ";

        configuration.set("fs.default.name", hdfsPath);
        Path path = new Path(remoteFile);
        fs = getFileSystem();
        try {
            if (fs.exists(path)) {
                log.info("File " + remoteFile + " already exists, deleting");
                fs.delete(path, true);
            }
        } catch (Exception ex) {
            log.info(method + "\t" + ex);
        }

        // Create a new file and write data to it.
        FSDataOutputStream out = null;
        InputStream in = null;
        try {
            out = fs.create(path);
            in = new BufferedInputStream(new FileInputStream(new File(localFile)));
            byte[] b = new byte[1024];
            int numBytes = 0;
            while ((numBytes = in.read(b)) > 0) {
                out.write(b, 0, numBytes);
            }
        } catch (Exception ex) {
            System.out.println(method + "\t" + ex);
        }
        finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
//        fs.close();
            } catch (Exception ex) {
                log.info(method + "\t" + ex);
            }
        }
        log.info(method + "output file created: " + remoteFile);
    }

    public List<String> downloadFile(String remoteFile) {
        String method = "downloadFile(): ";
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<String>();

        configuration.set("fs.default.name", hdfsPath);
        Path path = new Path(remoteFile);
        fs = getFileSystem();
        try {
            if (fs.exists(path)) {
                log.info("File " + remoteFile + " exists, reading");
                FSDataInputStream in =  fs.open(path);
                byte[] buff = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = in.read(buff)) != -1) {
                    sb.append(new String(buff, 0, bytesRead));
                }
                in.close();
                String text = sb.toString();
                String[] parts = text.split("\n");
                for (String p : parts) {
                    list.add(p.trim());
                }
            }
        } catch (Exception ex) {
            log.info(method + "\t" + ex);
        }
        return list;
    }


    /**
     * @param remoteFile
     * @param localFile
     */
    public void downloadFile(String remoteFolder, String remoteFileStartsWith, String localFile) {
        String method = "downloadFile(): ";

        configuration.set("fs.default.name", hdfsPath);
        Path path = new Path(remoteFolder);
        fs = getFileSystem();
        try {
            if (fs.exists(path)) {
                log.info(remoteFolder + " --> " + remoteFolder);
                TreeSet<String> allFiles = getAllFiles(remoteFolder);
                for (String s : allFiles) {
                    if (s.startsWith(remoteFileStartsWith)) {
                        // download
                        path = new Path(remoteFolder+"/"+s);
                        getFileSystem();
                        FSDataInputStream in =  fs.open(path);
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
                        byte[] buff = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = in.read(buff)) != -1) {
                            out.write(buff, 0, bytesRead);
                        }
                        in.close();
                        out.flush();
                        out.close();
                    }
                }
            }
        } catch (Exception ex) {
            log.info(method + "\t" + ex);
        }
    }

    /**
     * @param remoteFile
     * @param localFile
     */
    public void downloadFile(String remoteFile, String localFile) {
        String method = "downloadFile(): ";

        configuration.set("fs.default.name", hdfsPath);
        Path path = new Path(remoteFile);
        fs = getFileSystem();
        try {
            if (fs.exists(path)) {
                log.info(remoteFile + " --> " + localFile);
                FSDataInputStream in =  fs.open(path);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
                byte[] buff = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = in.read(buff)) != -1) {
                    out.write(buff, 0, bytesRead);
                }
                in.close();
                out.flush();
                out.close();
            }
        } catch (Exception ex) {
            log.info(method + "\t" + ex);
        }
    }

    public TreeSet<String> getAllFiles(String folder) {
        String method = "getAllFiles(): ";
        log.info(method);

        TreeSet<String> set = new TreeSet<String>();
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(folder);
            fs = getFileSystem();
            if ( fs.exists(path) ) {
                FileStatus[] fStatus = fs.listStatus(path);
                if (fStatus != null) {
                    for (FileStatus fstat : fStatus) {
                        String name = fstat.getPath().getName();
                        set.add(name);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(method + "\t" + ex);
        }
        return set;
    }

    public void createDir(String folder) {
        String method = "createDir(): ";
        log.info(method);
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(folder);
            fs = getFileSystem();
            if ( fs.exists(path) ) {
                log.info(folder + " exists");
            } else {
                fs.mkdirs(path);
                log.info(folder + " created");
            }
        } catch (IOException ex) {
            log.error(method + "\t" + ex);
        }
    }

    public String getLatestFileInFolder(String folder) {
        String method = "getLatestFileInFolder( hdfs_folder): ";
        log.info(method);

        List<String> list = new ArrayList<>();
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(folder);
            fs = getFileSystem();
            if ( fs.exists(path) ) {
                FileStatus[] fStatus = fs.listStatus(path);
                if (fStatus != null) {
                    for (FileStatus fstat : fStatus) {
                        String name = fstat.getPath().getName();
                        list.add(name);
                    }
                    Collections.sort(list);
                    return list.get(list.size() - 1);
                }
            }
        } catch (Exception ex) {
            log.error(method + "\t" + ex);
        }
        return null;
    }
    public List<String> readGunzipFileInList(String filePath) {
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();

            BufferedReader br = new BufferedReader(new InputStreamReader( new GZIPInputStream(fs.open(path))));
            List<String> list = new ArrayList<>();
            String line;
            while ( (line = br.readLine()) != null ) {
                list.add(line);
            }
            return list;
        } catch (Exception ex) {
            System.out.println("Error in reading gunzip file: " + ex);
        }
        return null;
    }

    public List<String> readGunzipFileInList(String filePath, int top) {
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();

            BufferedReader br = new BufferedReader(new InputStreamReader( new GZIPInputStream(fs.open(path))));
            List<String> list = new ArrayList<>();
            String line;
            while ( (line = br.readLine()) != null ) {
                list.add(line);
                top--;
                if (top < 0) {
                    break;
                }
            }
            return list;
        } catch (Exception ex) {
            System.out.println("Error in reading gunzip file: " + ex);
        }
        return null;
    }

    public String readGunzipFileInString(String filePath) {
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();

            BufferedReader br = new BufferedReader(new InputStreamReader( new GZIPInputStream(fs.open(path))));
            StringBuilder builder = new StringBuilder();
            String line;
            while ( (line = br.readLine()) != null ) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (Exception ex) {
            System.out.println("Error in reading gunzip file: " + ex);
        }
        return null;
    }

    public HashMap<String, TreeMap<Integer, String>> customReadNexidiaGunzipFile(String filePath) {
        // interactionid : time : speaker,date,text
        HashMap<String, TreeMap<Integer, String>> map = new HashMap<String, TreeMap<Integer, String>>();
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();
            BufferedReader br = new BufferedReader(new InputStreamReader( new GZIPInputStream(fs.open(path))));

            String line;
            while ( (line = br.readLine()) != null ) {
                String[] parts = line.trim().split("\t");
                if (parts != null && parts.length < 2) {
                    parts = line.trim().split("\u0001");
                }

                if (parts != null && parts.length >= 8) {
                    String id = parts[0].trim();
                    String speaker = parts[1].trim();
                    String transcript = parts[4].trim();
                    String dayid = parts[7].trim();

                    int startMS = -1;
                    if (parts[2].trim() != null && !parts[2].trim().isEmpty()) {
                        startMS = Integer.parseInt(parts[2].trim());
                    }

                    if (startMS != -1) {
                        TreeMap<Integer, String> tmp = new TreeMap<Integer, String>();
                        String val = speaker + "_" + dayid + "_" + transcript;
                        if (map.containsKey(id)) {
                            tmp = map.get(id);
                        }
                        tmp.put(startMS, val);
                        map.put(id, tmp);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in reading nexidia gunzip file: " + ex);
        }
        return map;
    }

    public HashMap<String, TreeMap<Integer, String>> customReadNexidiaPlainFile(String filePath) {
        // interactionid : time : speaker,date,text
        HashMap<String, TreeMap<Integer, String>> map = new HashMap<String, TreeMap<Integer, String>>();
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(filePath);
            fs = getFileSystem();
            BufferedReader br = new BufferedReader(new InputStreamReader( fs.open(path) ));

            String line;
            while ( (line = br.readLine()) != null ) {
                String[] parts = line.trim().split("\t");
                if (parts != null && parts.length < 2) {
                    parts = line.trim().split("\u0001");
                }

                if (parts != null && parts.length >= 8) {
                    String id = parts[0].trim();
                    String speaker = parts[1].trim();
                    String transcript = parts[4].trim();
                    String dayid = parts[7].trim();

                    int startMS = -1;
                    if (parts[2].trim() != null && !parts[2].trim().isEmpty()) {
                        startMS = Integer.parseInt(parts[2].trim());
                    }

                    if (startMS != -1) {
                        TreeMap<Integer, String> tmp = new TreeMap<Integer, String>();
                        String val = speaker + "_" + dayid + "_" + transcript;
                        if (map.containsKey(id)) {
                            tmp = map.get(id);
                        }
                        tmp.put(startMS, val);
                        map.put(id, tmp);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in reading nexidia gunzip file: " + ex);
        }
        return map;
    }

    public String readCustomerFile(String remoteFile, String localFile) {
        UtilitiesFile uf = new UtilitiesFile();
        try {
            configuration.set("fs.default.name", hdfsPath);
            Path path = new Path(remoteFile);
            fs = getFileSystem();

            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
            String line;
            StringBuilder builder = new StringBuilder();
            int lineCount = 0;
            int lineValid = 1;
            int batch = 100000;
            int numBatch = 0;
            while ( (line = br.readLine()) != null ) {
                lineCount++;
                String[] parts = line.split("\\|");
                if ( parts.length >= 27
                        && !parts[2].trim().isEmpty() && !parts[27].trim().isEmpty()) {
                    lineValid++;
                    builder.append(parts[2].trim()).append(" | ")
                            .append(parts[27].trim()).append("\n");
                }
                if (lineValid % batch == 0) {
                    lineValid = 1;
                    numBatch++;
                    uf.appendToFile(localFile, builder.toString());
                    builder = new StringBuilder();
                    System.out.println( (numBatch*batch) + " written." );
                    System.out.println("Total line count: " + lineCount);
                }
//                if (numBatch >= 10) {
//                    break;
//                }
            }
            return builder.toString();
        } catch (Exception ex) {
            log.error("Error in reading file: " + ex);
        }
        return null;
    }
}
