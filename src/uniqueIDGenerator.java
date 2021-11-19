import java.util.ArrayList;

public class uniqueIDGenerator {
    private static final ArrayList<Integer> UIDS = new ArrayList<>();
    private static final ArrayList<Integer> OIDS = new ArrayList<>();
    private static final ArrayList<String> UserNames = new ArrayList<>();
    private static final ArrayList<Integer> SKUS = new ArrayList<>();
    public static int generateUID(){
        return numGenerate(UIDS);
    }
    public static int generateOID(){
        return numGenerate(OIDS);
    }
    private static int numGenerate(ArrayList<Integer> ids){
        int id = -1;
        boolean valid = false;
        while(!valid) {
            id = (int) (Math.random() * 90000) + 10000;
            for (Integer i : ids) {
                if (id == i) {
                    break;
                }
            }
            valid = true;
        }
        ids.add(id);
        return id;
    }
    public static boolean userNameExists(String name){
        for(String s : UserNames){
            if (s.equals(name)){
                UserNames.add(name);
                return true;
            }
        }
        return false;
    }
    public static int SKUGenerate(){
        return numGenerate(SKUS);
    }
}
