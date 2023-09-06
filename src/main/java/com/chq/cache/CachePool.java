package com.chq.cache;

import java.util.HashMap;
import java.util.Map;

public interface CachePool {
     Map<Integer,String> POSITION_CACHE=new HashMap<>();
     Map<Integer,String> CLASS_CACHE=new HashMap<>();
}
