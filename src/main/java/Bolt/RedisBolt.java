package Bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class RedisBolt extends BaseRichBolt{
    private HashMap<String, Long> counts = null;
    private Hashtable<String,Long> TopN = null;
    private Jedis jedis = null;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.counts = new HashMap<>();
        this.TopN = new Hashtable<>();
        this.jedis = new Jedis("redis",6379);
    }

    public void execute(Tuple input) {
        String hashtag = input.getStringByField("hashtag");
        Long newcount = input.getLongByField("count");
        this.counts.put(hashtag, newcount);

        if (TopN(hashtag,newcount)) {
            try {
                jedis.flushAll();
                Iterator it = TopN.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    jedis.hset("hash_tag", pair.getKey().toString(), pair.getValue().toString());
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // No output
    }

    public boolean TopN(String hashtag, long newcount){
        boolean fr = false;
        if(this.TopN.isEmpty() || this.TopN.size() < 10){
            TopN.put(hashtag,newcount);
            return true;
        }else{
            Iterator it = TopN.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                if(Long.parseLong(pair.getValue().toString()) < newcount){
                    it.remove();
                    fr = true;
                    break;
                }
            }

            if(fr) {TopN.put(hashtag,newcount);return true;}
            else{return false;}
        }
    }
}
