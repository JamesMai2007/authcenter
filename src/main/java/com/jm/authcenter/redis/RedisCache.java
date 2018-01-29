package com.jm.authcenter.redis;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/1/28.
 */
public class RedisCache implements Cache {
    private RedisTemplate<String, Object> redisTemplate;
    private String name;
    //private Integer defaultTTL = 15 *60; //秒


    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public void setDefaultTTL(Integer defaultTTL) {
        this.defaultTTL = defaultTTL;
    }*/

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        // TODO Auto-generated method stub
        return this.redisTemplate;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        final byte[] keyBs = toByteArray(key);
        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {

                //byte[] key = keyf.getBytes();
                byte[] value = connection.hGet(name.getBytes(),keyBs);
                if (value == null) {
                    return null;
                }

                return toObject(value);

            }
        });

        try {
            callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (object != null ? (T)object : null);
    }

    @Override
    public ValueWrapper get(Object key) {
        final byte[] keyBs = toByteArray(key);
        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {

                //byte[] key = keyf.getBytes();
                byte[] value = connection.hGet(name.getBytes(),keyBs);
                if (value == null) {
                    return null;
                }
                return toObject(value);

            }
        });
        return (object != null ? new SimpleValueWrapper(object) : null);
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        // TODO Auto-generated method stub
        final byte[] keyBs = toByteArray(key);
        final Object valuef = value;
        //final long liveTime = defaultTTL;

        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                //byte[] keyb = keyf.getBytes();
                byte[] valueb = toByteArray(valuef);
                connection.hSet(name.getBytes(),keyBs, valueb);
                /*if (liveTime > 0) {
                    connection.expire(keyBs, liveTime);
                }*/
                return 1L;
            }
        });
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    private byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    private Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public void evict(Object key) {
        // TODO Auto-generated method stub
        final byte[] keyBs = toByteArray(key);
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                return connection.hDel(name.getBytes(),keyBs);
            }
        });
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                //connection.flushDb();
                connection.del(name.getBytes());
                return "ok";
            }
        });
    }

    public List<Object> getAll()
    {
        List<Object> objects = null;
        objects = redisTemplate.execute(new RedisCallback<List<Object>>() {
            public List<Object> doInRedis(RedisConnection connection)
                    throws DataAccessException {

                //byte[] key = keyf.getBytes();
                Map<byte[],byte[]> maps = connection.hGetAll(name.getBytes());
                if (maps == null) {
                    return null;
                }
                List<Object>  list = maps.values().stream().map(val -> {
                    return toObject(val);
                }).collect(Collectors.toList());

                return list;

            }
        });
        return objects;
    }
}
