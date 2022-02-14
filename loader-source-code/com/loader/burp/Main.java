package com.loader.burp;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main
{
    public static String name = "NULL";
    
    private static Scanner sc = new Scanner(System.in);
    private static Keygen kg;
    
    
    public static String execCommand(final String command) {
        try {
            return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getErrorStream())).readLine();
        }
        catch (Exception ex) {
            return null;
        }
    }

    private static String inputBoolean(String value) {
        value = value.trim();
        value = value.toLowerCase();
        if (value.equals("y")) return "Y";
        if (value.equals("n")) return "N";
        return "NULL";
    }
    
    private static String getCommand() {
        int JAVA_VERSION = Integer.parseInt(System.getProperty("java.version").split("\\.")[0]);
        String JAVA_HOME = System.getProperty("java.home");
        File KeygenFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String KeygenFileName = KeygenFile.getName();
        String JAVA_PATH = null;
        String javapath = KeygenFile.getParent() + File.separator + "bin" + File.separator + "java";
        File javafile = new File(javapath);
        if (javafile.exists() && !javafile.isDirectory() && javafile.canExecute()) {
            JAVA_PATH = javafile.getPath();
        }
        if ((javafile = new File(javapath + ".exe")).exists() && !javafile.isDirectory() && javafile.canExecute()) {
            JAVA_PATH = javafile.getPath();
        }
        if (JAVA_PATH != null) {
            String res = Main.execCommand(JAVA_PATH + " -version");
            if (res != null) {
                JAVA_VERSION = Integer.parseInt(res.split("\"")[1].split("\\.")[0]);
            }
        } else {
            javapath = JAVA_HOME + File.separator + "bin" + File.separator + "java";
            javafile = new File(javapath);
            if (javafile.exists() && !javafile.isDirectory() && javafile.canExecute()) {
                JAVA_PATH = javafile.getPath();
            }
            if ((javafile = new File(javapath + ".exe")).exists() && !javafile.isDirectory() && javafile.canExecute()) {
                JAVA_PATH = javafile.getPath();
            }
        }
        String newest_file = "NULL";
        try {
            long newest_time = 0L;
            File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String current_dir = f.isDirectory() ? f.getPath() : f.getParentFile().toString();
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(current_dir, new String[0]), "burpsuite_*.jar");
            for (Path path : dirStream) {
                if (Files.isDirectory(path, new LinkOption[0]) || newest_time >= path.toFile().lastModified()) continue;
                newest_time = path.toFile().lastModified();
                newest_file = path.getFileName().toString();
            }
        }
        catch (Throwable newest_time) {}
        Object cmd = JAVA_PATH;
        if (JAVA_VERSION == 16) {
            cmd = (String)cmd + " --illegal-access=permit";
        }
        if (JAVA_VERSION >= 17) {
            cmd = (String)cmd + " --add-opens=java.base/java.lang=ALL-UNNAMED";
        }
        if (newest_file == "NULL") return "NULL";
        cmd = (String)cmd + " -javaagent:" + KeygenFileName + " -noverify -jar " + newest_file;
        return (String)cmd;
    }
    
    public static void main(final String[] array) throws Throwable {
        String console = "[#]/> ";
        
        System.out.println(banner);
        System.out.println();
        if (getCommand() == "NULL") {
            throwError(2);
            System.out.println("[INFO]  Burpsuite must be named: \"burpsuite_*.jar\"");
            System.out.println("[INFO]  Exiting...\nBye! :)");
            System.exit(0);
        }
        System.out.println("Enter your name/alias that will be displayed in the \"Licenced to\" text:");
        while (true) {
            System.out.print(console);
            if (sc.hasNextLine()) {
                name = sc.nextLine();
                name = name.trim();
                if (name.length() > 30) {
                    throwError(1);
                    continue;
                } else break;
            } else throwError(1);
        }
        kg = new Keygen();
        String licenseText = "Licensed to : " + name;
        String license = kg.generateLicense(licenseText);
        String command = getCommand();
        System.out.println();
        System.out.println("In order to complete the activization you must run the following command:");
        System.out.println("=================================================");
        System.out.println(command);
        System.out.println("=================================================");
        while (true) {
            System.out.print("Do you want to run the command? [Y/n] ");
            String input = inputBoolean(sc.nextLine());
            if (input == "NULL") {
                throwError(1);
                continue;
            } else if (input == "Y") {
                System.out.println("[INFO]  Running...");
                Runtime.getRuntime().exec(Main.getCommand());
                break;
            }
            break;
        }
        System.out.println();
        System.out.println("Input the following activation key:");
        System.out.println("=================================================");
        System.out.println(license);
        System.out.println("=================================================");
        System.out.println();
        System.out.println("Input the request from \"Manual activation\":");
        String request;
        String response;
        while (true) {
            System.out.print(console);
            if (sc.hasNextLine()) {
                request = sc.nextLine();
                request = request.trim();
                response = kg.generateActivation(request);
                if (response == "NULL") {
                    throwError(3);
                    continue;
                } else break;
            } else throwError(1);
        }
        sc.close();
        System.out.println();
        System.out.println("Paste this activision response in BurpSuite:");
        System.out.println("=================================================");
        System.out.println(response);
        System.out.println("=================================================");
        System.exit(0);
    }

    private static void throwError(int i) {
        switch(i) {
        case 1:
            System.out.println("[ERROR] Invalid input");
            break;
        case 2:
            System.out.println("[ERROR] BurpSuite was not found");
            break;
        case 3:
            System.out.println("[ERROR] Error decoding the request");
            break;
        default:
            break;
        }       
    }
    
    public static String banner =
              "██╗      ██████╗  █████╗ ██████╗ ███████╗██████╗ \n"
            + "██║     ██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗\n"
            + "██║     ██║   ██║███████║██║  ██║█████╗  ██████╔╝\n"
            + "██║     ██║   ██║██╔══██║██║  ██║██╔══╝  ██╔══██╗\n"
            + "███████╗╚██████╔╝██║  ██║██████╔╝███████╗██║  ██║\n"
            + "╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝ ╚══════╝╚═╝  ╚═╝\n"
            + "=================================================\n"
            + "          BurpSuite Pro Loader (v2.1)\n"
            + "=================================================";
}