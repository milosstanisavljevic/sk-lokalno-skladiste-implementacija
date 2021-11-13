
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
    public boolean checkPrivileges(String privilege) {

        try {
            String path = rootDirectoryPath + "\\" + "users.json";
            Type type = new TypeToken<Korisnik[]>() {
            }.getType();
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(path));
            Korisnik[] data = gson.fromJson(reader, type);

            for (Korisnik k : data) {
                if(k.getUsername().equalsIgnoreCase(connectedUser)) {
                    switch (privilege) {

                        case ("read"):
                            return k.isRead();

                        case ("write"):
                            return k.isWrite();

                        case ("delete"):
                            return k.isDelete();

                        case ("edit"):
                            return k.isEdit();

                    }
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String checkAdmin(String path) {
        try {
            String admin = "";

            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(path));
            Map<String, Object> map = gson.fromJson(reader, Map.class);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("admin")) {
                    admin += entry.getValue().toString();
                }
            }
            reader.close();
            return admin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object checkConfigType(String path, String key) {
        try {
            Object value = null;
            path = rootDirectoryPath + "\\config.json";

            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(path));
            Map<String, Object> map = gson.fromJson(reader, Map.class);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    value = entry.getValue();
                }
            }
            reader.close();
            return value;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    public boolean createRoot(String path, String username, String password){
        File root = new File(path);
        setRootDirectoryPath(path);
        boolean b = root.mkdir();
        if(b){
            try {
                makeDefaultConfig(path, username);
                makeDefaultUser(path, username, password);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else
            return false;
    }

    @Override
    public boolean checkIfRootExists(String path){
        File f = new File(path);
        if(f.exists()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void createFile(String path, String fileName){

        int fs = countFiles();
        double i = Double.parseDouble(checkConfigType(path, "maxNumber").toString());
        if(i > fs) {
            String p = path + "\\" + fileName;
            File f = new File(p);
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createMoreFiles(String path, int n, String filetype) {
        int fs = countFiles();
        double br =Double.parseDouble(checkConfigType(path, "maxNumber").toString());
        if(br>fs) {
            super.createMoreFiles(path, n, filetype);
        }
    }

    @Override
    public void createMoreFolders(String path, int n) {
        int fs = countFiles();
        double br = Double.parseDouble(checkConfigType(path,"maxNumber").toString());
        if (br>fs) {
            super.createMoreFolders(path, n);
        }
    }
    /**Proveriiti da li ovo sme*/

    @Override
    public void createFolder(String path, String folderName){
        int fs = countFiles();
        double i = Double.parseDouble(checkConfigType(path, "maxNumber").toString());
        if(i > fs) {
            String p = path + "\\" + folderName;
            File f = new File(p);
            f.mkdir();
        }
    }

    @Override
    public void deleteFile(String path, String name) {
        //proveravanje privilegije
        try {
            File f = new File(path + "\\" + name);
            f.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFolder(String path, String name) {
        //proveravanje privilegije

        File file = new File(path + "\\" + name);
        String[] files = file.list();
        if(files.length > 0) {
            for (String s : files) {
                File currentFile = new File(file.getPath(), s);
                currentFile.delete();
            }
        }else{
            file.delete();
        }
        file.delete();
    }

    @Override
    public void moveFromTo(String fromPath, String toPath, String file) {

        try {
            Files.move(Paths.get(fromPath + "\\" + file), Paths.get(toPath + "\\" + file), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

    @Override
    public void downloadFile(String path, String filename) {

        try {

            Files.copy(Paths.get(path + "\\" + filename), Paths.get("C:\\Users\\milja\\Downloads\\" + filename), StandardCopyOption.COPY_ATTRIBUTES);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void copyPasteFiles(String fromFolder, String toFolder, String filename) {

        try{

            Files.copy(Paths.get(fromFolder  + "\\" + filename), Paths.get(toFolder + "\\" + filename), StandardCopyOption.COPY_ATTRIBUTES);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int countFiles() {
        try (Stream<Path> files = Files.list(Paths.get(rootDirectoryPath))) {
            int count = (int) files.count();
            return count;
        }catch (Exception e){
            e.printStackTrace();
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

            if(checkPrivileges("read") && checkPrivileges("write") && checkPrivileges("edit") && checkPrivileges("delete")) {
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

        if(checkPrivileges("read") && checkPrivileges("write") && checkPrivileges("edit") && checkPrivileges("delete")) {
            try {
                if (privilege.equals("1")) {
                    path = rootDirectoryPath + "\\" + "users.json";
                    List<Korisnik> korisnici = loadUsers(name, password, false, false, true, false);
                    makeUser(path, korisnici);
                }
                if (privilege.equals("2")) {
                    path = rootDirectoryPath + "\\" + "users.json";
                    List<Korisnik> korisnici = loadUsers(name, password, false, true, true, false);
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
