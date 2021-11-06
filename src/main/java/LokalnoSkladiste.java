
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


public class LokalnoSkladiste extends SpecifikacijaSkladista{

    static {
        Manager.registerImpl(new LokalnoSkladiste());
    }

    private String rootDirectoryPath;
    private int brojac = 0;
    private int brojac1 = 0;
    private int files = 0;
    private int memory = 0;


    public LokalnoSkladiste() {
    }

    public void checkPrivileges() {
    }


    @Override
    public String checkAdmin(String path) {
        try {
            String admin = "";

            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(path));
            Map<String, Object> map = gson.fromJson(reader, Map.class);

            for (Map.Entry<String, Object> entry: map.entrySet()) {
                if(entry.getKey().equalsIgnoreCase("admin")){
                    admin += entry.getValue().toString();
                }
            }
            reader.close();
            return admin;
        }catch (Exception e){
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

            for (Map.Entry<String, Object> entry: map.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(key)){
                    value = entry.getValue();
                }
            }
            reader.close();
            return value;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean checkUser(String path, String username1, String password1) {
        try {
            String username2 = "";
            boolean s1 = false;
            String password2 = "";
            boolean s2 = false;

            setRootDirectoryPath(path);
            path = rootDirectoryPath + "\\" + "users.json";

            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(path));

            Map<String, Object> map = gson.fromJson(reader, Map.class);

            for (Map.Entry<String, Object> entry: map.entrySet()) {
                if(entry.getKey().equalsIgnoreCase("username")){
                    if(entry.getValue().toString().equalsIgnoreCase(username1)) {
                        username2 += entry.getValue().toString();
                        System.out.println(username2);
                        s1 = true;
                    }
                }
                if(entry.getKey().equalsIgnoreCase("password")){
                    if(entry.getValue().toString().equalsIgnoreCase(password1)) {
                        password2 += entry.getValue().toString();
                        System.out.println(password2);
                        s2 = true;
                    }
                }
                if(s1 == true && s2 == true){
                    reader.close();
                    return true;
                }

            }
            reader.close();

            return false;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
            System.out.println("Error!");
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
        files++;
        setFiles(files);
        double i = Double.parseDouble(checkConfigType(path, "maxNumber").toString());
        if(i > files) {
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
        files+=n;
        setFiles(files);
        double br = Double.parseDouble(checkConfigType(path, "maxNumber").toString());
        if(br > files) {
            for (int i = 0; i < n; i++) {
                createFile(path, "myFile" + brojac++ + filetype);
            }
        }
    }

    @Override
    public void createMoreFolders(String path, int n) {
        files+=n;
        setFiles(files);
        double br = Double.parseDouble(checkConfigType(path, "maxNumber").toString());
        if(br > files) {
            for (int i = 0; i < n; i++) {
                createFolder(path, "Folder" + brojac1++ );
            }
        }
    }

    @Override
    public void createFolder(String path, String folderName){
        files++;
        setFiles(files);
        double i = Double.parseDouble(checkConfigType(path, "maxNumber").toString());
        if(i > files) {
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
            if(f.delete()){
                System.out.println(name + "deleted");
            }else{
                System.out.println("File was not deleted");
            }
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
        path = path + "\\" + "config.json";
        try {
            Map<String, Object> map = mapConfig(1000000, ".txt", 10, username);
            makeConfig(path, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateConfig(String path, int size, String filetype, int maxNumber) {
            path = path + "\\" + "config.json";
            String admin = checkAdmin(path);
            Map<String, Object> map = mapConfig(size, filetype, maxNumber, admin);
            makeConfig(path, map);
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
             for (Korisnik k : korisnici) {
                 new Gson().toJson(k, writer);
                 System.out.println(i++);
             }
             writer.close();

         }catch (Exception e){
             e.printStackTrace();
         }
    }

    @Override
    public void makeDefaultUser(String path, String username, String password) {

        path = path + "\\" + "users.json";

        try {
            List<Korisnik> korisnici = loadUsers(username, password, true, true, true, true);
            makeUser(path, korisnici);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(String path, String name, String password, String privilege) {
        try {
            if(privilege.equals("1")){
                path = path + "\\" + "users.json";
                List<Korisnik> korisnici = loadUsers(name, password, false, false, true, false);
                makeUser(path, korisnici);
            }
            if(privilege.equals("2")){
                path = path + "\\" + "users.json";
                List<Korisnik> korisnici = loadUsers(name, password, false, true, true, false);
                makeUser(path, korisnici);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
