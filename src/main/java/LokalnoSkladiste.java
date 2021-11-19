
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class LokalnoSkladiste extends SpecifikacijaSkladista {

    static {
        Manager.registerImpl(new LokalnoSkladiste());
    }

    private String rootDirectoryPath;
    private String connectedUser;
    private int brojac = 0;
    private int brojac1 = 0;
    private int files = 0;
    private int memory = 0;


    public LokalnoSkladiste() {
    }

    @Override
    public boolean checkPrivileges(String privilege, String path, String connectedUser) {
        return super.checkPrivileges(privilege, path, connectedUser);
    }

    @Override
    public String checkAdmin(String path) {
        return super.checkAdmin(path);
    }

    @Override
    public Object checkConfigType(String path, String key) {
        return super.checkConfigType(path, key);
    }

    @Override
    public boolean checkUser(String path, String username1, String password1) {

        Type type = new TypeToken<Korisnik[]>() {
        }.getType();
        try {
            String username2 = "";
            boolean s1 = false;
            String password2 = "";
            boolean s2 = false;

            setRootDirectoryPath(path);
            path = path + "\\" + "users.json";
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(path));
            Korisnik[] data = gson.fromJson(reader, type);

            for (Korisnik k : data) {
                System.out.println(k.getUsername());
                System.out.println(k.getPassword());

                if (k.getUsername().equalsIgnoreCase(username1)) {
                    username2 += k.getUsername();
                    System.out.println(username2);
                    s1 = true;
                }
                if (k.getPassword().equalsIgnoreCase(password1)) {
                    password2 += k.getPassword();
                    System.out.println(password2);
                    s2 = true;
                }

                if (s1 && s2) {
                    reader.close();
                    connectedUser = k.getUsername();
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean createRoot(String path, String name, String username, String password){
        path = path + "\\" + name;
        File root = new File(path);
        setRootDirectoryPath(path);
        boolean b = root.mkdir();
        if(b){
            try {
                makeDefaultConfig(path, username);
                makeDefaultUser(path, username, password);
                connectedUser = username;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else
            return false;
    }

    @Override
    public boolean checkIfRootExists(String path, String name){
        File f = new File(path);
        return f.exists();
    }

    @Override
    public boolean createFile(String path, String fileName){

        if(checkPrivileges("write", rootDirectoryPath, connectedUser)) {
            int fs = countFiles();
            double i = Double.parseDouble(checkConfigType(rootDirectoryPath, "maxNumber").toString());
            long fm = countFilesMemory();
            double j = Double.parseDouble(checkConfigType(rootDirectoryPath, "sizeOfDir").toString());
            if (i > fs && j >= fm) {
                String p = path + "\\" + fileName;
                File f = new File(p);

                try {
                    f.createNewFile();
                    return true;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        }
        return false;
    }

    @Override
    public String getPath() {

        return rootDirectoryPath;
    }

    @Override
    public void createMoreFiles(String path, int n, String filetype) {
        int fs = countFiles();
        double br =Double.parseDouble(checkConfigType(rootDirectoryPath, "maxNumber").toString());
        if(br>fs) {
            super.createMoreFiles(path, n, filetype);
        }
    }

    @Override
    public void createMoreFolders(String path, int n) {
        int fs = countFiles();
        double br = Double.parseDouble(checkConfigType(rootDirectoryPath,"maxNumber").toString());
        if (br>fs) {
            super.createMoreFolders(path,n);
        }
    }
    /**Proveriiti da li ovo sme*/

    @Override
    public boolean createFolder(String path, String folderName){
        if(checkPrivileges("write", rootDirectoryPath, connectedUser)) {
            System.out.println("dal ovde udje");
            int fs = countFiles();
            double i = Double.parseDouble(checkConfigType(rootDirectoryPath, "maxNumber").toString());
            long fm = countFilesMemory();
            double j = Double.parseDouble(checkConfigType(rootDirectoryPath, "sizeOfDir").toString());
            if (i > fs && j >= fm) {
                String p = path + "\\" + folderName;
                File f = new File(p);
                f.mkdir();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteFile(String path, String name) {
        if(checkPrivileges("delete", rootDirectoryPath, connectedUser)) {
            try {
                File f = new File(path + "\\" + name);
                System.out.println(f);
                f.delete();
                return true;

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean deleteFolder(String path, String name) {

        if(checkPrivileges("delete", rootDirectoryPath, connectedUser)) {
            File file = new File(path + "\\" + name);
            String[] files = file.list();
            System.out.println(file.getTotalSpace());
            if (files.length > 0) {
                for (String s : files) {
                    File currentFile = new File(file.getPath(), s);
                    currentFile.delete();
                    return true;
                }
            }
            file.delete();
            return true;
        }
        return false;
    }

    @Override
    public boolean moveFromTo(String fromPath, String toPath, String file) {

        if(checkPrivileges("edit", rootDirectoryPath, connectedUser)) {
            try {
                Files.move(Paths.get(fromPath + "\\" + file), Paths.get(toPath + "\\" + file), StandardCopyOption.REPLACE_EXISTING);
                return true;

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean downloadFile(String path, String filename) {

        if(checkPrivileges("read", rootDirectoryPath, connectedUser)) {
            try {

                Files.copy(Paths.get(path + "\\" + filename), Paths.get("C:\\Users\\milja\\Downloads\\" + filename), StandardCopyOption.COPY_ATTRIBUTES);
                return true;

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean copyPasteFiles(String fromFolder, String toFolder, String filename) {

        if(checkPrivileges("edit",rootDirectoryPath, connectedUser)){
            try {

                Files.copy(Paths.get(fromFolder + "\\" + filename), Paths.get(toFolder + "\\" + filename), StandardCopyOption.COPY_ATTRIBUTES);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int countFiles() {
        try (Stream<Path> files = Files.list(Paths.get(rootDirectoryPath))) {
            return (int) files.count();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long countFilesMemory() {
        long m = 0;
        for (String f: listFiles(rootDirectoryPath)){
            File file = new File(f);
            m+=file.getTotalSpace();
            return m;
        }
        return 0;
    }

    @Override
    public String[] listFiles(String path) {
        return super.listFiles(path);
    }

    @Override
    public void makeConfig(String path, Map<String, Object> map){
        try {
            Writer writer = new FileWriter(path);
            new Gson().toJson(map, writer);

            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> mapConfig(Object size, String filetype, int maxNumber, String admin) {
        return super.mapConfig(size, filetype, maxNumber, admin);
    }

    @Override
    public void makeDefaultConfig(String path, String username) {
        path = rootDirectoryPath + "\\" + "config.json";
        try {
            Map<String, Object> map = mapConfig(1000000, ".json", 10, username);
            makeConfig(path, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateConfig(String path, int size, String filetype, int maxNumber) {

            if(checkPrivileges("edit",rootDirectoryPath, connectedUser)) {
                path = rootDirectoryPath + "\\" + "config.json";
                String admin = checkAdmin(path);
                Map<String, Object> map = mapConfig(size, filetype, maxNumber, admin);
                makeConfig(path, map);
                return true;
            }else{
                return false;
            }
    }

    @Override
    public List<Korisnik> loadUsers(String username, String password, boolean edit, boolean write, boolean read, boolean delete) {
        return super.loadUsers(username, password, edit, write, read, delete);
    }

    @Override
    public void makeUser(String path, List<Korisnik> korisnici) {
         try {
             int i=0;
             Writer writer = new FileWriter(path);
             new Gson().toJson(korisnici,writer);
             writer.close();

         }catch (Exception e){
             e.printStackTrace();
         }
    }

    @Override
    public void makeDefaultUser(String path, String username, String password) {

        path = rootDirectoryPath + "\\" + "users.json";

        try {
            List<Korisnik> korisnici = loadUsers(username, password, true, true, true, true);
            makeUser(path, korisnici);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addUser(String path, String name, String password, String privilege) {

        if(checkPrivileges("edit",rootDirectoryPath, connectedUser)) {
            try {
                if (privilege.equals("1")) {
                    path = rootDirectoryPath + "\\" + "users.json";
                    List<Korisnik> korisnici = loadUsers(name, password, false, false, true, false);
                    makeUser(path, korisnici);
                }
                if (privilege.equals("2")) {
                    path = rootDirectoryPath + "\\" + "users.json";
                    List<Korisnik> korisnici = loadUsers(name, password, false, true, true, true);
                    makeUser(path, korisnici);
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } return false;
    }

    public String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }

    @Override
    public void setMemory(int memory) {
        this.memory = memory;
    }

    @Override
    public void setFiles(int files) {
        this.files = files;
    }
}
