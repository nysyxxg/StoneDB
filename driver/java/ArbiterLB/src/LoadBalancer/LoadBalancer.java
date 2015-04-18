package LoadBalancer;

import Util.Operations;
import Util.TimeLogger;

import java.util.ArrayList;

/**
 * Created by amaliujia on 15-4-12.
 */
public abstract class LoadBalancer {
    protected ArrayList<DBInstance> instances;

    public ArrayList<LBEmeralddb> dbs;

    protected static final String tempFile = "c.txt";

    public TimeLogger logger;

    public LoadBalancer(){
        instances = new ArrayList<DBInstance>();
        dbs = new ArrayList<LBEmeralddb>();
        logger = new TimeLogger();
    }

    public void addInstance(DBInstance instance){
        instances.add(instance);
    }

    public void addInstance(String ip, int port){
        instances.add(new DBInstance(ip, port));
    }



    public abstract void init();

    public void destroy(){
        for(int i = 0; i < dbs.size(); i++) {
            dbs.get(i).disconnect();
        }
    }

    public abstract void sumbit(Operations e, String Key, String record);

    public abstract void sumbit(Operations e, String Key);

}
