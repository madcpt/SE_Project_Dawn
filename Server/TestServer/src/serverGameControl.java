import java.util.ArrayList;
import java.util.List;

public class serverGameControl {
    private List<String> playerList;
    private Data dataclass;

    public serverGameControl(){
        playerList = new ArrayList<>();
        dataclass = new Data();
    }

    void addPlayer(String pureIP){
        if(playerList.contains(pureIP)){
            System.out.println(pureIP + " : Already prepared.");
        }else{
            playerList.add(pureIP);
            dataclass.newPlayer(pureIP);
        }
        System.out.println("Prepared: " + playerList);
    }
}
