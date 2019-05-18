import javax.print.attribute.standard.DateTimeAtCompleted;
import java.util.ArrayList;
import java.util.List;

public class serverGameControl {
    private List<String> playerList;
    Data dataclass;

    public serverGameControl(){
        playerList = new ArrayList<>();
        dataclass = new Data();
    }

    void addPlayer(String pureIP,int id,String name){
        if(playerList.contains(pureIP)){
            System.out.println(pureIP + " : Already prepared.");
        }else{
            playerList.add(pureIP);
            dataclass.newPlayer(pureIP,id*100,name);
        }
        System.out.println("Prepared: " + playerList);
    }

}
