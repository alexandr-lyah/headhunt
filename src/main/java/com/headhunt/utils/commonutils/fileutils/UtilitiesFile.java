package com.headhunt.utils.commonutils.fileutils;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by sagraw001c on 1/21/15.
 */

public class UtilitiesFile {

    public Properties loadPropFile(String name) {
        Properties prop = new Properties();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
            prop.load(in);
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return prop;
    }

    public Set<String> readFileInSetLowerCase(String file) {
        Set<String> set = new HashSet<>();
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null ) {
                count++;
                if (!line.startsWith("@Attributes:"))
                    if (!line.trim().isEmpty())
                        set.add(line.trim().toLowerCase());
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception ex) {
            System.out.println("Error in line: " + count + "  Exception: " + ex);
        }
        return set;
    }

    public List<String> readFileInList(String file) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null ) {
                count++;
                if (!line.startsWith("@Attributes:"))
                    list.add(line.trim());
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception ex) {
            System.out.println("Error in line: " + count + "  Exception: " + ex);
        }
        return list;
    }

    public List<String> readFileInListAsIs(String file) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null ) {
                count++;
                if (!line.startsWith("@Attributes:"))
                    list.add(line);
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception ex) {
            System.out.println("Error in line: " + count + "  Exception: " + ex);
        }
        return list;
    }

    public List<String> readFileInListGetFirst(String file, String delim) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null ) {
                count++;
                if (!line.startsWith("@Attributes:")) {
                    if (line.contains(";")) {
                        String [] parts = line.split(";");
                        if (parts.length >= 1) {
                            list.add(parts[0].trim());
                        }
                    }
                }
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception ex) {
            System.out.println("Error in line: " + count + "  Exception: " + ex);
        }
        return list;
    }

    public String readFileInString(String file) {
        int count = 0;
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null ) {
                count++;
                if (!line.startsWith("@Attributes:"))
                    builder.append(line.trim()).append("\n");
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception ex) {
            System.out.println("Error in line: " + count + "  Exception: " + ex);
        }
        return builder.toString();
    }

    public List<String> readGunzipFileInList(String file) {
        try {
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
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

    public String readGunzipFileInString(String file) {
        try {
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
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

    public HashMap<String, String> readFileInMap(String file, String delim) {
        HashMap<String, String> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null ) {
                if (!line.startsWith("@Attributes:")) {
                    String[] parts = line.split(delim);
                    map.put(parts[0].trim(), parts[1].trim());
                }
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception ex) {
            System.out.println("Error in readFileIntoMap: " + ex);
        }
        return map;
    }

    public void writeToFile(String file, String builder) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(builder);
            bw.close();
        } catch (Exception ex) {

        }
    }
    
    public void appendToFile(String file, String builder) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
            bw.write(builder);
            bw.close();
        } catch (Exception ex) {

        }
    }

}
