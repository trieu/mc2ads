import redis.clients.jedis.Jedis;

Jedis jedis = new Jedis("127.0.0.1", 6379);
 arr = jedis.hgetAll("campaigns");

output = arr.toString()